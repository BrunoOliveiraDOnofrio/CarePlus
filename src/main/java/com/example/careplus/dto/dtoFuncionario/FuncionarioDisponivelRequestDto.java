package com.example.careplus.dto.dtoFuncionario;

import io.swagger.v3.oas.annotations.media.Schema;

public class FuncionarioDisponivelRequestDto {

    @Schema(description = "Cardiologia", example = "Cardiologia")
    private String especialidade;

    @Schema(description = "2026-01-30 16:00:00", example = "2026-01-30 16:00:00")
    private String dataHora;

    public FuncionarioDisponivelRequestDto() {
    }

    public FuncionarioDisponivelRequestDto(String especialidade, String dataHora) {
        this.especialidade = especialidade;
        this.dataHora = dataHora;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }
}

