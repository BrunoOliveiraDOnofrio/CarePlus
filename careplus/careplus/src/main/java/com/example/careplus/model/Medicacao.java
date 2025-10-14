package com.example.careplus.model;

import java.time.Duration;
import java.time.LocalDate;

public class Medicacao implements Comparable<Medicacao> {

    private Long idMedicacao;
    private String nomeMedicacao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean ativo;

    public Medicacao(Long idMedicacao, String nomeMedicacao, LocalDate dataInicio, LocalDate dataFim, boolean ativo) {
        this.idMedicacao = idMedicacao;
        this.nomeMedicacao = nomeMedicacao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.ativo = ativo;
    }

    public Long getIdMedicacao() {
        return idMedicacao;
    }

    public void setIdMedicacao(Long idMedicacao) {
        this.idMedicacao = idMedicacao;
    }

    public String getNomeMedicacao() {
        return nomeMedicacao;
    }

    public void setNomeMedicacao(String nomeMedicacao) {
        this.nomeMedicacao = nomeMedicacao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Duration getTempoMedicando() {
        LocalDate fim;

        if (dataFim == null) {
            fim = LocalDate.now();
        } else {
            fim = dataFim;
        }

        return Duration.between(dataInicio.atStartOfDay(), fim.atStartOfDay());
    }

    @Override
    public String toString() {
        long dias = getTempoMedicando().toDays();
        String status;

        if (ativo) {
            status = "Sim";
        } else {
            status = "Não";
        }

        return String.format("Medicação: %s | Tempo medicando: %d dias | Ativa: %s", nomeMedicacao, dias, status);
    }

    @Override
    public int compareTo(Medicacao outra) {
        return this.nomeMedicacao.compareToIgnoreCase(outra.nomeMedicacao);
    }

}
