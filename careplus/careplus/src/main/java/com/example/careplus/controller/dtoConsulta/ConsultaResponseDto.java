package com.example.careplus.controller.dtoConsulta;

import com.example.careplus.controller.dtoFuncionario.FuncionarioResponseDto;
import com.example.careplus.controller.dtoFuncionario.FuncionarioResquestDto;
import com.example.careplus.controller.dtoPaciente.PacienteResponseDto;
import com.example.careplus.model.Funcionario;
import com.example.careplus.model.Paciente;
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
    private String anotacoes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean presenca;

    public ConsultaResponseDto() {
    }

    public ConsultaResponseDto(Long id, PacienteResponseDto paciente, FuncionarioResponseDto funcionario, LocalDateTime dataHora, String tipo, String anotacoes, Boolean presenca) {
        this.id = id;
        this.paciente = paciente;
        this.funcionario = funcionario;
        this.dataHora = dataHora;
        this.tipo = tipo;
        this.anotacoes = anotacoes;
        this.presenca = presenca;
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

    public String getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(String anotacoes) {
        this.anotacoes = anotacoes;
    }

    public Boolean getPresenca() {
        return presenca;
    }

    public void setPresenca(Boolean presenca) {
        this.presenca = presenca;
    }
}
