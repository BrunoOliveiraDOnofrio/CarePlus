package com.example.careplus.controller.dtoPaciente;

import com.example.careplus.model.Tratamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalhePacienteDTO {
    // Informações do Paciente
    private Long pacienteId;
    private String nome;
    private String fotoPerfil;

    // Ficha Clínica
    private FichaClinicaDTO fichaClinica;

    // Observações Comportamentais
    private String observacoesComportamentais;

    // Observações da Última Consulta
    private ObservacoesDTO observacoes;

    // Última Consulta
    private UltimaConsultaDTO ultimaConsulta;

    // Progresso do Tratamento
    private ProgressoTratamentoDTO progresso;

    // Próxima Consulta
    private LocalDateTime proximaConsulta;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FichaClinicaDTO {
        private Integer idade;
        private String anamnese;
        private String diagnostico;
        private String planoTerapeutico;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ObservacoesDTO {
        private String cid;
        private String medicacao;
        private Integer atendimentoEspecial;
        private Boolean desfraldada;
        private String hiperfoco;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UltimaConsultaDTO {
        private LocalDateTime data;
        private List<String> materiais;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProgressoTratamentoDTO {
        private Integer percentual;
        private List<Tratamento> tratamentoFeito;
        private String tratamentoAtual;
    }
}
