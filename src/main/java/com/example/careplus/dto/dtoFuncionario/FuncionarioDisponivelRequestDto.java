package com.example.careplus.dto.dtoFuncionario;

import io.swagger.v3.oas.annotations.media.Schema;

public class FuncionarioDisponivelRequestDto {

    @Schema(description = "Cardiologia", example = "Cardiologia")
    private String especialidade;

    @Schema(description = "2026-01-30", example = "2026-01-30")
    private String data;

    @Schema(description = "16:00:00", example = "16:00:00")
    private String horarioInicio;

    public FuncionarioDisponivelRequestDto() {}

    public FuncionarioDisponivelRequestDto(String especialidade, String data, String horarioInicio) {
        this.especialidade = especialidade;
        this.data = data;
        this.horarioInicio = horarioInicio;
    }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(String horarioInicio) { this.horarioInicio = horarioInicio; }
}
