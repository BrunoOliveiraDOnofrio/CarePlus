package com.example.careplus.dto.dtoCid;

import com.example.careplus.model.ClassificacaoDoencas;

import java.util.List;
import java.util.stream.Collectors;

public class ClassificacaoDoencasMapper {

    public static ClassificacaoDoencasResponseDto toResponseDto(ClassificacaoDoencas entity) {
        if (entity == null) return null;
        return new ClassificacaoDoencasResponseDto(entity.getId(), entity.getCid());
    }

    public static List<ClassificacaoDoencasResponseDto> toResponseDto(List<ClassificacaoDoencas> list) {
        return list.stream().map(ClassificacaoDoencasMapper::toResponseDto).collect(Collectors.toList());
    }
}
