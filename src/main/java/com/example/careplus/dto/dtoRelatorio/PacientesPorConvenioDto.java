package com.example.careplus.dto.dtoRelatorio;

import io.swagger.v3.oas.annotations.media.Schema;

public class PacientesPorConvenioDto {

    @Schema(description = "Nome do convênio/seguradora")
    private String seguradora;

    @Schema(description = "Total de clientes ativos neste convênio")
    private Long totalClientes;

    public PacientesPorConvenioDto() {
    }

    public PacientesPorConvenioDto(String seguradora, Long totalClientes) {
        this.seguradora = seguradora;
        this.totalClientes = totalClientes;
    }

    public String getSeguradora() {
        return seguradora;
    }

    public void setSeguradora(String seguradora) {
        this.seguradora = seguradora;
    }

    public Long getTotalClientes() {
        return totalClientes;
    }

    public void setTotalClientes(Long totalClientes) {
        this.totalClientes = totalClientes;
    }
}

