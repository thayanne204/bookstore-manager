package br.com.bookstore.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bookstore.entity.Venda;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByClienteId(Long clienteId);

    List<Venda> findByStatus(String status);

    List<Venda> findByDataVendaBetween(
            LocalDateTime inicio,
            LocalDateTime fim
    );

    List<Venda> findByClienteNomeContainingIgnoreCase(String nomeCliente);
}