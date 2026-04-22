package com.example.careplus.dto.dtoPaciente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    // CIDs
    private List<CidDTO> cids;

    // Medicações
    private List<MedicacaoDTO> medicacoes;

    // Última Consulta
    private UltimaConsultaDTO ultimaConsulta;

    // Próxima Consulta
    private LocalDate proximaConsulta;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FichaClinicaDTO {
        private Long id;
        private Integer idade;
        private String anamnese;
        private String diagnostico;
        private String planoTerapeutico;
        private String observacoesComportamentais;
        private Integer atendimentoEspecial;
        private Boolean desfraldada;
        private String hiperfoco;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UltimaConsultaDTO {
        private Long consultaId;
        private LocalDate data;
        private String especialidade;
        private String nomeFuncionario;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MedicacaoDTO {
        private Long idMedicacao;
        private String nomeMedicacao;
        private LocalDate dataInicio;
        private LocalDate dataFim;
        private Boolean ativo;
        private LocalDateTime dataModificacao;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CidDTO {
        private Long id;
        private String cid;
        private LocalDate dtModificacao;
    }
}
