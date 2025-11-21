package com.example.careplus.controller.dtoConsulta;

import com.example.careplus.model.Funcionario;
import com.example.careplus.model.Paciente;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

public class ConsultaRequestDto {
    private Long pacienteId;
    private Long funcionarioId;
    private LocalDateTime dataHora;
    private Boolean confirmada;

    public ConsultaRequestDto(Long pacienteId, Long funcionarioId, LocalDateTime dataHora, Boolean confirmada) {
        this.pacienteId = pacienteId;
        this.funcionarioId = funcionarioId;
        this.dataHora = dataHora;
        this.confirmada = confirmada;
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

    public Boolean getConfirmada() {
        return confirmada;
    }

    public void setConfirmada(Boolean confirmada) {
        this.confirmada = confirmada;
    }
}
