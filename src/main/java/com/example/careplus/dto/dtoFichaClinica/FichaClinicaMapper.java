package com.example.careplus.dto.dtoFichaClinica;

import com.example.careplus.model.FichaClinica;

import java.util.List;

public class FichaClinicaMapper {

    public static FichaClinicaResponseDto toResponseDto(FichaClinica entity) {
        if (entity == null) {
            return null;
        }

        return new FichaClinicaResponseDto(
                entity.getId(),
                entity.getPaciente() != null ? entity.getPaciente().getId() : null,
                entity.getDesfraldado(),
                entity.getHiperfoco(),
                entity.getAnamnese(),
                entity.getDiagnostico(),
                entity.getResumoClinico(),
                entity.getNivelAgressividade()
        );
    }

    public static List<FichaClinicaResponseDto> toResponseDto(List<FichaClinica> entities) {
        return entities.stream().map(FichaClinicaMapper::toResponseDto).toList();
    }
}
