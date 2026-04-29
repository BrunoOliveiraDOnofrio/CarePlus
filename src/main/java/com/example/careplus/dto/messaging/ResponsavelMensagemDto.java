package com.example.careplus.dto.messaging;

/**
 * Subconjunto de Responsavel esperado pelo consumer.
 */
public class ResponsavelMensagemDto {
    private String nome;
    private String telefone;

    public ResponsavelMensagemDto() {}

    public ResponsavelMensagemDto(String nome, String telefone) {
        this.nome = nome;
        this.telefone = telefone;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}