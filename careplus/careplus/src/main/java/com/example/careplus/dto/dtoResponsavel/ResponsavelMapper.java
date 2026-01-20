package com.example.careplus.dto.dtoResponsavel;

import com.example.careplus.dto.dtoEndereco.EnderecoMapper;
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

        // Mapear endereco se presente
        if (dto.getEndereco() != null) {
            entity.setEndereco(EnderecoMapper.toEntity(dto.getEndereco()));
        }

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

        // Mapear endereco se presente
        if (entity.getEndereco() != null) {
            dto.setEndereco(EnderecoMapper.toResponseDto(entity.getEndereco()));
        }

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

        // Atualizar endereco se presente
        if (dto.getEndereco() != null) {
            if (entity.getEndereco() != null) {
                // Atualizar endereco existente
                entity.getEndereco().setCep(dto.getEndereco().getCep());
                entity.getEndereco().setLogradouro(dto.getEndereco().getLogradouro());
                entity.getEndereco().setNumero(dto.getEndereco().getNumero());
                entity.getEndereco().setComplemento(dto.getEndereco().getComplemento());
                entity.getEndereco().setBairro(dto.getEndereco().getBairro());
                entity.getEndereco().setCidade(dto.getEndereco().getCidade());
                entity.getEndereco().setEstado(dto.getEndereco().getEstado());
            } else {
                // Criar novo endereco
                entity.setEndereco(EnderecoMapper.toEntity(dto.getEndereco()));
            }
        }

        return entity;
    }
    //SAI DTO
}
