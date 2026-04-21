package com.example.careplus.dto.dtoRelatorio;

import com.example.careplus.model.Paciente;
import java.util.List;
import java.util.stream.Collectors;

public class RelatorioMapper {

    public static PacientesSemConsultaDto toPacientesSemConsultaDto(Paciente paciente) {
        if (paciente == null) {
            return null;
        }
        return new PacientesSemConsultaDto(
                paciente.getId(),
                paciente.getNome(),
                paciente.getEmail(),
                paciente.getConvenio()
        );
    }

    public static List<PacientesSemConsultaDto> toPacientesSemConsultaDtoList(List<Paciente> pacientes) {
        if (pacientes == null) {
            return null;
        }
        return pacientes.stream()
                .map(RelatorioMapper::toPacientesSemConsultaDto)
                .collect(Collectors.toList());
    }

    public static FuncionariosPorEspecialidadeDto toFuncionariosPorEspecialidadeDto(Object[] result) {
        if (result == null || result.length < 3) {
            return null;
        }
        return new FuncionariosPorEspecialidadeDto(
                (String) result[0],  // especialidade
                ((Number) result[1]).longValue(),  // totalFuncionarios
                ((Number) result[2]).longValue()   // totalPacientes
        );
    }

    public static List<FuncionariosPorEspecialidadeDto> toFuncionariosPorEspecialidadeDtoList(List<Object[]> results) {
        if (results == null) {
            return null;
        }
        return results.stream()
                .map(RelatorioMapper::toFuncionariosPorEspecialidadeDto)
                .collect(Collectors.toList());
    }

    public static PacientesPorConvenioDto toPacientesPorConvenioDto(Object[] result) {
        if (result == null || result.length < 2) {
            return null;
        }
        return new PacientesPorConvenioDto(
                (String) result[0],  // convenio
                ((Number) result[1]).longValue()   // totalClientes
        );
    }

    public static List<PacientesPorConvenioDto> toPacientesPorConvenioDtoList(List<Object[]> results) {
        if (results == null) {
            return null;
        }
        return results.stream()
                .map(RelatorioMapper::toPacientesPorConvenioDto)
                .collect(Collectors.toList());
    }
}
