package com.example.careplus.dto.dtoCuidador;

import com.example.careplus.dto.dtoEndereco.EnderecoMapper;
import com.example.careplus.model.Cuidador;
import com.example.careplus.model.Paciente;
import com.example.careplus.model.Responsavel;

import java.util.List;

public class CuidadorMapper {

    // ENTRA DTO
    public static Cuidador toEntity(CuidadorRequestDto dto, Paciente paciente, Responsavel responsavel) {
        if (dto == null) {
            return null;
        }

        Cuidador entity = new Cuidador();
        entity.setPaciente(paciente);
        entity.setResponsavel(responsavel);
        entity.setParentesco(dto.getParentesco());

        return entity;
    }
    // SAI ENTIDADE
    // ENTRA ENTIDADE
    public static CuidadorResponseDto toResponseDto(Cuidador entity) {
        if (entity == null || entity.getPaciente() == null || entity.getResponsavel() == null) {
            return null;
        }

        CuidadorResponseDto dto = new CuidadorResponseDto();

        dto.setParentesco(entity.getParentesco());

        Paciente paciente = entity.getPaciente();
        dto.setPacienteId(paciente.getId());
        dto.setPacienteNome(paciente.getNome());
        dto.setPacienteDtNascimento(paciente.getDtNascimento());
        dto.setPacienteEmail(paciente.getEmail());

        Responsavel responsavel = entity.getResponsavel();
        dto.setResponsavelId(responsavel.getId());
        dto.setResponsavelNome(responsavel.getNome());
        dto.setTelefone(responsavel.getTelefone());
        dto.setResponsavelEmail(responsavel.getEmail());

        return dto;
    }
    // SAI DTO
    // ENTRA ENTIDADE - LISTAGEM
    public static List<CuidadorResponseDto> toResponseDto(List<Cuidador> entities) {
        return entities.stream()
                .map(CuidadorMapper::toResponseDto)
                .toList();
    }
    // SAI DTO - LISTAGEM

    // ENTRA ENTIDADE
    public static CuidadorContatoResponseDto toContatoResponseDto(Cuidador entity) {
        if (entity == null || entity.getResponsavel() == null) {
            return null;
        }

        CuidadorContatoResponseDto dto = new CuidadorContatoResponseDto();

        Responsavel responsavel = entity.getResponsavel();
        dto.setNome(responsavel.getNome());
        dto.setParentesco(entity.getParentesco());
        dto.setTelefone(responsavel.getTelefone());
        dto.setEmail(responsavel.getEmail());
        dto.setEndereco(EnderecoMapper.toResponseDto(responsavel.getEndereco()));

        return dto;
    }
    // SAI CuidadorContatoResponseDto

    // ENTRA ENTIDADE - LISTAGEM
    public static List<CuidadorContatoResponseDto> toContatoResponseDto(List<Cuidador> entities) {
        return entities.stream()
                .map(CuidadorMapper::toContatoResponseDto)
                .toList();
    }
    // SAI CuidadorContatoResponseDto - LISTAGEM

    // ENTRA REQUEST DTO + ENTIDADE
    public static Cuidador updateEntityFromDto(CuidadorRequestDto dto, Cuidador entity, Paciente paciente, Responsavel responsavel) {
        entity.setPaciente(paciente);
        entity.setResponsavel(responsavel);
        entity.setParentesco(dto.getParentesco());

        return entity;
    }
    // ATUALIZA ENTIDADE EXISTENTE
}
