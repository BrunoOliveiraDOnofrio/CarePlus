package com.example.careplus.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Prontuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    @Schema(description = "Diogo Francisco dos Santos")
    private Paciente paciente;

    @Schema(description = "Sim")
    private String desfraldado;

    @Schema(description = "Dinossauro")
    private String hiperfoco;

    @Schema(description = "Anotação da conversa com os pais")
    private String anamnese;

    @Schema(description = "Autismo nível 3")
    private String diagnostico;

    @Schema(description = "O paciente vem evoluindo bem")
    private String resumoClinico;

    @Schema(description = "2")
    private String nivelAgressividade;

    @OneToMany(mappedBy = "prontuario", cascade = CascadeType.ALL) // permite modificar
    @JsonManagedReference
    private List<ClassificacaoDoencas> cid;

    @OneToMany(mappedBy = "prontuario", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Tratamento> tratamentos;


    public Prontuario() {
    }

    public Prontuario(Long id, Paciente paciente, String desfraldado, String hiperfoco,
                      String anamnese, String diagnostico, String resumoClinico, String nivelAgressividade) {
        this.id = id;
        this.paciente = paciente;
        this.desfraldado = desfraldado;
        this.hiperfoco = hiperfoco;
        this.anamnese = anamnese;
        this.diagnostico = diagnostico;
        this.resumoClinico = resumoClinico;
        this.nivelAgressividade = nivelAgressividade;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public String getDesfraldado() { return desfraldado; }
    public void setDesfraldado(String desfraldado) { this.desfraldado = desfraldado; }

    public String getHiperfoco() { return hiperfoco; }
    public void setHiperfoco(String hiperfoco) { this.hiperfoco = hiperfoco; }

    public String getAnamnese() { return anamnese; }
    public void setAnamnese(String anamnese) { this.anamnese = anamnese; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getResumoClinico() { return resumoClinico; }
    public void setResumoClinico(String resumoClinico) { this.resumoClinico = resumoClinico; }

    public String getNivelAgressividade() { return nivelAgressividade; }
    public void setNivelAgressividade(String nivelAgressividade) { this.nivelAgressividade = nivelAgressividade; }

    public List<ClassificacaoDoencas> getCid() { return cid; }
    public void setCid(List<ClassificacaoDoencas> cid) { this.cid = cid; }

    public List<Tratamento> getTratamentos() { return tratamentos; }
    public void setTratamentos(List<Tratamento> tratamentos) { this.tratamentos = tratamentos; }
}
