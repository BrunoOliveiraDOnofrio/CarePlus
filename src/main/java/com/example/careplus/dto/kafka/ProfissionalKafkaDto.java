package com.example.careplus.dto.kafka;

/**
 * Subconjunto de Profissional esperado pelo consumer.
 */
public class ProfissionalKafkaDto {
    private Long id;
    private String nome;
    private String especialidade;
    private String tipoAtendimento;

    public ProfissionalKafkaDto() {}

    public ProfissionalKafkaDto(Long id, String nome, String especialidade, String tipoAtendimento) {
        this.id = id;
        this.nome = nome;
        this.especialidade = especialidade;
        this.tipoAtendimento = tipoAtendimento;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public String getTipoAtendimento() { return tipoAtendimento; }
    public void setTipoAtendimento(String tipoAtendimento) { this.tipoAtendimento = tipoAtendimento; }
}
