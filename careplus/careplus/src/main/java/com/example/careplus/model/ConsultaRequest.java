package com.example.careplus.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

// o front deve passar apenas esses parâmetros
public class ConsultaRequest {

    private Long pacienteId;
    private Long funcionarioId;

    @Schema(description = "2025-10-14")
    private LocalDateTime dataHora;

    public ConsultaRequest() {
    }

    public ConsultaRequest(Long pacienteId, Long funcionarioId, LocalDateTime dataHora) {
        this.pacienteId = pacienteId;
        this.funcionarioId = funcionarioId;
        this.dataHora = dataHora;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
