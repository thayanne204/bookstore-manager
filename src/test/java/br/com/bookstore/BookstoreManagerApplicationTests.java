package br.com.bookstore;

import org.junit.jupiter.api.Test;

import br.com.bookstore.exception.BusinessException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Teste simples que não exige MySQL em execução.
 * Para testes de integração (@SpringBootTest), configure um banco de teste (ex.: H2 ou Testcontainers).
 */
class BookstoreManagerApplicationTests {

    @Test
    void businessExceptionGuardaMensagem() {
        assertEquals("erro", new BusinessException("erro").getMessage());
    }
}
