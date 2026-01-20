package com.example.careplus.dto.dtoMaterial;

import com.example.careplus.model.Material;
import java.util.List;
import java.util.stream.Collectors;

public class MaterialMapper {

    public static Material toEntity(MaterialRequestDto dto) {
        Material a = new Material();
        a.setItem(dto.getItem());
        a.setTempoExposicao(dto.getTempoExposicao());
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
        return list.stream().map(MaterialMapper::toResponseDto).collect(Collectors.toList());
    }

    public static List<Material> toEntityList(List<MaterialRequestDto> list) {
        return list.stream().map(MaterialMapper::toEntity).collect(Collectors.toList());
    }
}
