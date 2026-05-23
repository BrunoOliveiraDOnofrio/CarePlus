package com.example.careplus.dto.dtoMedicacao;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class MedicacaoResponseDto {

    private Long idMedicacao;
    private String nomeMedicacao;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFim;

    private boolean ativo;

    public MedicacaoResponseDto() {}

    public MedicacaoResponseDto(Long idMedicacao, String nomeMedicacao, LocalDate dataInicio, LocalDate dataFim, boolean ativo) {
        this.idMedicacao = idMedicacao;
        this.nomeMedicacao = nomeMedicacao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.ativo = ativo;
    }

    public Long getIdMedicacao() { return idMedicacao; }
    public void setIdMedicacao(Long idMedicacao) { this.idMedicacao = idMedicacao; }

    public String getNomeMedicacao() { return nomeMedicacao; }
    public void setNomeMedicacao(String nomeMedicacao) { this.nomeMedicacao = nomeMedicacao; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public boolean getAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
