package br.com.bookstore.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bookstore.entity.Editora;
import br.com.bookstore.exception.BusinessException;
import br.com.bookstore.exception.ResourceNotFoundException;
import br.com.bookstore.repository.EditoraRepository;
import br.com.bookstore.repository.LivroRepository;

@Service
public class EditoraService {

    private final EditoraRepository editoraRepository;

    private final LivroRepository livroRepository;

    public EditoraService(
            EditoraRepository editoraRepository,
            LivroRepository livroRepository) {
        this.editoraRepository = editoraRepository;
        this.livroRepository = livroRepository;
    }

    public List<Editora> listarTodas() {
        return editoraRepository.findAll();
    }

    public Editora buscarPorId(Long id) {
        return editoraRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Editora não encontrada."));
    }

    @Transactional
    public Editora cadastrar(Editora editora) {
        if (editoraRepository.existsByNome(editora.getNome())) {
            throw new BusinessException("Já existe uma editora com esse nome.");
        }

        return editoraRepository.save(editora);
    }

    @Transactional
    public Editora atualizar(Long id, Editora dados) {
        Editora editora = buscarPorId(id);

        boolean nomeDuplicado = !editora.getNome().equalsIgnoreCase(dados.getNome())
                && editoraRepository.existsByNome(dados.getNome());

        if (nomeDuplicado) {
            throw new BusinessException("Já existe uma editora com esse nome.");
        }

        editora.setNome(dados.getNome());
        editora.setCnpj(dados.getCnpj());
        editora.setEmail(dados.getEmail());
        editora.setTelefone(dados.getTelefone());
        editora.setEndereco(dados.getEndereco());

        return editoraRepository.save(editora);
    }

    @Transactional
    public void excluir(Long id) {
        Editora editora = buscarPorId(id);

        if (livroRepository.existsByEditoraId(id)) {
            throw new BusinessException(
                    "Não é possível excluir a editora porque existem livros vinculados a ela.");
        }

        editoraRepository.delete(editora);
    }
}
