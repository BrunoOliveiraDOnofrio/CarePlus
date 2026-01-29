package com.example.careplus.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String item;
    private Integer tempoExposicao; // minutos
    private LocalDate dataImplementacao;

    @ManyToOne
    @JoinColumn(name = "fk_consulta")
    @JsonBackReference
    private ConsultaProntuario consultaProntuario;

    public ConsultaProntuario getConsultaProntuario() {
        return consultaProntuario;
    }

    public void setConsultaProntuario(ConsultaProntuario consultaProntuario) {
        this.consultaProntuario = consultaProntuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getTempoExposicao() {
        return tempoExposicao;
    }

    public void setTempoExposicao(Integer tempoExposicao) {
        this.tempoExposicao = tempoExposicao;
    }

    public LocalDate getDataImplementacao() {
        return dataImplementacao;
    }

    public void setDataImplementacao(LocalDate dataImplementacao) {
        this.dataImplementacao = dataImplementacao;
    }

}
