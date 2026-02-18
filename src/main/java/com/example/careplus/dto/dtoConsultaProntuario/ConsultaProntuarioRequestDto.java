package com.example.careplus.dto.dtoConsultaProntuario;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class ConsultaProntuarioRequestDto {
    private Long pacienteId;
    private Long funcionarioId;

    @Schema(description = "Data e hora da consulta no formato yyyy-MM-dd HH:mm:ss", example = "2026-01-20 14:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHora;
    private String tipo;

    public ConsultaProntuarioRequestDto() {
    }

    public ConsultaProntuarioRequestDto(Long pacienteId, Long funcionarioId, LocalDateTime dataHora, String tipo) {
        this.pacienteId = pacienteId;
        this.funcionarioId = funcionarioId;
        this.dataHora = dataHora;
        this.tipo = tipo;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}

