
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
import jakarta.validation.Valid;

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

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> cadastrar(
            @Valid @RequestBody ClienteRequestDTO dto) {

        Cliente cliente = converterParaEntidade(dto);
        Cliente salvo = clienteService.cadastrar(cliente);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(converterParaDTO(salvo));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        List<ClienteResponseDTO> clientes = clienteService.listarTodos()
                .stream()
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(
            @PathVariable Long id) {

        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(converterParaDTO(cliente));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorNome(
            @RequestParam String nome) {

        List<ClienteResponseDTO> clientes = clienteService.buscarPorNome(nome)
                .stream()
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(clientes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO dto) {

        Cliente dados = converterParaEntidade(dto);
        Cliente atualizado = clienteService.atualizar(id, dados);

        return ResponseEntity.ok(converterParaDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        clienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
