package com.example.careplus.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class ClassificacaoDoencas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cid;
    private LocalDate dtModificacao;

    // 🔹 Várias classificações pertencem a uma ficha clínica
    @ManyToOne
    @JoinColumn(name = "prontuario_id")
    @JsonBackReference
    private FichaClinica fichaClinica;

    public ClassificacaoDoencas() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCid() { return cid; }
    public void setCid(String cid) { this.cid = cid; }

    public LocalDate getDtModificacao() { return dtModificacao; }
    public void setDtModificacao(LocalDate dtModificacao) { this.dtModificacao = dtModificacao; }

    public FichaClinica getFichaClinica() { return fichaClinica; }
    public void setFichaClinica(FichaClinica fichaClinica) { this.fichaClinica = fichaClinica; }
}
