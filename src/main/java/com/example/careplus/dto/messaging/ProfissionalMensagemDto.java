package com.example.careplus.dto.messaging;

/**
 * Subconjunto de Profissional esperado pelo consumer.
 */
public class ProfissionalMensagemDto {
    private String nome;
    private String especialidade;

    public ProfissionalMensagemDto() {}

    public ProfissionalMensagemDto(String nome, String especialidade) {
        this.nome = nome;
        this.especialidade = especialidade;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
}
