package com.example.careplus.controller.dtoEspecialista;


public class EspecialistaResponseDto {
    private Long id;
    private String nome;
    private String email;
    private SupervisorDto supervisor;
    private String cargo;
    private String especialidade;

    public EspecialistaResponseDto(Long id, String nome, String email, SupervisorDto supervisor, String cargo, String especialidade) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.supervisor = supervisor;
        this.cargo = cargo;
        this.especialidade = especialidade;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SupervisorDto getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(SupervisorDto supervisor) {
        this.supervisor = supervisor;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }
}
