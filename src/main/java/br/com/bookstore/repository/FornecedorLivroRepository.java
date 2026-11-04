package br.com.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bookstore.entity.FornecedorLivro;

@Repository
public interface FornecedorLivroRepository extends JpaRepository<FornecedorLivro, Long> {

    List<FornecedorLivro> findByFornecedorId(Long fornecedorId);

    List<FornecedorLivro> findByLivroId(Long livroId);

    boolean existsByFornecedorIdAndLivroId(Long fornecedorId, Long livroId);
}