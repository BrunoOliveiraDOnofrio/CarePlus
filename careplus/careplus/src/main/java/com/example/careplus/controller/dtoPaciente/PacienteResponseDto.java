package com.example.careplus.controller.dtoPaciente;

import java.time.LocalDate;

public class PacienteResponseDto {

    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String cargo;
    private String telefone;
    private LocalDate dtNascimento;

    public PacienteResponseDto() {
    }

    public PacienteResponseDto(Long id, String nome, String email, String cpf, String cargo, String telefone, LocalDate dtNascimento) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.cargo = cargo;
        this.telefone = telefone;
        this.dtNascimento = dtNascimento;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(LocalDate dtNascimento) {
        this.dtNascimento = dtNascimento;
    }
}
