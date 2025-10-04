package com.example.careplus.controller.dtoResponsavel;

import com.example.careplus.model.Responsavel;

import java.util.List;

public class ResponsavelMapper {

    //ENTRA DTO
    public static Responsavel toEntity(ResponsavelRequestDto dto){
        if (dto == null){
            return null;
        }

        Responsavel entity = new Responsavel();
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setTelefone(dto.getTelefone());
        entity.setDtNascimento(dto.getDtNascimento());
        entity.setCpf(dto.getCpf());
        entity.setConvenio(dto.getConvenio());

        return entity;
    }
    //SAI ENTIDADE

    //ENTRA ENTIDADE
    public static ResponsavelResponseDto toResponseDto(Responsavel entity){
        if (entity == null){
            return null;
        }

        ResponsavelResponseDto dto = new ResponsavelResponseDto();
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());
        dto.setDtNascimento(entity.getDtNascimento());
        dto.setCpf(entity.getCpf());
        dto.setConvenio(entity.getConvenio());

        return dto;
    }
    //SAI DTO

    //ENTRA ENTIDADE - LISTAGEM
    public static List<ResponsavelResponseDto> toResponseDto(List<Responsavel> entity){
        return entity.stream()
                .map(ResponsavelMapper::toResponseDto)
                .toList();
    }
    //SAI DTO - LISTAGEM

    //ENTRA REQUEST + ENTIDADE
    public static Responsavel updateEntityFromDto(ResponsavelRequestDto dto, Responsavel entity) {
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setTelefone(dto.getTelefone());
        entity.setDtNascimento(dto.getDtNascimento());
        entity.setCpf(dto.getCpf());
        entity.setConvenio(dto.getConvenio());

        return entity;
    }
    //SAI DTO
}
