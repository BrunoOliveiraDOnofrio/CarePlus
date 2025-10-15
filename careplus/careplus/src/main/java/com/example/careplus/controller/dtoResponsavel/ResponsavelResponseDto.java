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
        if(this.telefone == null || this.telefone.length() != 11){
            return null;
        }

        return "(" + telefone.substring(0,2) + ")" + " " + telefone.substring(2,7) + "-" + telefone.substring(7, 11);
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
