package com.example.careplus.dto.dtoConsultaProntuario;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConsultaProntuarioRequestDto {
    private Long pacienteId;
    private Long funcionarioId;

    @Schema(description = "Data da consulta", example = "2026-01-20")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;

    @Schema(description = "Horário de início", example = "14:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horarioInicio;

    @Schema(description = "Horário de fim", example = "15:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horarioFim;

    private String tipo;

    public ConsultaProntuarioRequestDto() {}

    public ConsultaProntuarioRequestDto(Long pacienteId, Long funcionarioId, LocalDate data, LocalTime horarioInicio, LocalTime horarioFim, String tipo) {
        this.pacienteId = pacienteId;
        this.funcionarioId = funcionarioId;
        this.data = data;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.tipo = tipo;
    }

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public Long getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(Long funcionarioId) { this.funcionarioId = funcionarioId; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(LocalTime horarioInicio) { this.horarioInicio = horarioInicio; }

    public LocalTime getHorarioFim() { return horarioFim; }
    public void setHorarioFim(LocalTime horarioFim) { this.horarioFim = horarioFim; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
