package com.example.careplus.controller.dtoMaterial;

import com.example.careplus.model.Material;
import java.util.List;

public class MaterialMapper {

    public static Material toEntity(MaterialRequestDto dto) {
        Material a = new Material();
        a.setItem(dto.getItem());
        a.setTempoExposicao(dto.getTempoExposicao());
        a.setDataImplementacao(dto.getDataImplementacao());
        return a;
    }

    public static MaterialResponseDto toResponseDto(Material entity) {
        MaterialResponseDto dto = new MaterialResponseDto();
        dto.setId(entity.getId());
        dto.setItem(entity.getItem());
        dto.setTempoExposicao(entity.getTempoExposicao());
        dto.setDataImplementacao(entity.getDataImplementacao());
        return dto;
    }

    public static List<MaterialResponseDto> toResponseDto(List<Material> list) {
        return list.stream().map(MaterialMapper::toResponseDto).toList();
    }
}
