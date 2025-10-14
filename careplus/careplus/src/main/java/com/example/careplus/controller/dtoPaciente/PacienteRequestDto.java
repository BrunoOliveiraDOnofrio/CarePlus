package com.example.careplus.controller.dtoPaciente;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class PacienteRequestDto {

    @Schema(description = "Vitor Miguel Raimundo Ribeiro")
    private String nome;

    @Schema(description = "vitor_ribeiro@performa.com.br")
    private String email;

    @Schema(description = "191.644.388-56")
    private String cpf;

    @Schema(description = "Estagirario")
    private String cargo;

    @Schema(description = "(11) 99182-8249")
    private String telefone;

    @Schema(description = "qhcPkyyKB1")
    private String senha;

    @Schema(description = "2025-10-14")
    private LocalDate dtNascimento;

    public PacienteRequestDto(String nome, String email, String cpf, String cargo, String telefone, String senha, LocalDate dtNascimento) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.cargo = cargo;
        this.telefone = telefone;
        this.senha = senha;
        this.dtNascimento = dtNascimento;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDate getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(LocalDate dtNascimento) {
        this.dtNascimento = dtNascimento;
    }
}
