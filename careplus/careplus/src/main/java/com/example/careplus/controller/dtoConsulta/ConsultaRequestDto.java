package com.example.careplus.controller.dtoConsulta;

import com.example.careplus.model.Funcionario;
import com.example.careplus.model.Paciente;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

public class ConsultaRequestDto {
    private Long pacienteId;
    private Long funcionarioId;
    private LocalDateTime dataHora;

    public ConsultaRequestDto(Long presenca, Long funcionarioId, LocalDateTime dataHora) {
        this.pacienteId = presenca;
        this.funcionarioId = funcionarioId;
        this.dataHora = dataHora;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }
}
