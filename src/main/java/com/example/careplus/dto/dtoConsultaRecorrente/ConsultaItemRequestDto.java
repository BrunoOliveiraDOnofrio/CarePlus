package com.example.careplus.dto.dtoConsultaRecorrente;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Representa um bloco de consultas a serem agendadas.
 * Pode ter um funcionário único (funcionarioId) ou múltiplos (funcionarioIds).
 * As datas são geradas semanalmente entre dataInicio e dataFim.
 * Para uma consulta única, basta que dataInicio == dataFim.
 */
public class ConsultaItemRequestDto {

    @Schema(description = "ID de um único funcionário (use este ou funcionarioIds)")
    private Long funcionarioId;

    @Schema(description = "Lista de IDs de funcionários (use este ou funcionarioId)")
    private List<Long> funcionarioIds;

    @Schema(description = "Horário de início", example = "10:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horarioInicio;

    @Schema(description = "Horário de fim", example = "11:00:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horarioFim;

    @Schema(description = "Data de início da recorrência (ou data única)", example = "07/04/2026")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataInicio;

    @Schema(description = "Data de fim da recorrência (igual a dataInicio para consulta única)", example = "07/04/2027")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFim;

    @Schema(description = "Tipo da consulta", example = "Rotina")
    private String tipo;

    public ConsultaItemRequestDto() {}

    // ---------- helpers ----------

    /**
     * Retorna a lista normalizada de IDs de funcionários,
     * unificando funcionarioId e funcionarioIds.
     */
    public List<Long> getFuncionarioIdsList() {
        if (funcionarioIds != null && !funcionarioIds.isEmpty()) {
            return funcionarioIds;
        }
        if (funcionarioId != null) {
            return List.of(funcionarioId);
        }
        return List.of();
    }

    // ---------- getters / setters ----------

    public Long getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(Long funcionarioId) { this.funcionarioId = funcionarioId; }

    public List<Long> getFuncionarioIds() { return funcionarioIds; }
    public void setFuncionarioIds(List<Long> funcionarioIds) { this.funcionarioIds = funcionarioIds; }

    public LocalTime getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(LocalTime horarioInicio) { this.horarioInicio = horarioInicio; }

    public LocalTime getHorarioFim() { return horarioFim; }
    public void setHorarioFim(LocalTime horarioFim) { this.horarioFim = horarioFim; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}

