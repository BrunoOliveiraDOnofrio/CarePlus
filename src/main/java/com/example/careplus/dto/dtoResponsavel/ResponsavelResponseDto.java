package com.example.careplus.dto.dtoResponsavel;

import com.example.careplus.dto.dtoEndereco.EnderecoResponseDto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class ResponsavelResponseDto {
    private Long id;
    @Schema(description = "Ana Josefa")
    private String nome;
    @Schema(description = "vitor_ribeiro@performa.com.br")
    private String email;
    @Schema(description = "(11) 98559-3381")
    private String telefone;
    @Schema(description = "2025-10-14")
    private LocalDate dtNascimento;
    private String cpf;
    @Schema(description = "Endereco do responsavel")
    private EnderecoResponseDto endereco;

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public EnderecoResponseDto getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoResponseDto endereco) {
        this.endereco = endereco;
    }
}
