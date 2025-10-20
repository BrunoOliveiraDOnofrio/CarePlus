package com.example.careplus.controller.dtoConsulta;

import com.example.careplus.model.Especialista;
import com.example.careplus.model.Paciente;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

public class ConsultaRequestDto {
    private Long pacienteId;
    private Long especialistaId;
    private LocalDateTime dataHora;

    public ConsultaRequestDto(Long presenca, Long especialistaId, LocalDateTime dataHora) {
        this.pacienteId = presenca;
        this.especialistaId = especialistaId;
        this.dataHora = dataHora;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public Long getEspecialistaId() {
        return especialistaId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }
}
