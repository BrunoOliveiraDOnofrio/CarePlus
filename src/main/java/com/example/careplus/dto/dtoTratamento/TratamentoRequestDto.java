package com.example.careplus.dto.dtoTratamento;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class TratamentoRequestDto {

    @Schema(description = "Colocar o nome do tratamento. Exemplo: Intervenção do fonoaudiólogo.")
    private String tipoDeTratamento;

    @Schema(description = "Campo de status se a o tratamento está ativo ou não.")
    private Boolean finalizado;

    private Long idProntuario;

    public TratamentoRequestDto(String tipoDeTratamento, Boolean finalizado, Long idProntuario) {
        this.tipoDeTratamento = tipoDeTratamento;
        this.finalizado = finalizado;
        this.idProntuario = idProntuario;
    }

    public TratamentoRequestDto(String tipoDeTratamento) {
        this.tipoDeTratamento = tipoDeTratamento;
    }

    public String getTipoDeTratamento() {
        return tipoDeTratamento;
    }

    public void setTipoDeTratamento(String tipoDeTratamento) {
        this.tipoDeTratamento = tipoDeTratamento;
    }

    public Boolean getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }

    public Long getIdProntuario() {
        return idProntuario;
    }

    public void setIdProntuario(Long idProntuario) {
        this.idProntuario = idProntuario;
    }
}

