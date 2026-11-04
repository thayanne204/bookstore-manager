
package br.com.bookstore.dto;

import java.math.BigDecimal;

public class ItemVendaResponseDTO {

    private Long livroId;
    private String tituloLivro;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;

    public ItemVendaResponseDTO() {
    }

    public ItemVendaResponseDTO(
            Long livroId,
            String tituloLivro,
            Integer quantidade,
            BigDecimal precoUnitario,
            BigDecimal subtotal) {

        this.livroId = livroId;
        this.tituloLivro = tituloLivro;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = subtotal;
    }

    public Long getLivroId() { return livroId; }
    public String getTituloLivro() { return tituloLivro; }
    public Integer getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public BigDecimal getSubtotal() { return subtotal; }
}
