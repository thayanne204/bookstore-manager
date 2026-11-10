package br.com.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.bookstore.dto.ItemVendaRequestDTO;
import br.com.bookstore.dto.VendaRequestDTO;
import br.com.bookstore.entity.Cliente;
import br.com.bookstore.entity.Livro;
import br.com.bookstore.entity.Venda;
import br.com.bookstore.exception.BusinessException;
import br.com.bookstore.repository.ClienteRepository;
import br.com.bookstore.repository.LivroRepository;
import br.com.bookstore.repository.MovimentacaoEstoqueRepository;
import br.com.bookstore.repository.VendaRepository;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private LivroRepository livroRepository;
    @Mock
    private MovimentacaoEstoqueRepository movimentacaoRepository;

    @InjectMocks
    private VendaService vendaService;

    private Cliente cliente;
    private Livro livro;

    @BeforeEach
    void preparar() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Maria");

        livro = new Livro();
        livro.setId(2L);
        livro.setTitulo("Dom Casmurro");
        livro.setPreco(new BigDecimal("50.00"));
        livro.setQuantidadeEstoque(10);
    }

    private VendaRequestDTO criarRequisicao(int quantidade, String desconto) {
        ItemVendaRequestDTO item = new ItemVendaRequestDTO();
        item.setLivroId(2L);
        item.setQuantidade(quantidade);

        VendaRequestDTO dto = new VendaRequestDTO();
        dto.setClienteId(1L);
        dto.setFormaPagamento("PIX");
        dto.setDesconto(new BigDecimal(desconto));
        dto.setItens(List.of(item));
        return dto;
    }

    @Test
    void registrarVendaCalculaTotalComDescontoEBaixaEstoque() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(livroRepository.findById(2L)).thenReturn(Optional.of(livro));
        when(vendaRepository.save(any(Venda.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        Venda venda = vendaService.registrarVenda(criarRequisicao(3, "10.00"));

        assertEquals(new BigDecimal("140.00"), venda.getValorTotal());
        assertEquals(7, livro.getQuantidadeEstoque());
        assertEquals("CONCLUIDA", venda.getStatus());
    }

    @Test
    void registrarVendaComEstoqueInsuficienteLancaExcecao() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(livroRepository.findById(2L)).thenReturn(Optional.of(livro));

        assertThrows(BusinessException.class,
                () -> vendaService.registrarVenda(criarRequisicao(11, "0")));

        verify(vendaRepository, never()).save(any());
    }

    @Test
    void registrarVendaParaClienteInativoLancaExcecao() {
        cliente.setAtivo(false);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        assertThrows(BusinessException.class,
                () -> vendaService.registrarVenda(criarRequisicao(1, "0")));
    }

    @Test
    void excluirVendaNaoCanceladaLancaExcecao() {
        Venda venda = new Venda();
        venda.setId(5L);
        venda.setStatus("CONCLUIDA");
        when(vendaRepository.findById(5L)).thenReturn(Optional.of(venda));

        assertThrows(BusinessException.class, () -> vendaService.excluir(5L));

        verify(vendaRepository, never()).delete(any());
    }
}
