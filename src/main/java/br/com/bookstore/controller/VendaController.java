
package br.com.bookstore.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bookstore.dto.ItemVendaResponseDTO;
import br.com.bookstore.dto.VendaRequestDTO;
import br.com.bookstore.dto.VendaResponseDTO;
import br.com.bookstore.entity.ItemVenda;
import br.com.bookstore.entity.Venda;
import br.com.bookstore.service.VendaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    private VendaResponseDTO converterParaDTO(Venda venda) {

        List<ItemVendaResponseDTO> itens = venda.getItens()
                .stream()
                .map(this::converterItemParaDTO)
                .toList();

        return new VendaResponseDTO(
                venda.getId(),
                venda.getCliente().getId(),
                venda.getCliente().getNome(),
                venda.getDataVenda(),
                venda.getValorTotal(),
                venda.getDesconto(),
                venda.getFormaPagamento(),
                venda.getStatus(),
                itens
        );
    }

    private ItemVendaResponseDTO converterItemParaDTO(ItemVenda item) {
        return new ItemVendaResponseDTO(
                item.getLivro().getId(),
                item.getLivro().getTitulo(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getSubtotal()
        );
    }

    @PostMapping
    public ResponseEntity<VendaResponseDTO> registrarVenda(
            @Valid @RequestBody VendaRequestDTO dados) {

        Venda venda = vendaService.registrarVenda(dados);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(converterParaDTO(venda));
    }

    @GetMapping
    public ResponseEntity<List<VendaResponseDTO>> listarTodas() {

        List<VendaResponseDTO> vendas = vendaService.listarTodas()
                .stream()
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponseDTO> buscarPorId(
            @PathVariable Long id) {

        Venda venda = vendaService.buscarPorId(id);

        return ResponseEntity.ok(converterParaDTO(venda));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VendaResponseDTO>> buscarPorCliente(
            @PathVariable Long clienteId) {

        List<VendaResponseDTO> vendas = vendaService.buscarPorCliente(clienteId)
                .stream()
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<VendaResponseDTO>> buscarPorStatus(
            @PathVariable String status) {

        List<VendaResponseDTO> vendas = vendaService.buscarPorStatus(status)
                .stream()
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(vendas);
    }
}
