package com.example.careplus.dto.dtoEndereco;

import com.example.careplus.model.Endereco;

import java.util.List;

public class EnderecoMapper {

    public static Endereco toEntity(EnderecoRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Endereco entity = new Endereco();
        entity.setCep(dto.getCep());
        entity.setLogradouro(dto.getLogradouro());
        entity.setNumero(dto.getNumero());
        entity.setComplemento(dto.getComplemento());
        entity.setBairro(dto.getBairro());
        entity.setCidade(dto.getCidade());
        entity.setEstado(dto.getEstado());

        return entity;
    }

    public static Endereco toEntity(EnderecoResponseDto dto) {
        if (dto == null) {
            return null;
        }

        Endereco entity = new Endereco();
        entity.setId(dto.getId());
        entity.setCep(dto.getCep());
        entity.setLogradouro(dto.getLogradouro());
        entity.setNumero(dto.getNumero());
        entity.setComplemento(dto.getComplemento());
        entity.setBairro(dto.getBairro());
        entity.setCidade(dto.getCidade());
        entity.setEstado(dto.getEstado());

        return entity;
    }

    public static EnderecoResponseDto toResponseDto(Endereco entity) {
        if (entity == null) {
            return null;
        }

        EnderecoResponseDto dto = new EnderecoResponseDto(
                entity.getId(),
                entity.getCep(),
                entity.getLogradouro(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getBairro(),
                entity.getCidade(),
                entity.getEstado()
        );

        return dto;
    }

    public static List<EnderecoResponseDto> toResponseDto(List<Endereco> entities) {
        return entities.stream()
                .map(EnderecoMapper::toResponseDto)
                .toList();
    }
}

