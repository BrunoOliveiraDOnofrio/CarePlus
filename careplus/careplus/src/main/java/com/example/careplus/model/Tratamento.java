package com.example.careplus.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Tratamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Colocar o nome do tratamento. Exemplo: Intervenção do fonoaudiólogo.")
    private String tipoDeTatamento;

    @Schema(description = "Campo de status se a o tratamento está ativo ou não.")
    private Boolean finalizado;

    @Schema(description = "última data de modificação do tratamento")
    private LocalDateTime dataModificacao;

    public Tratamento() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoDeTatamento() {
        return tipoDeTatamento;
    }

    public void setTipoDeTatamento(String tipoDeTatamento) {
        this.tipoDeTatamento = tipoDeTatamento;
    }

    public Boolean getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }

    public LocalDateTime getDataModificacao() {
        return dataModificacao;
    }

    public void setDataModificacao(LocalDateTime dataModificacao) {
        this.dataModificacao = dataModificacao;
    }
}
