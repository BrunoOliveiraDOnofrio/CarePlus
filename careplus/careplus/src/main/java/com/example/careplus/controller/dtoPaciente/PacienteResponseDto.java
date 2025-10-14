package com.example.careplus.controller.dtoPaciente;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class PacienteResponseDto {

    private Long id;

    @Schema(description = "Diogo Francisco dos Santos")
    private String nome;

    @Schema(description = "iago_benedito_barbosa@navescorat.com.br")
    private String email;


    @Schema(description = "484.356.058-84")
    private String cpf;

    @Schema(description = "Estudante")
    private String cargo;

    @Schema(description = "(11) 98215-0272")
    private String telefone;

    @Schema(description = "2025-10-14")
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
