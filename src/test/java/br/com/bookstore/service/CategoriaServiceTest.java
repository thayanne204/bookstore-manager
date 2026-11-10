package br.com.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.bookstore.entity.Categoria;
import br.com.bookstore.exception.BusinessException;
import br.com.bookstore.exception.ResourceNotFoundException;
import br.com.bookstore.repository.CategoriaRepository;
import br.com.bookstore.repository.LivroRepository;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void cadastrarCategoriaDuplicadaLancaExcecao() {
        Categoria categoria = new Categoria();
        categoria.setNome("Romance");
        when(categoriaRepository.existsByNome("Romance")).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> categoriaService.cadastrar(categoria));

        verify(categoriaRepository, never()).save(any());
    }

    @Test
    void excluirCategoriaComLivrosLancaExcecao() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(livroRepository.existsByCategoriaId(1L)).thenReturn(true);

        assertThrows(BusinessException.class, () -> categoriaService.excluir(1L));

        verify(categoriaRepository, never()).delete(any());
    }

    @Test
    void buscarCategoriaInexistenteLancaNaoEncontrado() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoriaService.buscarPorId(99L));
    }
}
