
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

import br.com.bookstore.dto.CategoriaRequestDTO;
import br.com.bookstore.dto.CategoriaResponseDTO;
import br.com.bookstore.entity.Categoria;
import br.com.bookstore.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Categorias", description = "Cadastro e consulta de categorias de livros")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Dados enviados inválidos"),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflito com regra de negócio")
})
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    private CategoriaResponseDTO converterParaDTO(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getAtivo()
        );
    }

    private Categoria converterParaEntidade(CategoriaRequestDTO dto) {
        Categoria categoria = new Categoria();
        preencherCategoria(categoria, dto);
        return categoria;
    }

    private void preencherCategoria(Categoria categoria, CategoriaRequestDTO dto) {
        categoria.setNome(dto.getNome());
        categoria.setDescricao(dto.getDescricao());
        categoria.setAtivo(dto.getAtivo());
    }

    @Operation(summary = "Cadastrar novo registro")
    @ApiResponse(responseCode = "201", description = "Criado com sucesso")
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> cadastrar(
            @Valid @RequestBody CategoriaRequestDTO dto) {

        Categoria categoria = converterParaEntidade(dto);
        Categoria salva = categoriaService.cadastrar(categoria);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(converterParaDTO(salva));
    }

    @Operation(summary = "Listar todos os registros")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarTodas() {

        List<CategoriaResponseDTO> categorias = categoriaService.listarTodas()
                .stream()
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(categorias);
    }

    @Operation(summary = "Buscar registro por ID")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(
            @PathVariable Long id) {

        Categoria categoria = categoriaService.buscarPorId(id);

        return ResponseEntity.ok(converterParaDTO(categoria));
    }

    @Operation(summary = "Atualizar registro existente")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequestDTO dto) {

        Categoria dados = converterParaEntidade(dto);
        Categoria atualizada = categoriaService.atualizar(id, dados);

        return ResponseEntity.ok(converterParaDTO(atualizada));
    }

    @Operation(summary = "Excluir registro (respeitando as regras de negócio)")
    @ApiResponse(responseCode = "204", description = "Excluído com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        categoriaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
