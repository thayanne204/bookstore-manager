
package br.com.bookstore.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.bookstore.dto.ItemVendaResponseDTO;
import br.com.bookstore.dto.VendaRequestDTO;
import br.com.bookstore.dto.VendaResponseDTO;
import br.com.bookstore.dto.VendaUpdateRequestDTO;
import br.com.bookstore.entity.ItemVenda;
import br.com.bookstore.entity.Venda;
import br.com.bookstore.service.VendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Vendas", description = "Registro de vendas com controle de estoque")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Dados enviados inválidos"),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflito com regra de negócio")
})
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

    @Operation(summary = "Registrar venda, validando estoque e baixando os livros")
    @ApiResponse(responseCode = "201", description = "Criado com sucesso")
    @PostMapping
    public ResponseEntity<VendaResponseDTO> registrarVenda(
            @Valid @RequestBody VendaRequestDTO dados) {

        Venda venda = vendaService.registrarVenda(dados);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(converterParaDTO(venda));
    }

    @Operation(summary = "Listar todos os registros")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping
    public ResponseEntity<List<VendaResponseDTO>> listarTodas() {

        List<VendaResponseDTO> vendas = vendaService.listarTodas()
                .stream()
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(vendas);
    }

    @Operation(summary = "Buscar registro por ID")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping("/{id}")
    public ResponseEntity<VendaResponseDTO> buscarPorId(
            @PathVariable Long id) {

        Venda venda = vendaService.buscarPorId(id);

        return ResponseEntity.ok(converterParaDTO(venda));
    }

    @Operation(summary = "Listar vendas de um cliente")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VendaResponseDTO>> buscarPorCliente(
            @PathVariable Long clienteId) {

        List<VendaResponseDTO> vendas = vendaService.buscarPorCliente(clienteId)
                .stream()
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(vendas);
    }

    @Operation(summary = "Listar vendas por status (CONCLUIDA ou CANCELADA)")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<VendaResponseDTO>> buscarPorStatus(
            @PathVariable String status) {

        List<VendaResponseDTO> vendas = vendaService.buscarPorStatus(status)
                .stream()
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(vendas);
    }

    @Operation(summary = "Listar vendas em um período (datas no formato AAAA-MM-DD)")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping("/periodo")
    public ResponseEntity<List<VendaResponseDTO>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        List<VendaResponseDTO> vendas = vendaService
                .buscarPorPeriodo(inicio.atStartOfDay(), fim.atTime(LocalTime.MAX))
                .stream()
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(vendas);
    }

    @Operation(summary = "Atualizar registro existente")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @PutMapping("/{id}")
    public ResponseEntity<VendaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody VendaUpdateRequestDTO dto) {

        Venda venda = vendaService.atualizar(id, dto.getFormaPagamento());

        return ResponseEntity.ok(converterParaDTO(venda));
    }

    @Operation(summary = "Cancelar venda e devolver os livros ao estoque")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<VendaResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(converterParaDTO(vendaService.cancelar(id)));
    }

    @Operation(summary = "Excluir registro (respeitando as regras de negócio)")
    @ApiResponse(responseCode = "204", description = "Excluído com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        vendaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
