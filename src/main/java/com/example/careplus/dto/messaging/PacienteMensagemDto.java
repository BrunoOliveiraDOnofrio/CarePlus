package com.example.careplus.dto.messaging;

/**
 * Subconjunto de Paciente esperado pelo consumer.
 */
public class PacienteMensagemDto {
    private String nome;
    private ResponsavelMensagemDto responsavel;

    public PacienteMensagemDto() {}

    public PacienteMensagemDto(String nome, ResponsavelMensagemDto responsavel) {
        this.nome = nome;
        this.responsavel = responsavel;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public ResponsavelMensagemDto getResponsavel() { return responsavel; }
    public void setResponsavel(ResponsavelMensagemDto responsavel) { this.responsavel = responsavel; }
}
