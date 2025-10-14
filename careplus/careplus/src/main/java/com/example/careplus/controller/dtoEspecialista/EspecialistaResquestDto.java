package com.example.careplus.controller.dtoEspecialista;


import io.swagger.v3.oas.annotations.media.Schema;

public class EspecialistaResquestDto {

    @Schema(description = "Iago Benedito Barbosa")
    private String nome;

    @Schema(description = "iago_benedito_barbosa@navescorat.com.br")
    private String email;

    @Schema(description = "olgyT0E7nH")
    private String senha;

    private SupervisorDto supervisor;

    @Schema(description = "Estagiaria")
    private String cargo;

    @Schema(description = "Fonoaudiologa")
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
