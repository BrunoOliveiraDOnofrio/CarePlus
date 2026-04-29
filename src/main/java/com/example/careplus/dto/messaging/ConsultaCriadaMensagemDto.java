package com.example.careplus.dto.messaging;

import java.util.List;

/**
 * Representa uma consulta dentro do evento enviado via mensageria.
 */
public class ConsultaCriadaMensagemDto {
    private PacienteMensagemDto paciente;
    private List<ProfissionalMensagemDto> profissionais;
    private String dataHora;
    private String tipo;

    public ConsultaCriadaMensagemDto() {}

    public ConsultaCriadaMensagemDto(PacienteMensagemDto paciente, List<ProfissionalMensagemDto> profissionais,
                                     String dataHora, String tipo) {
        this.paciente = paciente;
        this.profissionais = profissionais;
        this.dataHora = dataHora;
        this.tipo = tipo;
    }

    public PacienteMensagemDto getPaciente() { return paciente; }
    public void setPaciente(PacienteMensagemDto paciente) { this.paciente = paciente; }

    public List<ProfissionalMensagemDto> getProfissionais() { return profissionais; }
    public void setProfissionais(List<ProfissionalMensagemDto> profissionais) { this.profissionais = profissionais; }

    public String getDataHora() { return dataHora; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
