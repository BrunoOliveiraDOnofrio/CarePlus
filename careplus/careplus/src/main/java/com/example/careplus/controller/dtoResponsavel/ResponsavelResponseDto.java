package com.example.careplus.controller.dtoResponsavel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class ResponsavelResponseDto {

    @Schema(description = "Ana Josefa")
    private String nome;

    @Schema(description = "vitor_ribeiro@performa.com.br")
    private String email;

    @Schema(description = "(11) 98559-3381")
    private String telefone;

    @Schema(description = "2025-10-14")
    private LocalDate dtNascimento;

    @JsonIgnore // tira do json para restar apenas o mascarado
    private String cpf;

    @Schema(description = "Sul America")
    private String convenio;


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
    // Forma de proteger o dado do responsável
    @JsonProperty("cpf")
    public String getCpfMascarado() {
        if (cpf == null || cpf.length() != 11){
            return null;
        }
        return cpf.substring(0, 3) + ".***.***-" + cpf.substring(9);
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }
}
