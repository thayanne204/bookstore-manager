
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
import jakarta.validation.Valid;

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

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> cadastrar(
            @Valid @RequestBody CategoriaRequestDTO dto) {

        Categoria categoria = converterParaEntidade(dto);
        Categoria salva = categoriaService.cadastrar(categoria);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(converterParaDTO(salva));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarTodas() {

        List<CategoriaResponseDTO> categorias = categoriaService.listarTodas()
                .stream()
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(
            @PathVariable Long id) {

        Categoria categoria = categoriaService.buscarPorId(id);

        return ResponseEntity.ok(converterParaDTO(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequestDTO dto) {

        Categoria dados = converterParaEntidade(dto);
        Categoria atualizada = categoriaService.atualizar(id, dados);

        return ResponseEntity.ok(converterParaDTO(atualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        categoriaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
