package br.com.bookstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bookstore.entity.Editora;

@Repository
public interface EditoraRepository extends JpaRepository<Editora, Long> {

    Optional<Editora> findByNome(String nome);

    boolean existsByNome(String nome);
}