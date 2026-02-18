package com.example.careplus.dto.dtoPaciente;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @Schema(description = "(11) 98215-0272")
    private String telefone;
    @Schema(description = "2025-10-14")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dtNascimento;
    @Schema(description = "Sul America")
    private String convenio;
    @Schema(description = "2025-11-25")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;
    public PacienteResponseDto() {
    }
    public PacienteResponseDto(Long id, String nome, String email, String cpf, String telefone, LocalDate dtNascimento, String convenio, LocalDate dataInicio) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.dtNascimento = dtNascimento;
        this.convenio = convenio;
        this.dataInicio = dataInicio;
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
    public LocalDate getDataInicio() {
        return dataInicio;
    }
    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }
}