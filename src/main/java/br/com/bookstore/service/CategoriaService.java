
package br.com.bookstore.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.bookstore.entity.Categoria;
import br.com.bookstore.exception.BusinessException;
import br.com.bookstore.exception.ResourceNotFoundException;
import br.com.bookstore.repository.CategoriaRepository;
import br.com.bookstore.repository.LivroRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final LivroRepository livroRepository;

    public CategoriaService(
            CategoriaRepository categoriaRepository,
            LivroRepository livroRepository) {
        this.categoriaRepository = categoriaRepository;
        this.livroRepository = livroRepository;
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoria não encontrada."));
    }

    public Categoria cadastrar(Categoria categoria) {
        if (categoriaRepository.existsByNome(categoria.getNome())) {
            throw new BusinessException("Já existe uma categoria com esse nome.");
        }

        return categoriaRepository.save(categoria);
    }

    public Categoria atualizar(Long id, Categoria dados) {
        Categoria categoria = buscarPorId(id);

        boolean nomeDuplicado = categoriaRepository.existsByNome(dados.getNome())
                && !categoria.getNome().equalsIgnoreCase(dados.getNome());

        if (nomeDuplicado) {
            throw new BusinessException("Já existe uma categoria com esse nome.");
        }

        categoria.setNome(dados.getNome());
        categoria.setDescricao(dados.getDescricao());
        categoria.setAtivo(dados.getAtivo());

        return categoriaRepository.save(categoria);
    }

    public void excluir(Long id) {
        Categoria categoria = buscarPorId(id);

        if (livroRepository.existsByCategoriaId(id)) {
            throw new BusinessException(
                    "Não é possível excluir a categoria porque existem livros vinculados a ela.");
        }

        categoriaRepository.delete(categoria);
    }
}
