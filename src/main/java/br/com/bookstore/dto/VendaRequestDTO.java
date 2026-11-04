
package br.com.bookstore.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class VendaRequestDTO {

    @NotNull(message = "O cliente é obrigatório.")
    @Positive(message = "O ID do cliente deve ser positivo.")
    private Long clienteId;

    @NotBlank(message = "A forma de pagamento é obrigatória.")
    private String formaPagamento;

    private BigDecimal desconto = BigDecimal.ZERO;

    @NotEmpty(message = "A venda precisa ter pelo menos um item.")
    @Valid
    private List<ItemVendaRequestDTO> itens;

    public VendaRequestDTO() {
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public List<ItemVendaRequestDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemVendaRequestDTO> itens) {
        this.itens = itens;
    }
}
