package com.example.careplus.dto.dtoConsultaProntuario;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ConsultaProntuarioRequest {

    private Long pacienteId;
    private List<Long> funcionarioIds;

    @Schema(description = "2026-01-20", example = "2026-01-20")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;

    @Schema(description = "14:00:00", example = "14:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horarioInicio;

    @Schema(description = "15:00:00", example = "15:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horarioFim;

    private String tipo;

    public ConsultaProntuarioRequest() {}

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public List<Long> getFuncionarioIds() { return funcionarioIds; }
    public void setFuncionarioIds(List<Long> funcionarioIds) { this.funcionarioIds = funcionarioIds; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(LocalTime horarioInicio) { this.horarioInicio = horarioInicio; }

    public LocalTime getHorarioFim() { return horarioFim; }
    public void setHorarioFim(LocalTime horarioFim) { this.horarioFim = horarioFim; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
