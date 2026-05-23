package com.example.careplus.dto.dtoMedicacao;

import com.example.careplus.model.Medicacao;

import java.util.List;
import java.util.stream.Collectors;

public class MedicacaoMapper {

    public static MedicacaoResponseDto toResponseDto(Medicacao entity) {
        if (entity == null) return null;
        return new MedicacaoResponseDto(
                entity.getIdMedicacao(),
                entity.getNomeMedicacao(),
                entity.getDataInicio(),
                entity.getDataFim(),
                entity.getAtivo()
        );
    }

    public static List<MedicacaoResponseDto> toResponseDto(List<Medicacao> list) {
        return list.stream().map(MedicacaoMapper::toResponseDto).collect(Collectors.toList());
    }
}
