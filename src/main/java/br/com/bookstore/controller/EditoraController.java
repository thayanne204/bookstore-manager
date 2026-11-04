
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

import br.com.bookstore.entity.Editora;
import br.com.bookstore.exception.ResourceNotFoundException;
import br.com.bookstore.repository.EditoraRepository;

@RestController
@RequestMapping("/api/editoras")
public class EditoraController {

    private final EditoraRepository editoraRepository;

    public EditoraController(EditoraRepository editoraRepository) {
        this.editoraRepository = editoraRepository;
    }

    @PostMapping
    public ResponseEntity<Editora> cadastrar(@RequestBody Editora editora) {
        Editora salva = editoraRepository.save(editora);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @GetMapping
    public ResponseEntity<List<Editora>> listarTodas() {
        return ResponseEntity.ok(editoraRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Editora> buscarPorId(@PathVariable Long id) {
        Editora editora = editoraRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Editora não encontrada."));
        return ResponseEntity.ok(editora);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Editora> atualizar(
            @PathVariable Long id,
            @RequestBody Editora dados) {

        Editora editora = editoraRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Editora não encontrada."));

        editora.setNome(dados.getNome());

        return ResponseEntity.ok(editoraRepository.save(editora));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        Editora editora = editoraRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Editora não encontrada."));

        editoraRepository.delete(editora);
        return ResponseEntity.noContent().build();
    }
}
