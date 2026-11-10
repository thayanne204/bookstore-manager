package br.com.bookstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bookstore.entity.Livro;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByTituloContainingIgnoreCase(String titulo);

    Optional<Livro> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    boolean existsByCategoriaId(Long categoriaId);

    boolean existsByEditoraId(Long editoraId);

    List<Livro> findByAtivoTrue();

    List<Livro> findByQuantidadeEstoqueLessThanEqual(Integer quantidade);
}