
package br.com.bookstore.controller;

import java.math.BigDecimal;
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

import br.com.bookstore.dto.LivroRequestDTO;
import br.com.bookstore.dto.LivroResponseDTO;
import br.com.bookstore.entity.Categoria;
import br.com.bookstore.entity.Editora;
import br.com.bookstore.entity.Livro;
import br.com.bookstore.exception.ResourceNotFoundException;
import br.com.bookstore.repository.CategoriaRepository;
import br.com.bookstore.repository.EditoraRepository;
import br.com.bookstore.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Livros", description = "Gerenciamento de livros e estoque")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Dados enviados inválidos"),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflito com regra de negócio")
})
@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService livroService;
    private final CategoriaRepository categoriaRepository;
    private final EditoraRepository editoraRepository;

    public LivroController(
            LivroService livroService,
            CategoriaRepository categoriaRepository,
            EditoraRepository editoraRepository) {
        this.livroService = livroService;
        this.categoriaRepository = categoriaRepository;
        this.editoraRepository = editoraRepository;
    }

    private LivroResponseDTO converterParaDTO(Livro livro) {
        return new LivroResponseDTO(
                livro.getId(),
                livro.getTitulo(),
                livro.getIsbn(),
                livro.getDescricao(),
                livro.getDataPublicacao(),
                livro.getPreco(),
                livro.getQuantidadeEstoque(),
                livro.getEstoqueMinimo(),
                livro.getCategoria().getNome(),
                livro.getEditora().getNome(),
                livro.getAtivo()
        );
    }

    private Livro converterParaEntidade(LivroRequestDTO dto) {
        Livro livro = new Livro();
        preencherLivro(livro, dto);
        return livro;
    }

    private void preencherLivro(Livro livro, LivroRequestDTO dto) {

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoria não encontrada."));

        Editora editora = editoraRepository.findById(dto.getEditoraId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Editora não encontrada."));

        livro.setTitulo(dto.getTitulo());
        livro.setIsbn(dto.getIsbn());
        livro.setDescricao(dto.getDescricao());
        livro.setDataPublicacao(dto.getDataPublicacao());
        livro.setPreco(dto.getPreco());
        livro.setQuantidadeEstoque(dto.getQuantidadeEstoque());
        livro.setEstoqueMinimo(dto.getEstoqueMinimo());
        livro.setCategoria(categoria);
        livro.setEditora(editora);
        livro.setAtivo(dto.getAtivo() == null ? Boolean.TRUE : dto.getAtivo());
    }

    @Operation(summary = "Cadastrar novo registro")
    @ApiResponse(responseCode = "201", description = "Criado com sucesso")
    @PostMapping
    public ResponseEntity<LivroResponseDTO> cadastrar(
            @Valid @RequestBody LivroRequestDTO dto) {

        Livro livro = converterParaEntidade(dto);
        Livro salvo = livroService.cadastrar(livro);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(converterParaDTO(salvo));
    }

    @Operation(summary = "Listar todos os registros")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listarTodos() {

        List<LivroResponseDTO> livros = livroService.listarTodos()
                .stream()
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(livros);
    }

    @Operation(summary = "Buscar registro por ID")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscarPorId(
            @PathVariable Long id) {

        Livro livro = livroService.buscarPorId(id);

        return ResponseEntity.ok(converterParaDTO(livro));
    }

    @Operation(summary = "Buscar livros pelo título (parcial, sem diferenciar maiúsculas)")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping("/buscar")
    public ResponseEntity<List<LivroResponseDTO>> buscarPorTitulo(
            @RequestParam String titulo) {

        List<LivroResponseDTO> livros = livroService.buscarPorTitulo(titulo)
                .stream()
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(livros);
    }

    @Operation(summary = "Filtrar livros por categoria e/ou faixa de preço")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping("/filtrar")
    public ResponseEntity<List<LivroResponseDTO>> filtrar(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) BigDecimal precoMin,
            @RequestParam(required = false) BigDecimal precoMax) {

        List<LivroResponseDTO> livros = livroService
                .filtrar(categoriaId, precoMin, precoMax)
                .stream()
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(livros);
    }

    @Operation(summary = "Listar apenas livros ativos")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping("/ativos")
    public ResponseEntity<List<LivroResponseDTO>> listarAtivos() {

        List<LivroResponseDTO> livros = livroService.listarAtivos()
                .stream()
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(livros);
    }

    @Operation(summary = "Listar livros com estoque menor ou igual ao limite informado")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<LivroResponseDTO>> listarEstoqueBaixo(
            @RequestParam(defaultValue = "5") int limite) {

        List<LivroResponseDTO> livros = livroService.listarEstoqueBaixo(limite)
                .stream()
                .map(this::converterParaDTO)
                .toList();

        return ResponseEntity.ok(livros);
    }

    @Operation(summary = "Atualizar registro existente")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LivroRequestDTO dto) {

        Livro livro = livroService.buscarPorId(id);

        preencherLivro(livro, dto);

        Livro atualizado = livroService.atualizar(id, livro);

        return ResponseEntity.ok(converterParaDTO(atualizado));
    }

    @Operation(summary = "Excluir registro (respeitando as regras de negócio)")
    @ApiResponse(responseCode = "204", description = "Excluído com sucesso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        livroService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
