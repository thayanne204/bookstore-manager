package br.com.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VendaUpdateRequestDTO {

    @NotBlank(message = "A forma de pagamento é obrigatória.")
    @Size(max = 30, message = "A forma de pagamento deve ter no máximo 30 caracteres.")
    private String formaPagamento;

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
}
