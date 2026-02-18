package com.example.careplus.service;

import com.example.careplus.dto.dtoDetalhes.AtualizarFichaClinicaDTO;
import com.example.careplus.dto.dtoDetalhes.AtualizarObservacoesComportamentaisDTO;
import com.example.careplus.dto.dtoDetalhes.AtualizarTratamentoDTO;
import com.example.careplus.dto.dtoPaciente.DetalhePacienteDTO;
import com.example.careplus.model.ConsultaProntuario;
import com.example.careplus.model.Paciente;
import com.example.careplus.model.FichaClinica;
import com.example.careplus.model.Tratamento;
import com.example.careplus.repository.ConsultaProntuarioRepository;
import com.example.careplus.repository.PacienteRepository;
import com.example.careplus.repository.FichaClinicaRepository;
import com.example.careplus.repository.TratamentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DetalhePacienteService {

    private final PacienteRepository pacienteRepository;
    private final ConsultaProntuarioRepository consultaProntuarioRepository;
    private final FichaClinicaRepository fichaClinicaRepository;
    private final FichaClinicaService fichaClinicaService;
    private final TratamentoRepository tratamentoRepository;

    public DetalhePacienteDTO buscarDetalhesCompletoPaciente(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        List<ConsultaProntuario> consultas = consultaProntuarioRepository.buscarUltimaConsultaPorPaciente(pacienteId);
        ConsultaProntuario ultimaConsulta = consultas.isEmpty() ? null : consultas.get(0);

        List<ConsultaProntuario> proximasConsultas = consultaProntuarioRepository.buscarProximaConsultaPorPaciente(pacienteId);

        DetalhePacienteDTO dto = new DetalhePacienteDTO();
        dto.setPacienteId(paciente.getId());
        dto.setNome(paciente.getNome());

        // Ficha Clínica
        DetalhePacienteDTO.FichaClinicaDTO fichaClinicaDTO = new DetalhePacienteDTO.FichaClinicaDTO();
        fichaClinicaDTO.setId(fichaClinicaService.buscarFichaClinicaPorId(pacienteId).getId());
        fichaClinicaDTO.setIdade(Period.between(paciente.getDtNascimento(), LocalDate.now()).getYears());
        fichaClinicaDTO.setAnamnese(fichaClinicaService.buscarFichaClinicaPorId(pacienteId).getAnamnese());
        fichaClinicaDTO.setDiagnostico(fichaClinicaService.buscarFichaClinicaPorId(pacienteId).getDiagnostico());
        fichaClinicaDTO.setPlanoTerapeutico(paciente.getConvenio());
        dto.setFichaClinica(fichaClinicaDTO);

        // Observações Comportamentais
        dto.setObservacoesComportamentais(fichaClinicaService.buscarFichaClinicaPorId(pacienteId).getResumoClinico());

        if (ultimaConsulta != null) {
            // Observações da Última Consulta
            DetalhePacienteDTO.ObservacoesDTO observacoes = new DetalhePacienteDTO.ObservacoesDTO();
            observacoes.setCid(fichaClinicaService.buscarFichaClinicaPorId(pacienteId).getCid().getLast().getCid());
            observacoes.setMedicacao(fichaClinicaService.buscarFichaClinicaPorId(pacienteId).getMedicacoes().getLast().getNomeMedicacao());
            observacoes.setAtendimentoEspecial(fichaClinicaService.buscarFichaClinicaPorId(pacienteId).getNivelAgressividade());
            observacoes.setDesfraldada(fichaClinicaService.buscarFichaClinicaPorId(pacienteId).getDesfraldado());
            observacoes.setHiperfoco(fichaClinicaService.buscarFichaClinicaPorId(pacienteId).getHiperfoco());
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
        progresso.setTratamentoFeito(fichaClinicaService.buscarFichaClinicaPorId(pacienteId).getTratamentos());
        progresso.setTratamentoAtual(fichaClinicaService.buscarFichaClinicaPorId(pacienteId).getTratamentos().getLast().getTipoDeTratamento());
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

    @Transactional
    public void atualizarFichaClinica(Long pacienteId, AtualizarFichaClinicaDTO dto) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        FichaClinica fichaClinica = fichaClinicaService.buscarFichaClinicaPorId(pacienteId);

        fichaClinica.setAnamnese(dto.getAnamnese());
        fichaClinica.setDiagnostico(dto.getDiagnostico());
        paciente.setConvenio(dto.getPlanoTerapeutico());

        fichaClinicaRepository.save(fichaClinica);
        pacienteRepository.save(paciente);
    }

    @Transactional
    public void atualizarObservacoesComportamentais(Long pacienteId, AtualizarObservacoesComportamentaisDTO dto) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        FichaClinica fichaClinica = fichaClinicaService.buscarFichaClinicaPorId(pacienteId);

        fichaClinica.setResumoClinico(dto.getObservacoesComportamentais());

        fichaClinicaRepository.save(fichaClinica);
        pacienteRepository.save(paciente);
    }

    @Transactional
    public void atualizarTratamento(Long pacienteId, AtualizarTratamentoDTO dto) {
        // busca a ficha clínica do paciente pra ter certeza que existe
        FichaClinica fichaClinica = fichaClinicaRepository.findById(dto.getIdFichaClinica())
                .orElseThrow(() -> new RuntimeException("Ficha clínica não encontrada"));

        // busca o tratamento específico pelo tipo de tratamento e pela ficha clínica
        Tratamento tratamento = tratamentoRepository
                .findByTipoDeTratamentoAndFichaClinica_Id(dto.getTipoDeTratamento(), dto.getIdFichaClinica())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Tratamento '%s' não encontrado para a ficha clínica %d",
                                dto.getTipoDeTratamento(), dto.getIdFichaClinica())));

        // atualiza apenas o status de finalizado
        tratamento.setFinalizado(dto.getFinalizado());
        // atualiza a data de modificação
        tratamento.setDataModificacao(LocalDateTime.now());

        // salva as alterações
        tratamentoRepository.save(tratamento);
    }
}