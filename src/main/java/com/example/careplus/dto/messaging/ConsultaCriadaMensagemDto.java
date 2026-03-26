package com.example.careplus.dto.messaging;

/**
 * Representa uma consulta dentro do evento enviado via mensageria.
 */
public class ConsultaCriadaMensagemDto {
    private Long id;
    private PacienteMensagemDto paciente;
    private ProfissionalMensagemDto funcionario;
    private String dataHora;
    private String tipo;

    public ConsultaCriadaMensagemDto() {}

    public ConsultaCriadaMensagemDto(Long id, PacienteMensagemDto paciente, ProfissionalMensagemDto funcionario,
                                     String dataHora, String tipo) {
        this.id = id;
        this.paciente = paciente;
        this.funcionario = funcionario;
        this.dataHora = dataHora;
        this.tipo = tipo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PacienteMensagemDto getPaciente() { return paciente; }
    public void setPaciente(PacienteMensagemDto paciente) { this.paciente = paciente; }

    public ProfissionalMensagemDto getFuncionario() { return funcionario; }
    public void setFuncionario(ProfissionalMensagemDto funcionario) { this.funcionario = funcionario; }

    public String getDataHora() { return dataHora; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
