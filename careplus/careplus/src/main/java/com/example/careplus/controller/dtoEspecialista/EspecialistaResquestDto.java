package com.example.careplus.controller.dtoEspecialista;


public class EspecialistaResquestDto {
    private String nome;
    private String email;
    private String senha;
    private SupervisorDto supervisor;
    private String cargo;
    private String especialidade;

    public EspecialistaResquestDto(String nome, String email, String senha, SupervisorDto supervisor, String cargo, String especialidade) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.supervisor = supervisor;
        this.cargo = cargo;
        this.especialidade = especialidade;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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
