
package br.com.bookstore.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class LivroRequestDTO {

    @NotBlank(message = "O título é obrigatório.")
    @Size(max = 200, message = "O título deve ter no máximo 200 caracteres.")
    private String titulo;

    @Size(max = 20)
    private String isbn;

    private String descricao;

    private LocalDate dataPublicacao;

    @NotNull(message = "O preço é obrigatório.")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
    private BigDecimal preco;

    @NotNull(message = "A quantidade em estoque é obrigatória.")
    @PositiveOrZero(message = "O estoque não pode ser negativo.")
    private Integer quantidadeEstoque;

    @NotNull(message = "O estoque mínimo é obrigatório.")
    @PositiveOrZero(message = "O estoque mínimo não pode ser negativo.")
    private Integer estoqueMinimo;

    @NotNull(message = "A categoria é obrigatória.")
    private Long categoriaId;

    @NotNull(message = "A editora é obrigatória.")
    private Long editoraId;

    private Boolean ativo = true;

    public LivroRequestDTO() {
    }

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

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }

    public Long getEditoraId() { return editoraId; }
    public void setEditoraId(Long editoraId) { this.editoraId = editoraId; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
