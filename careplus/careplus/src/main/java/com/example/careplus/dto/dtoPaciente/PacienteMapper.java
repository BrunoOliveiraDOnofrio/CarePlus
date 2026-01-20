package com.example.careplus.dto.dtoPaciente;

import com.example.careplus.dto.dtoEndereco.EnderecoMapper;
import com.example.careplus.model.Paciente;

import java.time.LocalDate;
import java.util.List;

public class PacienteMapper {

    public static Paciente toEntity(PacienteRequestDto dto){
        if (dto == null){
            return null;
        }

        Paciente entity = new Paciente();
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setCpf(dto.getCpf());
        entity.setTelefone(dto.getTelefone());
        entity.setDtNascimento(dto.getDtNascimento());
        entity.setConvenio(dto.getConvenio());
        entity.setDataInicio(LocalDate.now()); // define data de início automaticamente

        // Mapear endereco se presente
        if (dto.getEndereco() != null) {
            entity.setEndereco(EnderecoMapper.toEntity(dto.getEndereco()));
        }

        return entity;
    }

    public static Paciente toEntityResponse(PacienteResponseDto dto){
        if (dto == null){
            return null;
        }

        Paciente entity = new Paciente();
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setCpf(dto.getCpf());
        entity.setTelefone(dto.getTelefone());
        entity.setDtNascimento(dto.getDtNascimento());
        entity.setConvenio(dto.getConvenio());
        entity.setDataInicio(dto.getDataInicio());

        // Mapear endereco se presente
        if (dto.getEndereco() != null) {
            entity.setEndereco(EnderecoMapper.toEntity(dto.getEndereco()));
        }

        return entity;
    }

    public static PacienteResponseDto toResponseDto(Paciente entity){
        if (entity == null){
            return null;
        }

        PacienteResponseDto dto = new PacienteResponseDto(
            entity.getId(),
            entity.getNome(),
            entity.getEmail(),
            entity.getCpf(),
            entity.getTelefone(),
            entity.getDtNascimento(),
            entity.getConvenio(),
            entity.getDataInicio(),
            entity.getEndereco() != null ? EnderecoMapper.toResponseDto(entity.getEndereco()) : null
        );

        return dto;
    }

    public static List<PacienteResponseDto> toResponseDto(List<Paciente> entity){
        return entity.stream().map(PacienteMapper::toResponseDto).toList();
    }


}
