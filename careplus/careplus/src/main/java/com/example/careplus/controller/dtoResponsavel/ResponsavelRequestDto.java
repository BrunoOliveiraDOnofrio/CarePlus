package com.example.careplus.controller.dtoResponsavel;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class ResponsavelRequestDto {

    @NotBlank
    private String nome;
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 11, max = 11) @Pattern(regexp = "\\d+")
    private String telefone;
    @NotNull
    private LocalDate dtNascimento;
    @NotBlank @Size(min = 11, max = 11) @Pattern(regexp = "\\d+")
    private String cpf;
    @NotBlank
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

    public String getCpf() {
        return cpf;
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
