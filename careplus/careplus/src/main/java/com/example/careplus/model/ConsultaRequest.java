package com.example.careplus.model;

import java.time.LocalDateTime;

// o front deve passar apenas esses parâmetros
public class ConsultaRequest {
    private Integer usuarioId;
    private Long especialistaId;
    private LocalDateTime dataHora;

    public ConsultaRequest() {
    }

    public ConsultaRequest(Integer usuarioId, Long especialistaId, LocalDateTime dataHora) {
        this.usuarioId = usuarioId;
        this.especialistaId = especialistaId;
        this.dataHora = dataHora;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
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
