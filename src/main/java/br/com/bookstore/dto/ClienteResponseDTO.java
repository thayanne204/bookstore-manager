
package br.com.bookstore.dto;

import java.time.LocalDate;

public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private String endereco;
    private String cidade;
    private String estado;
    private Boolean ativo;

    public ClienteResponseDTO() {
    }

    public ClienteResponseDTO(
            Long id,
            String nome,
            String cpf,
            String email,
            String telefone,
            LocalDate dataNascimento,
            String endereco,
            String cidade,
            String estado,
            Boolean ativo) {

        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.ativo = ativo;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getEndereco() { return endereco; }
    public String getCidade() { return cidade; }
    public String getEstado() { return estado; }
    public Boolean getAtivo() { return ativo; }
}
