package com.example.careplus.controller.dtoConsulta;


import java.time.LocalDateTime;

public class ConsultaRequestDto {
    private Long pacienteId;
    private Long funcionarioId;
    private LocalDateTime dataHora;
    private String tipo;

    public ConsultaRequestDto(Long pacienteId, Long funcionarioId, LocalDateTime dataHora, String tipo) {
        this.pacienteId = pacienteId;
        this.funcionarioId = funcionarioId;
        this.dataHora = dataHora;
        this.tipo = tipo;
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


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
