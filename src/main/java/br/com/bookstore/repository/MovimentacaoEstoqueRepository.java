package br.com.bookstore.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bookstore.entity.MovimentacaoEstoque;
import br.com.bookstore.entity.TipoMovimentacao;

@Repository
public interface MovimentacaoEstoqueRepository
        extends JpaRepository<MovimentacaoEstoque, Long> {

    List<MovimentacaoEstoque> findByLivroId(Long livroId);

    List<MovimentacaoEstoque> findByTipo(TipoMovimentacao tipo);

    List<MovimentacaoEstoque> findByDataMovimentacaoBetween(
            LocalDateTime inicio,
            LocalDateTime fim
    );
}