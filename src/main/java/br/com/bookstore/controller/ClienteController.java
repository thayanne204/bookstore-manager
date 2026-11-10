
package br.com.bookstore.controller;

import java.util.List;

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

import br.com.bookstore.dto.ClienteRequestDTO;
import br.com.bookstore.dto.ClienteResponseDTO;
import br.com.bookstore.entity.Cliente;
import br.com.bookstore.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Clientes", description = "Cadastro e consulta de clientes")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Dados enviados inválidos"),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflito com regra de negócio")
})
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    private ClienteResponseDTO converterParaDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getDataNascimento(),
                cliente.getEndereco(),
                cliente.getCidade(),
                cliente.getEstado(),
                cliente.getAtivo()
        );
    }

    private Cliente converterParaEntidade(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        preencherCliente(cliente, dto);
        return cliente;
    }

    private void preencherCliente(Cliente cliente, ClienteRequestDTO dto) {
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setDataNascimento(dto.getDataNascimento());
        cliente.setEndereco(dto.getEndereco());
        cliente.setCidade(dto.getCidade());
        cliente.setEstado(dto.getEstado());
        cliente.setAtivo(dto.getAtivo());
    }

    @Operation(summary = "Cadastrar novo registro")
    @ApiResponse(responseCode = "201", description = "Criado com sucesso")
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> cadastrar(
            @Valid @RequestBody ClienteRequestDTO dto) {

        Cliente cliente = converterParaEntidade(dto);
        Cliente salvo = clienteService.cadastrar(cliente);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(converterParaDTO(salvo));
    }

    @Operation(summary = "Listar todos os registros")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        List<ClienteResponseDTO> clientes = clienteService.listarTodos()
                .stream()
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(clientes);
    }

    @Operation(summary = "Buscar registro por ID")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(
            @PathVariable Long id) {

        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(converterParaDTO(cliente));
    }

    @Operation(summary = "Buscar clientes pelo nome (parcial, sem diferenciar maiúsculas)")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorNome(
            @RequestParam String nome) {

        List<ClienteResponseDTO> clientes = clienteService.buscarPorNome(nome)
                .stream()
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(clientes);
    }

    @Operation(summary = "Atualizar registro existente")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO dto) {

        Cliente dados = converterParaEntidade(dto);
        Cliente atualizado = clienteService.atualizar(id, dados);

        return ResponseEntity.ok(converterParaDTO(atualizado));
    }

    @Operation(summary = "Excluir registro (respeitando as regras de negócio)")
    @ApiResponse(responseCode = "204", description = "Excluído com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        clienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
