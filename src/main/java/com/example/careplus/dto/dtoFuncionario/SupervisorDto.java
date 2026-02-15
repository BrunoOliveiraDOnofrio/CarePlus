package com.example.careplus.dto.dtoFuncionario;

public class SupervisorDto {
    private Long id;
    private String nome;

    public SupervisorDto(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public SupervisorDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
