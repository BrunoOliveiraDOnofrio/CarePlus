package com.example.careplus.dto.dtoRelatorio;

import io.swagger.v3.oas.annotations.media.Schema;

public class FuncionariosPorEspecialidadeDto {

    @Schema(description = "Especialidade/Setor do funcionário")
    private String setor;

    @Schema(description = "Total de funcionários ativos neste setor")
    private Long totalFuncionarios;

    @Schema(description = "Total de pacientes atendidos neste setor")
    private Long totalPacientes;

    public FuncionariosPorEspecialidadeDto() {
    }

    public FuncionariosPorEspecialidadeDto(String setor, Long totalFuncionarios, Long totalPacientes) {
        this.setor = setor;
        this.totalFuncionarios = totalFuncionarios;
        this.totalPacientes = totalPacientes;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public Long getTotalFuncionarios() {
        return totalFuncionarios;
    }

    public void setTotalFuncionarios(Long totalFuncionarios) {
        this.totalFuncionarios = totalFuncionarios;
    }

    public Long getTotalPacientes() {
        return totalPacientes;
    }

    public void setTotalPacientes(Long totalPacientes) {
        this.totalPacientes = totalPacientes;
    }
}

