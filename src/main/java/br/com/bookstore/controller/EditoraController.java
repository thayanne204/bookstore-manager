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
import org.springframework.web.bind.annotation.RestController;

import br.com.bookstore.dto.EditoraRequestDTO;
import br.com.bookstore.dto.EditoraResponseDTO;
import br.com.bookstore.entity.Editora;
import br.com.bookstore.service.EditoraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Editoras", description = "Cadastro e consulta de editoras")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Dados enviados inválidos"),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflito com regra de negócio")
})
@RestController
@RequestMapping("/api/editoras")
public class EditoraController {

    private final EditoraService editoraService;

    public EditoraController(EditoraService editoraService) {
        this.editoraService = editoraService;
    }

    private EditoraResponseDTO converterParaDTO(Editora editora) {
        return new EditoraResponseDTO(
                editora.getId(),
                editora.getNome(),
                editora.getCnpj(),
                editora.getEmail(),
                editora.getTelefone(),
                editora.getEndereco()
        );
    }

    private Editora converterParaEntidade(EditoraRequestDTO dto) {
        Editora editora = new Editora();
        editora.setNome(dto.getNome());
        editora.setCnpj(dto.getCnpj());
        editora.setEmail(dto.getEmail());
        editora.setTelefone(dto.getTelefone());
        editora.setEndereco(dto.getEndereco());
        return editora;
    }

    @Operation(summary = "Cadastrar novo registro")
    @ApiResponse(responseCode = "201", description = "Criado com sucesso")
    @PostMapping
    public ResponseEntity<EditoraResponseDTO> cadastrar(
            @Valid @RequestBody EditoraRequestDTO dto) {

        Editora salva = editoraService.cadastrar(converterParaEntidade(dto));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(converterParaDTO(salva));
    }

    @Operation(summary = "Listar todos os registros")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping
    public ResponseEntity<List<EditoraResponseDTO>> listarTodas() {

        List<EditoraResponseDTO> editoras = editoraService.listarTodas()
                .stream()
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(editoras);
    }

    @Operation(summary = "Buscar registro por ID")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping("/{id}")
    public ResponseEntity<EditoraResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(converterParaDTO(editoraService.buscarPorId(id)));
    }

    @Operation(summary = "Atualizar registro existente")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @PutMapping("/{id}")
    public ResponseEntity<EditoraResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EditoraRequestDTO dto) {

        Editora atualizada = editoraService.atualizar(id, converterParaEntidade(dto));

        return ResponseEntity.ok(converterParaDTO(atualizada));
    }

    @Operation(summary = "Excluir registro (respeitando as regras de negócio)")
    @ApiResponse(responseCode = "204", description = "Excluído com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        editoraService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
