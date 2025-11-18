package com.example.careplus.controller.dtoProntuario;

import io.swagger.v3.oas.annotations.media.Schema;

public class ProntuarioRequestDto {

    @Schema(description = "1")
    private Long idPaciente;

    @Schema(description = "Sim")
    private Boolean desfraldado;

    @Schema(description = "Dinossauro")
    private String hiperfoco;

    @Schema(description = "Anotação da conversa com os pais")
    private String anamnese;

    @Schema(description = "Autismo nível 3")
    private String diagnostico;

    @Schema(description = "O paciente vem evoluindo bem")
    private String resumoClinico;

    @Schema(description = "2")
    private Integer nivelAgressividade;

    public ProntuarioRequestDto(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public ProntuarioRequestDto(Long idPaciente, Boolean desfraldado, String hiperfoco, String anamnese, String diagnostico, String resumoClinico, Integer nivelAgressividade) {
        this.idPaciente = idPaciente;
        this.desfraldado = desfraldado;
        this.hiperfoco = hiperfoco;
        this.anamnese = anamnese;
        this.diagnostico = diagnostico;
        this.resumoClinico = resumoClinico;
        this.nivelAgressividade = nivelAgressividade;
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Boolean getDesfraldado() {
        return desfraldado;
    }

    public void setDesfraldado(Boolean desfraldado) {
        this.desfraldado = desfraldado;
    }

    public String getHiperfoco() {
        return hiperfoco;
    }

    public void setHiperfoco(String hiperfoco) {
        this.hiperfoco = hiperfoco;
    }

    public String getAnamnese() {
        return anamnese;
    }

    public void setAnamnese(String anamnese) {
        this.anamnese = anamnese;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getResumoClinico() {
        return resumoClinico;
    }

    public void setResumoClinico(String resumoClinico) {
        this.resumoClinico = resumoClinico;
    }

    public Integer getNivelAgressividade() {
        return nivelAgressividade;
    }

    public void setNivelAgressividade(Integer nivelAgressividade) {
        this.nivelAgressividade = nivelAgressividade;
    }
}
