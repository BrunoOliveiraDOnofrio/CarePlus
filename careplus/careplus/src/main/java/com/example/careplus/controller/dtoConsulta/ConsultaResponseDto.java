package com.example.careplus.controller.dtoConsulta;

import com.example.careplus.controller.dtoFuncionario.FuncionarioResponseDto;
import com.example.careplus.controller.dtoPaciente.PacienteResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

public class ConsultaResponseDto {
    private Long id;
    private PacienteResponseDto paciente;
    private FuncionarioResponseDto funcionario;
    private LocalDateTime dataHora;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tipo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String observacoesComportamentais;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean presenca;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean confirmada;

    public ConsultaResponseDto() {
    }

    public ConsultaResponseDto(Long id, PacienteResponseDto paciente, FuncionarioResponseDto funcionario, LocalDateTime dataHora, String tipo, String observacoesComportamentais, Boolean presenca, Boolean confirmada) {
        this.id = id;
        this.paciente = paciente;
        this.funcionario = funcionario;
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

    public FuncionarioResponseDto getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioResponseDto funcionario) {
        this.funcionario = funcionario;
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
