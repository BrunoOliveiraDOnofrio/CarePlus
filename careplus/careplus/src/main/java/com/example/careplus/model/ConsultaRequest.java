package com.example.careplus.model;

import java.time.LocalDateTime;

// o front deve passar apenas esses parâmetros
public class ConsultaRequest {
    private Long pacienteId;
    private Long especialistaId;
    private LocalDateTime dataHora;

    public ConsultaRequest() {
    }

    public ConsultaRequest(Long pacienteId, Long especialistaId, LocalDateTime dataHora) {
        this.pacienteId = pacienteId;
        this.especialistaId = especialistaId;
        this.dataHora = dataHora;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Long getEspecialistaId() {
        return especialistaId;
    }

    public void setEspecialistaId(Long especialistaId) {
        this.especialistaId = especialistaId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
