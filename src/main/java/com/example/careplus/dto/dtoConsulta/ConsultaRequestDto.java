package com.example.careplus.dto.dtoConsulta;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ConsultaRequestDto {
    private Long pacienteId;
    private Long funcionarioId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHora;
    private String tipo;

    public ConsultaRequestDto() {
    }

    public ConsultaRequestDto(Long pacienteId, Long funcionarioId, LocalDateTime dataHora, String tipo) {
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
