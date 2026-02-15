package com.example.careplus.dto.dtoConsulta;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConsultaAtualResponseDto {

    // Dados da Consulta Atual
    private Long consultaId;
    private LocalDate data;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;
    private String tipo;
    private String especialidade;
    private String nomeProfissional;
    private String tratamentoAtual;

    // Dados do Paciente
    private DadosPaciente dadosPaciente;

    // Última Consulta
    private UltimaConsulta ultimaConsulta;


    public ConsultaAtualResponseDto() {
    }

    // Getters e Setters
    public Long getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(Long consultaId) {
        this.consultaId = consultaId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(LocalTime horarioFim) {
        this.horarioFim = horarioFim;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getNomeProfissional() {
        return nomeProfissional;
    }

    public void setNomeProfissional(String nomeProfissional) {
        this.nomeProfissional = nomeProfissional;
    }

    public String getTratamentoAtual() {
        return tratamentoAtual;
    }

    public void setTratamentoAtual(String tratamentoAtual) {
        this.tratamentoAtual = tratamentoAtual;
    }


    public DadosPaciente getDadosPaciente() {
        return dadosPaciente;
    }

    public void setDadosPaciente(DadosPaciente dadosPaciente) {
        this.dadosPaciente = dadosPaciente;
    }

    public UltimaConsulta getUltimaConsulta() {
        return ultimaConsulta;
    }

    public void setUltimaConsulta(UltimaConsulta ultimaConsulta) {
        this.ultimaConsulta = ultimaConsulta;
    }


    // Classe interna para dados do paciente
    public static class DadosPaciente {
        private Long pacienteId;
        private String nome;
        private String contato;
        private Integer idade;
        private String cid;
        private Boolean desfraldado;
        private String hiperfocoAtual;
        private String medicacoes;
        private String diagnostico;
        private String atendimentoEspecial;

        public DadosPaciente() {
        }

        public Long getPacienteId() {
            return pacienteId;
        }

        public void setPacienteId(Long pacienteId) {
            this.pacienteId = pacienteId;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getContato() {
            return contato;
        }

        public void setContato(String contato) {
            this.contato = contato;
        }

        public Integer getIdade() {
            return idade;
        }

        public void setIdade(Integer idade) {
            this.idade = idade;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public Boolean getDesfraldado() {
            return desfraldado;
        }

        public void setDesfraldado(Boolean desfraldado) {
            this.desfraldado = desfraldado;
        }

        public String getHiperfocoAtual() {
            return hiperfocoAtual;
        }

        public void setHiperfocoAtual(String hiperfocoAtual) {
            this.hiperfocoAtual = hiperfocoAtual;
        }

        public String getMedicacoes() {
            return medicacoes;
        }

        public void setMedicacoes(String medicacoes) {
            this.medicacoes = medicacoes;
        }

        public String getDiagnostico() {
            return diagnostico;
        }

        public void setDiagnostico(String diagnostico) {
            this.diagnostico = diagnostico;
        }

        public String getAtendimentoEspecial() {
            return atendimentoEspecial;
        }

        public void setAtendimentoEspecial(String atendimentoEspecial) {
            this.atendimentoEspecial = atendimentoEspecial;
        }
    }

    // Classe interna para última consulta
    public static class UltimaConsulta {
        private Long consultaId;
        private LocalDate data;
        private String tratamento;

        public UltimaConsulta() {
        }

        public Long getConsultaId() {
            return consultaId;
        }

        public void setConsultaId(Long consultaId) {
            this.consultaId = consultaId;
        }

        public LocalDate getData() {
            return data;
        }

        public void setData(LocalDate data) {
            this.data = data;
        }

        public String getTratamento() {
            return tratamento;
        }

        public void setTratamento(String tratamento) {
            this.tratamento = tratamento;
        }
    }
}

