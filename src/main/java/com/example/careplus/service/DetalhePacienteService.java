package com.example.careplus.service;

import com.example.careplus.dto.dtoDetalhes.AtualizarFichaClinicaDTO;
import com.example.careplus.dto.dtoDetalhes.AtualizarObservacoesComportamentaisDTO;
import com.example.careplus.dto.dtoDetalhes.AtualizarTratamentoDTO;
import com.example.careplus.dto.dtoPaciente.DetalhePacienteDTO;
import com.example.careplus.model.ConsultaProntuario;
import com.example.careplus.model.FichaClinica;
import com.example.careplus.model.Medicacao;
import com.example.careplus.model.Paciente;
import com.example.careplus.model.Tratamento;
import com.example.careplus.model.ClassificacaoDoencas;
import com.example.careplus.repository.ConsultaProntuarioRepository;
import com.example.careplus.repository.FichaClinicaRepository;
import com.example.careplus.repository.PacienteRepository;
import com.example.careplus.repository.TratamentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
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
        fichaClinicaDTO.setObservacoesComportamentais(
                fichaClinicaService.buscarFichaClinicaPorId(pacienteId).getResumoClinico()
        );
        fichaClinicaDTO.setAtendimentoEspecial(fichaClinicaService.buscarFichaClinicaPorId(pacienteId).getNivelAgressividade());
        fichaClinicaDTO.setDesfraldada(fichaClinicaService.buscarFichaClinicaPorId(pacienteId).getDesfraldado());
        fichaClinicaDTO.setHiperfoco(fichaClinicaService.buscarFichaClinicaPorId(pacienteId).getHiperfoco());
        dto.setFichaClinica(fichaClinicaDTO);

        // Última Consulta
        if (ultimaConsulta != null) {
            DetalhePacienteDTO.UltimaConsultaDTO ultimaConsultaDTO = new DetalhePacienteDTO.UltimaConsultaDTO();
            ultimaConsultaDTO.setData(ultimaConsulta.getData());
            ultimaConsultaDTO.setHorarioInicio(ultimaConsulta.getHorarioInicio());
            ultimaConsultaDTO.setHorarioFim(ultimaConsulta.getHorarioFim());
            ultimaConsultaDTO.setMateriais(ultimaConsultaDTO.getMateriais());
            dto.setUltimaConsulta(ultimaConsultaDTO);
        }

        List<Medicacao> medicacoes = fichaClinicaService.buscarFichaClinicaPorId(pacienteId).getMedicacoes();
        List<DetalhePacienteDTO.MedicacaoDTO> medicacoesDTO = new ArrayList<>();
        if (medicacoes != null) {
            for (Medicacao medicacao : medicacoes) {
                medicacoesDTO.add(new DetalhePacienteDTO.MedicacaoDTO(
                        medicacao.getIdMedicacao(),
                        medicacao.getNomeMedicacao(),
                        medicacao.getDataInicio(),
                        medicacao.getDataFim(),
                        medicacao.getAtivo(),
                        medicacao.getDataModificacao()
                ));
            }
        }
        dto.setMedicacoes(medicacoesDTO);

        // CIDs
        List<ClassificacaoDoencas> cids = fichaClinicaService.buscarFichaClinicaPorId(pacienteId).getCid();
        List<DetalhePacienteDTO.CidDTO> cidsDTO = new ArrayList<>();
        if (cids != null) {
            for (ClassificacaoDoencas cid : cids) {
                cidsDTO.add(new DetalhePacienteDTO.CidDTO(
                        cid.getId(),
                        cid.getCid(),
                        cid.getDtModificacao()
                ));
            }
        }
        dto.setCids(cidsDTO);

        // Próxima Consulta
        if (!proximasConsultas.isEmpty()) {
            dto.setProximaConsulta(proximasConsultas.get(0).getData());
        }

        return dto;
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