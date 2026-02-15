package com.example.careplus.dto.dtoConsultaProntuario;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class ConsultaProntuarioRequest {

    private Long pacienteId;
    private Long funcionarioId;

    @Schema(description = "2025-10-14 14:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHora;

    public ConsultaProntuarioRequest() {
    }

    public ConsultaProntuarioRequest(Long pacienteId, Long funcionarioId, LocalDateTime dataHora) {
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

