package com.example.careplus.service;

import com.example.careplus.dto.dtoRelatorio.FuncionariosPorEspecialidadeDto;
import com.example.careplus.dto.dtoRelatorio.PacientesPorConvenioDto;
import com.example.careplus.dto.dtoRelatorio.RelatorioMapper;
import com.example.careplus.repository.FuncionarioRepository;
import com.example.careplus.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioService {

    private final PacienteRepository pacienteRepository;
    private final FuncionarioRepository funcionarioRepository;

    public RelatorioService(PacienteRepository pacienteRepository, FuncionarioRepository funcionarioRepository) {
        this.pacienteRepository = pacienteRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    /**
     * Obtém quantidade de pacientes que nunca tiveram uma consulta
     * @return Quantidade de pacientes sem consulta
     */
    public Long obterPacientesSemConsulta() {
        return pacienteRepository.findPacientesSemConsulta();
    }

    /**
     * Obtém estatísticas de funcionários agrupados por especialidade
     * @return Lista com total de funcionários e pacientes por especialidade
     */
    public List<FuncionariosPorEspecialidadeDto> obterFuncionariosPorEspecialidade() {
        List<Object[]> resultados = funcionarioRepository.findAllAtivosExcluindoAdminEAgendamento();
        return RelatorioMapper.toFuncionariosPorEspecialidadeDtoList(resultados);
    }

    /**
     * Obtém estatísticas de pacientes agrupados por convênio/seguradora
     * @return Lista com total de pacientes por convênio, ordenado decrescente
     */
    public List<PacientesPorConvenioDto> obterPacientesPorConvenio() {
        var resultados = pacienteRepository.findPacientesPorConvenio();
        return RelatorioMapper.toPacientesPorConvenioDtoList(resultados);
    }

    /**
     * Obtém contagem de pacientes ativos com consulta marcada
     * @return Contagem de pacientes ativos com consulta futura
     */
    public long obterContagemPacientesAtivosComConsultaMarcada() {
        return pacienteRepository.countPacientesAtivosComConsultaMarcada();
    }

    /**
     * Obtém contagem de pacientes ativos sem consulta
     * @return Contagem de pacientes ativos que nunca tiveram uma consulta
     */
    public long obterContagemPacientesSemConsulta() {
        return pacienteRepository.findPacientesSemConsulta();
    }
}
