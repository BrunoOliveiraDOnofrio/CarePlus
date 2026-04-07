package com.example.careplus.dto.dtoConsultaProntuario;

import com.example.careplus.dto.dtoFuncionario.FuncionarioResponseDto;
import com.example.careplus.dto.dtoPaciente.PacienteResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

public class ConsultaProntuarioResponseDto {
    private Long id;
    private PacienteResponseDto paciente;
    private List<FuncionarioResponseDto> funcionarios;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHora;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tipo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String observacoesComportamentais;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean presenca;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean confirmada;

    public ConsultaProntuarioResponseDto() {
    }

    public ConsultaProntuarioResponseDto(Long id, PacienteResponseDto paciente, List<FuncionarioResponseDto> funcionarios, LocalDateTime dataHora, String tipo, String observacoesComportamentais, Boolean presenca, Boolean confirmada) {
        this.id = id;
        this.paciente = paciente;
        this.funcionarios = funcionarios;
        this.dataHora = dataHora;
        this.tipo = tipo;
        this.observacoesComportamentais = observacoesComportamentais;
        this.presenca = presenca;
        this.confirmada = confirmada;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PacienteResponseDto getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteResponseDto paciente) {
        this.paciente = paciente;
    }

    public List<FuncionarioResponseDto> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<FuncionarioResponseDto> funcionarios) {
        this.funcionarios = funcionarios;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getObservacoesComportamentais() {
        return observacoesComportamentais;
    }

    public void setObservacoesComportamentais(String observacoesComportamentais) {
        this.observacoesComportamentais = observacoesComportamentais;
    }

    public Boolean getPresenca() {
        return presenca;
    }

    public void setPresenca(Boolean presenca) {
        this.presenca = presenca;
    }

    public Boolean getConfirmada() {
        return confirmada;
    }

    public void setConfirmada(Boolean confirmada) {
        this.confirmada = confirmada;
    }
}

