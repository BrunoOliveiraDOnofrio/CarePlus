package com.example.careplus.dto.dtoPaciente;

import com.example.careplus.dto.dtoEndereco.EnderecoRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class PacienteRequestDto {

    @Schema(description = "Vitor Miguel Raimundo Ribeiro")
    private String nome;

    @Schema(description = "vitor_ribeiro@performa.com.br")
    private String email;

    @Schema(description = "191.644.388-56")
    private String cpf;

    @Schema(description = "(11) 99182-8249")
    private String telefone;

    @Schema(description = "2025-10-14")
    private LocalDate dtNascimento;

    @Schema(description = "Sul America")
    private String convenio;

    @Schema(description = "Endereço do paciente")
    private EnderecoRequestDto endereco;

    public PacienteRequestDto() {
    }

    public PacienteRequestDto(String nome, String email, String cpf, String telefone, LocalDate dtNascimento, String convenio, EnderecoRequestDto endereco) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.dtNascimento = dtNascimento;
        this.convenio = convenio;
        this.endereco = endereco;
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

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public EnderecoRequestDto getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoRequestDto endereco) {
        this.endereco = endereco;
    }
}

