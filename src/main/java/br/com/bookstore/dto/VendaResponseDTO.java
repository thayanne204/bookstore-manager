
package br.com.bookstore.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class VendaResponseDTO {

    private Long id;
    private Long clienteId;
    private String nomeCliente;
    private LocalDateTime dataVenda;
    private BigDecimal valorTotal;
    private BigDecimal desconto;
    private String formaPagamento;
    private String status;
    private List<ItemVendaResponseDTO> itens;

    public VendaResponseDTO() {
    }

    public VendaResponseDTO(
            Long id,
            Long clienteId,
            String nomeCliente,
            LocalDateTime dataVenda,
            BigDecimal valorTotal,
            BigDecimal desconto,
            String formaPagamento,
            String status,
            List<ItemVendaResponseDTO> itens) {

        this.id = id;
        this.clienteId = clienteId;
        this.nomeCliente = nomeCliente;
        this.dataVenda = dataVenda;
        this.valorTotal = valorTotal;
        this.desconto = desconto;
        this.formaPagamento = formaPagamento;
        this.status = status;
        this.itens = itens;
    }

    public Long getId() { return id; }
    public Long getClienteId() { return clienteId; }
    public String getNomeCliente() { return nomeCliente; }
    public LocalDateTime getDataVenda() { return dataVenda; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public BigDecimal getDesconto() { return desconto; }
    public String getFormaPagamento() { return formaPagamento; }
    public String getStatus() { return status; }
    public List<ItemVendaResponseDTO> getItens() { return itens; }
}
