package com.example.careplus.dto.dtoFichaClinica;

public class FichaClinicaResponseDto {

    private Long id;
    private Long idPaciente;
    private Boolean desfraldado;
    private String hiperfoco;
    private String anamnese;
    private String diagnostico;
    private String resumoClinico;
    private Integer nivelAgressividade;

    public FichaClinicaResponseDto() {
    }

    public FichaClinicaResponseDto(Long id, Long idPaciente, Boolean desfraldado, String hiperfoco,
                                   String anamnese, String diagnostico, String resumoClinico,
                                   Integer nivelAgressividade) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.desfraldado = desfraldado;
        this.hiperfoco = hiperfoco;
        this.anamnese = anamnese;
        this.diagnostico = diagnostico;
        this.resumoClinico = resumoClinico;
        this.nivelAgressividade = nivelAgressividade;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Long idPaciente) { this.idPaciente = idPaciente; }

    public Boolean getDesfraldado() { return desfraldado; }
    public void setDesfraldado(Boolean desfraldado) { this.desfraldado = desfraldado; }

    public String getHiperfoco() { return hiperfoco; }
    public void setHiperfoco(String hiperfoco) { this.hiperfoco = hiperfoco; }

    public String getAnamnese() { return anamnese; }
    public void setAnamnese(String anamnese) { this.anamnese = anamnese; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getResumoClinico() { return resumoClinico; }
    public void setResumoClinico(String resumoClinico) { this.resumoClinico = resumoClinico; }

    public Integer getNivelAgressividade() { return nivelAgressividade; }
    public void setNivelAgressividade(Integer nivelAgressividade) { this.nivelAgressividade = nivelAgressividade; }
}
