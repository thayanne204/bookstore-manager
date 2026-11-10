
package br.com.bookstore.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bookstore.entity.Cliente;
import br.com.bookstore.exception.BusinessException;
import br.com.bookstore.exception.ResourceNotFoundException;
import br.com.bookstore.repository.ClienteRepository;
import br.com.bookstore.repository.VendaRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    private final VendaRepository vendaRepository;

    public ClienteService(
            ClienteRepository clienteRepository,
            VendaRepository vendaRepository) {
        this.clienteRepository = clienteRepository;
        this.vendaRepository = vendaRepository;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente não encontrado."));
    }

    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional
    public Cliente cadastrar(Cliente cliente) {

        validarCliente(cliente);

        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new BusinessException(
                    "Já existe um cliente cadastrado com esse CPF."
            );
        }

        if (cliente.getEmail() != null
                && !cliente.getEmail().isBlank()
                && clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new BusinessException(
                    "Já existe um cliente cadastrado com esse e-mail."
            );
        }

        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente atualizar(Long id, Cliente dados) {

        Cliente cliente = buscarPorId(id);

        validarCliente(dados);

        if (!cliente.getCpf().equals(dados.getCpf())
                && clienteRepository.existsByCpf(dados.getCpf())) {
            throw new BusinessException(
                    "Esse CPF já está cadastrado."
            );
        }

        if (dados.getEmail() != null
                && !dados.getEmail().isBlank()
                && !dados.getEmail().equalsIgnoreCase(cliente.getEmail())
                && clienteRepository.existsByEmail(dados.getEmail())) {
            throw new BusinessException(
                    "Esse e-mail já está cadastrado."
            );
        }

        cliente.setNome(dados.getNome());
        cliente.setCpf(dados.getCpf());
        cliente.setEmail(dados.getEmail());
        cliente.setTelefone(dados.getTelefone());
        cliente.setDataNascimento(dados.getDataNascimento());
        cliente.setEndereco(dados.getEndereco());
        cliente.setCidade(dados.getCidade());
        cliente.setEstado(dados.getEstado());
        cliente.setAtivo(dados.getAtivo());

        return clienteRepository.save(cliente);
    }

    @Transactional
    public void excluir(Long id) {
        Cliente cliente = buscarPorId(id);

        if (vendaRepository.existsByClienteId(id)) {
            throw new BusinessException(
                    "Não é possível excluir o cliente porque ele possui vendas registradas. Inative o cadastro.");
        }

        clienteRepository.delete(cliente);
    }

    private void validarCliente(Cliente cliente) {

        if (cliente.getNome() == null
                || cliente.getNome().isBlank()) {
            throw new BusinessException("O nome é obrigatório.");
        }

        if (cliente.getCpf() == null
                || cliente.getCpf().isBlank()) {
            throw new BusinessException("O CPF é obrigatório.");
        }
    }
}
