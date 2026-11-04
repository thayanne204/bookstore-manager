
package br.com.bookstore.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.bookstore.entity.Livro;
import br.com.bookstore.exception.BusinessException;
import br.com.bookstore.exception.ResourceNotFoundException;
import br.com.bookstore.repository.LivroRepository;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    public Livro buscarPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Livro não encontrado."));
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Livro> listarAtivos() {
        return livroRepository.findByAtivoTrue();
    }

    public List<Livro> listarEstoqueBaixo(int limite) {
        if (limite < 0) {
            throw new BusinessException("O limite de estoque não pode ser negativo.");
        }

        return livroRepository.findByQuantidadeEstoqueLessThanEqual(limite);
    }

    public Livro cadastrar(Livro livro) {
        validarLivro(livro);

        if (livro.getIsbn() != null && !livro.getIsbn().isBlank()
                && livroRepository.existsByIsbn(livro.getIsbn())) {
            throw new BusinessException("Já existe um livro cadastrado com esse ISBN.");
        }

        return livroRepository.save(livro);
    }

    public Livro atualizar(Long id, Livro dados) {
        Livro livro = buscarPorId(id);

        validarLivro(dados);

        if (dados.getIsbn() != null && !dados.getIsbn().isBlank()
                && !dados.getIsbn().equals(livro.getIsbn())
                && livroRepository.existsByIsbn(dados.getIsbn())) {
            throw new BusinessException("Já existe um livro cadastrado com esse ISBN.");
        }

        livro.setTitulo(dados.getTitulo());
        livro.setIsbn(dados.getIsbn());
        livro.setDescricao(dados.getDescricao());
        livro.setDataPublicacao(dados.getDataPublicacao());
        livro.setPreco(dados.getPreco());
        livro.setQuantidadeEstoque(dados.getQuantidadeEstoque());
        livro.setEstoqueMinimo(dados.getEstoqueMinimo());
        livro.setCategoria(dados.getCategoria());
        livro.setEditora(dados.getEditora());
        livro.setAutores(dados.getAutores());
        livro.setAtivo(dados.getAtivo());

        return livroRepository.save(livro);
    }

    public void excluir(Long id) {
        Livro livro = buscarPorId(id);
        livroRepository.delete(livro);
    }

    private void validarLivro(Livro livro) {
        if (livro.getPreco() == null
                || livro.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O preço do livro deve ser maior que zero.");
        }

        if (livro.getQuantidadeEstoque() == null
                || livro.getQuantidadeEstoque() < 0) {
            throw new BusinessException("A quantidade em estoque não pode ser negativa.");
        }

        if (livro.getEstoqueMinimo() == null
                || livro.getEstoqueMinimo() < 0) {
            throw new BusinessException("O estoque mínimo não pode ser negativo.");
        }

        if (livro.getCategoria() == null || livro.getEditora() == null) {
            throw new BusinessException("O livro precisa ter uma categoria e uma editora.");
        }
    }
}
