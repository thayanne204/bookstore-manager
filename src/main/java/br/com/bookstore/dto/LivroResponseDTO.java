
package br.com.bookstore.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LivroResponseDTO {

    private Long id;
    private String titulo;
    private String isbn;
    private String descricao;
    private LocalDate dataPublicacao;
    private BigDecimal preco;
    private Integer quantidadeEstoque;
    private Integer estoqueMinimo;
    private String nomeCategoria;
    private String nomeEditora;
    private Boolean ativo;

    public LivroResponseDTO() {
    }

    public LivroResponseDTO(
            Long id,
            String titulo,
            String isbn,
            String descricao,
            LocalDate dataPublicacao,
            BigDecimal preco,
            Integer quantidadeEstoque,
            Integer estoqueMinimo,
            String nomeCategoria,
            String nomeEditora,
            Boolean ativo) {

        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
        this.descricao = descricao;
        this.dataPublicacao = dataPublicacao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.estoqueMinimo = estoqueMinimo;
        this.nomeCategoria = nomeCategoria;
        this.nomeEditora = nomeEditora;
        this.ativo = ativo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDate getDataPublicacao() { return dataPublicacao; }
    public void setDataPublicacao(LocalDate dataPublicacao) { this.dataPublicacao = dataPublicacao; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public Integer getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(Integer quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }

    public Integer getEstoqueMinimo() { return estoqueMinimo; }
    public void setEstoqueMinimo(Integer estoqueMinimo) { this.estoqueMinimo = estoqueMinimo; }

    public String getNomeCategoria() { return nomeCategoria; }
    public void setNomeCategoria(String nomeCategoria) { this.nomeCategoria = nomeCategoria; }

    public String getNomeEditora() { return nomeEditora; }
    public void setNomeEditora(String nomeEditora) { this.nomeEditora = nomeEditora; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
