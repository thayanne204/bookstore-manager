package br.com.bookstore.dto;

public class CategoriaResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private Boolean ativo;

    public CategoriaResponseDTO(Long id, String nome, String descricao, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Boolean getAtivo() {
        return ativo;
    }
}