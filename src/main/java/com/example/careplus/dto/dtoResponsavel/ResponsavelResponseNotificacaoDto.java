package com.example.careplus.dto.dtoResponsavel;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class ResponsavelResponseNotificacaoDto {

    @Schema(description = "Ana Josefa")
    private String nome;
    @Schema(description = "vitor_ribeiro@performa.com.br")
    private String email;
    @Schema(description = "(11) 98559-3381")
    private String telefone;

    public ResponsavelResponseNotificacaoDto(String nome, String email, String telefone) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public ResponsavelResponseNotificacaoDto() {
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
}
