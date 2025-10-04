package com.example.careplus.controller.dtoResponsavel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class ResponsavelResponseDto {

    private String nome;
    private String email;
    private String telefone;
    private LocalDate dtNascimento;
    @JsonIgnore // tira do json para restar apenas o mascarado
    private String cpf;
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
