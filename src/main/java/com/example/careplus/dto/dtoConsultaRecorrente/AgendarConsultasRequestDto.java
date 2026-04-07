package com.example.careplus.dto.dtoConsultaRecorrente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * DTO principal para o endpoint POST /recorrentes.
 * Permite agendar múltiplos blocos de consultas (recorrentes ou únicas) de uma só vez.
 */
public class AgendarConsultasRequestDto {

    @NotNull(message = "O ID do paciente é obrigatório")
    private Long pacienteId;

    @NotEmpty(message = "A lista de consultas não pode ser vazia")
    @Valid
    private List<ConsultaItemRequestDto> consultas;

    public AgendarConsultasRequestDto() {}

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public List<ConsultaItemRequestDto> getConsultas() { return consultas; }
    public void setConsultas(List<ConsultaItemRequestDto> consultas) { this.consultas = consultas; }
}

