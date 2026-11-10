package br.com.bookstore.dto;

public class EditoraResponseDTO {

    private Long id;
    private String nome;
    private String cnpj;
    private String email;
    private String telefone;
    private String endereco;

    public EditoraResponseDTO() {
    }

    public EditoraResponseDTO(
            Long id,
            String nome,
            String cnpj,
            String email,
            String telefone,
            String endereco) {

        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCnpj() { return cnpj; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getEndereco() { return endereco; }
}
