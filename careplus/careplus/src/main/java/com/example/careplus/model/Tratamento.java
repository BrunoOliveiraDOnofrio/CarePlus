package com.example.careplus.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Tratamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Colocar o nome do tratamento. Exemplo: Intervenção do fonoaudiólogo.")
    private String tipoDeTratamento;

    @Schema(description = "Campo de status se o tratamento está ativo ou não.")
    private Boolean finalizado;

    @Schema(description = "Última data de modificação do tratamento")
    private LocalDateTime dataModificacao;

    // 🔹 Vários tratamentos pertencem a um prontuário
    @ManyToOne
    @JoinColumn(name = "prontuario_id")
    @JsonBackReference
    private Prontuario prontuario;

    public Tratamento() {}

    public Tratamento(Long id, String tipoDeTratamento, Boolean finalizado, LocalDateTime dataModificacao, Prontuario prontuario) {
        this.id = id;
        this.tipoDeTratamento = tipoDeTratamento;
        this.finalizado = finalizado;
        this.dataModificacao = dataModificacao;
        this.prontuario = prontuario;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipoDeTratamento() { return tipoDeTratamento; }
    public void setTipoDeTratamento(String tipoDeTratamento) { this.tipoDeTratamento = tipoDeTratamento; }

    public Boolean getFinalizado() { return finalizado; }
    public void setFinalizado(Boolean finalizado) { this.finalizado = finalizado; }

    public LocalDateTime getDataModificacao() { return dataModificacao; }
    public void setDataModificacao(LocalDateTime dataModificacao) { this.dataModificacao = dataModificacao; }

    public Prontuario getProntuario() { return prontuario; }
    public void setProntuario(Prontuario prontuario) { this.prontuario = prontuario; }
}
