
package br.com.bookstore.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bookstore.dto.ItemVendaRequestDTO;
import br.com.bookstore.dto.VendaRequestDTO;
import br.com.bookstore.entity.Cliente;
import br.com.bookstore.entity.ItemVenda;
import br.com.bookstore.entity.Livro;
import br.com.bookstore.entity.MovimentacaoEstoque;
import br.com.bookstore.entity.TipoMovimentacao;
import br.com.bookstore.entity.Venda;
import br.com.bookstore.exception.BusinessException;
import br.com.bookstore.exception.ResourceNotFoundException;
import br.com.bookstore.repository.ClienteRepository;
import br.com.bookstore.repository.LivroRepository;
import br.com.bookstore.repository.MovimentacaoEstoqueRepository;
import br.com.bookstore.repository.VendaRepository;

@Service
public class VendaService {

    private static final String STATUS_CONCLUIDA = "CONCLUIDA";
    private static final String STATUS_CANCELADA = "CANCELADA";

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final LivroRepository livroRepository;
    private final MovimentacaoEstoqueRepository movimentacaoRepository;

    public VendaService(
            VendaRepository vendaRepository,
            ClienteRepository clienteRepository,
            LivroRepository livroRepository,
            MovimentacaoEstoqueRepository movimentacaoRepository) {

        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
        this.livroRepository = livroRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @Transactional
    public Venda registrarVenda(VendaRequestDTO dados) {

        Cliente cliente = clienteRepository.findById(dados.getClienteId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente não encontrado."));

        if (Boolean.FALSE.equals(cliente.getAtivo())) {
            throw new BusinessException(
                    "Não é possível vender para um cliente inativo.");
        }

        if (dados.getItens() == null || dados.getItens().isEmpty()) {
            throw new BusinessException(
                    "A venda precisa possuir pelo menos um item.");
        }

        BigDecimal desconto = dados.getDesconto() == null
                ? BigDecimal.ZERO
                : dados.getDesconto();

        if (desconto.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("O desconto não pode ser negativo.");
        }

        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setFormaPagamento(dados.getFormaPagamento());
        venda.setDesconto(desconto);
        venda.setStatus(STATUS_CONCLUIDA);

        List<ItemVenda> itens = new ArrayList<>();
        BigDecimal subtotalGeral = BigDecimal.ZERO;

        for (ItemVendaRequestDTO itemDTO : dados.getItens()) {

            if (itemDTO.getQuantidade() == null
                    || itemDTO.getQuantidade() <= 0) {
                throw new BusinessException(
                        "A quantidade deve ser maior que zero.");
            }

            Livro livro = livroRepository.findById(itemDTO.getLivroId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Livro não encontrado: " + itemDTO.getLivroId()));

            if (Boolean.FALSE.equals(livro.getAtivo())) {
                throw new BusinessException(
                        "O livro está inativo: " + livro.getTitulo());
            }

            if (livro.getQuantidadeEstoque() < itemDTO.getQuantidade()) {
                throw new BusinessException(
                        "Estoque insuficiente para o livro: " + livro.getTitulo());
            }

            BigDecimal precoUnitario = livro.getPreco();

            BigDecimal subtotal = precoUnitario.multiply(
                    BigDecimal.valueOf(itemDTO.getQuantidade()));

            ItemVenda item = new ItemVenda();
            item.setVenda(venda);
            item.setLivro(livro);
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(precoUnitario);
            item.setSubtotal(subtotal);

            itens.add(item);
            subtotalGeral = subtotalGeral.add(subtotal);

            livro.setQuantidadeEstoque(
                    livro.getQuantidadeEstoque() - itemDTO.getQuantidade());

            livroRepository.save(livro);
        }

        if (desconto.compareTo(subtotalGeral) > 0) {
            throw new BusinessException(
                    "O desconto não pode ser maior que o valor da venda.");
        }

        venda.setValorTotal(subtotalGeral.subtract(desconto));
        venda.setItens(itens);

        Venda vendaSalva = vendaRepository.save(venda);

        for (ItemVenda item : itens) {

            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();

            movimentacao.setLivro(item.getLivro());
            movimentacao.setTipo(TipoMovimentacao.SAIDA);
            movimentacao.setQuantidade(item.getQuantidade());
            movimentacao.setMotivo("Venda #" + vendaSalva.getId());

            movimentacaoRepository.save(movimentacao);
        }

        return vendaSalva;
    }

    @Transactional
    public Venda atualizar(Long id, String formaPagamento) {
        Venda venda = buscarPorId(id);

        if (!STATUS_CONCLUIDA.equals(venda.getStatus())) {
            throw new BusinessException(
                    "Só é possível alterar vendas com status CONCLUIDA.");
        }

        venda.setFormaPagamento(formaPagamento);

        return vendaRepository.save(venda);
    }

    @Transactional
    public Venda cancelar(Long id) {
        Venda venda = buscarPorId(id);

        if (STATUS_CANCELADA.equals(venda.getStatus())) {
            throw new BusinessException("A venda já está cancelada.");
        }

        for (ItemVenda item : venda.getItens()) {
            Livro livro = item.getLivro();

            livro.setQuantidadeEstoque(
                    livro.getQuantidadeEstoque() + item.getQuantidade());
            livroRepository.save(livro);

            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
            movimentacao.setLivro(livro);
            movimentacao.setTipo(TipoMovimentacao.ENTRADA);
            movimentacao.setQuantidade(item.getQuantidade());
            movimentacao.setMotivo("Cancelamento da venda #" + venda.getId());

            movimentacaoRepository.save(movimentacao);
        }

        venda.setStatus(STATUS_CANCELADA);

        return vendaRepository.save(venda);
    }

    @Transactional
    public void excluir(Long id) {
        Venda venda = buscarPorId(id);

        if (!STATUS_CANCELADA.equals(venda.getStatus())) {
            throw new BusinessException(
                    "Só é possível excluir vendas canceladas. Cancele a venda primeiro.");
        }

        vendaRepository.delete(venda);
    }

    public List<Venda> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio.isAfter(fim)) {
            throw new BusinessException("A data inicial não pode ser posterior à data final.");
        }

        return vendaRepository.findByDataVendaBetween(inicio, fim);
    }

    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }

    public Venda buscarPorId(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Venda não encontrada."));
    }

    public List<Venda> buscarPorCliente(Long clienteId) {
        return vendaRepository.findByClienteId(clienteId);
    }

    public List<Venda> buscarPorStatus(String status) {
        return vendaRepository.findByStatus(status);
    }
}
