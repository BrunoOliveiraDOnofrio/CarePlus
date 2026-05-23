package com.example.careplus.dto.dtoRelatorio;

import java.util.List;
import java.util.stream.Collectors;

public class RelatorioMapper {

    public static FuncionariosPorEspecialidadeDto toFuncionariosPorEspecialidadeDto(Object[] result) {
        if (result == null || result.length < 3) {
            return null;
        }
        return new FuncionariosPorEspecialidadeDto(
                (String) result[0],
                ((Number) result[1]).longValue(),
                ((Number) result[2]).longValue()
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
                (String) result[0],
                ((Number) result[1]).longValue()
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
