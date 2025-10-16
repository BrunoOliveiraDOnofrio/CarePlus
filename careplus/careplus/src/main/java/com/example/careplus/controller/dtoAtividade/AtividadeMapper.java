package com.example.careplus.controller.dtoAtividade;

import com.example.careplus.model.Atividade;
import java.util.List;

public class AtividadeMapper {

    public static Atividade toEntity(AtividadeRequestDto dto) {
        Atividade a = new Atividade();
        a.setItem(dto.getItem());
        a.setTempoExposicao(dto.getTempoExposicao());
        a.setDataImplementacao(dto.getDataImplementacao());
        return a;
    }

    public static AtividadeResponseDto toResponseDto(Atividade entity) {
        AtividadeResponseDto dto = new AtividadeResponseDto();
        dto.setId(entity.getIdAtividade());
        dto.setItem(entity.getItem());
        dto.setTempoExposicao(entity.getTempoExposicao());
        dto.setDataImplementacao(entity.getDataImplementacao());
        return dto;
    }

    public static List<AtividadeResponseDto> toResponseDto(List<Atividade> list) {
        return list.stream().map(AtividadeMapper::toResponseDto).toList();
    }
}
