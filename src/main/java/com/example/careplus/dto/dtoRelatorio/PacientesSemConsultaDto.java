package com.example.careplus.dto.dtoRelatorio;

import io.swagger.v3.oas.annotations.media.Schema;

public class PacientesSemConsultaDto {

    @Schema(description = "ID único do paciente")
    private Long id;

    @Schema(description = "Nome completo do paciente")
    private String nome;

    @Schema(description = "Email do paciente")
    private String email;

    @Schema(description = "Convenio/Seguradora do paciente")
    private String convenio;

    public PacientesSemConsultaDto() {
    }

    public PacientesSemConsultaDto(Long id, String nome, String email, String convenio) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.convenio = convenio;
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

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }
}

