package com.example.careplus.service;

import com.example.careplus.controller.dtoPaciente.DetalhePacienteDTO;
import com.example.careplus.model.Consulta;
import com.example.careplus.model.Paciente;
import com.example.careplus.repository.ConsultaRepository;
import com.example.careplus.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DetalhePacienteService {

    private final PacienteRepository pacienteRepository;
    private final ConsultaRepository consultaRepository;
    private final ProntuarioService prontuarioService;

    public DetalhePacienteDTO buscarDetalhesCompletoPaciente(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        List<Consulta> consultas = consultaRepository.buscarUltimaConsultaPorPaciente(pacienteId);
        Consulta ultimaConsulta = consultas.isEmpty() ? null : consultas.get(0);

        List<Consulta> proximasConsultas = consultaRepository.buscarProximaConsultaPorPaciente(pacienteId);

        DetalhePacienteDTO dto = new DetalhePacienteDTO();
        dto.setPacienteId(paciente.getId());
        dto.setNome(paciente.getNome());

        // Ficha Clínica
        DetalhePacienteDTO.FichaClinicaDTO fichaClinica = new DetalhePacienteDTO.FichaClinicaDTO();
        fichaClinica.setIdade(Period.between(paciente.getDtNascimento(), LocalDate.now()).getYears());
        fichaClinica.setAnamnese(prontuarioService.buscarProntuarioPorId(pacienteId).getAnamnese());
        fichaClinica.setDiagnostico(prontuarioService.buscarProntuarioPorId(pacienteId).getDiagnostico());
        fichaClinica.setPlanoTerapeutico(paciente.getConvenio());
        dto.setFichaClinica(fichaClinica);

        // Observações Comportamentais
        dto.setObservacoesComportamentais(prontuarioService.buscarProntuarioPorId(pacienteId).getResumoClinico());

        if (ultimaConsulta != null) {
            // Observações da Última Consulta
            DetalhePacienteDTO.ObservacoesDTO observacoes = new DetalhePacienteDTO.ObservacoesDTO();
            observacoes.setCid(prontuarioService.buscarProntuarioPorId(pacienteId).getCid().getLast().getCid());
            observacoes.setMedicacao(prontuarioService.buscarProntuarioPorId(pacienteId).getMedicacoes().getLast().getNomeMedicacao());
            observacoes.setAtendimentoEspecial(prontuarioService.buscarProntuarioPorId(pacienteId).getNivelAgressividade());
            observacoes.setDesfraldada(prontuarioService.buscarProntuarioPorId(pacienteId).getDesfraldado());
            observacoes.setHiperfoco(prontuarioService.buscarProntuarioPorId(pacienteId).getHiperfoco());
            dto.setObservacoes(observacoes);

            // Última Consulta
            DetalhePacienteDTO.UltimaConsultaDTO ultimaConsultaDTO = new DetalhePacienteDTO.UltimaConsultaDTO();
            ultimaConsultaDTO.setData(ultimaConsulta.getDataHora());
            ultimaConsultaDTO.setMateriais(ultimaConsultaDTO.getMateriais());
            dto.setUltimaConsulta(ultimaConsultaDTO);
        }

        // Progresso do Tratamento
        DetalhePacienteDTO.ProgressoTratamentoDTO progresso = new DetalhePacienteDTO.ProgressoTratamentoDTO();
        progresso.setPercentual(calcularProgresso(paciente));
        progresso.setTratamentoFeito(prontuarioService.buscarProntuarioPorId(pacienteId).getTratamentos());
        progresso.setTratamentoAtual(prontuarioService.buscarProntuarioPorId(pacienteId).getTratamentos().getLast().getTipoDeTratamento());
        dto.setProgresso(progresso);

        // Próxima Consulta
        if (!proximasConsultas.isEmpty()) {
            dto.setProximaConsulta(proximasConsultas.get(0).getDataHora());
        }

        return dto;
    }

    private Integer calcularProgresso(Paciente paciente) {
        // Implementar lógica de cálculo do progresso
        return 65; // Exemplo
    }
}