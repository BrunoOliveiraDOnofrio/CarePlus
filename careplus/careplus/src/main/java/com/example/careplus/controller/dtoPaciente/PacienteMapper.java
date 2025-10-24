package com.example.careplus.controller.dtoPaciente;

import com.example.careplus.model.Paciente;

import java.time.LocalDate;
import java.util.List;

public class PacienteMapper {

    public static Paciente toEntity(PacienteRequestDto dto){
        if (dto == null){
            return null;
        }

//        private Long id;
//        private String nome;
//        private String email;
//        private String cpf;
//        private String cargo;
//        private String telefone;
//        private String senha;
//        private LocalDate dtNascimento;


        Paciente entity = new Paciente();
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setCpf(dto.getCpf());
        entity.setTelefone(dto.getTelefone());
        entity.setSenha(dto.getSenha());
        entity.setDtNascimento(dto.getDtNascimento());

        return entity;
    }

    public static Paciente toEntityResponse(PacienteResponseDto dto){
        if (dto == null){
            return null;
        }

//        private Long id;
//        private String nome;
//        private String email;
//        private String cpf;
//        private String cargo;
//        private String telefone;
//        private String senha;
//        private LocalDate dtNascimento;


        Paciente entity = new Paciente();
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setCpf(dto.getCpf());
        entity.setTelefone(dto.getTelefone());
        entity.setDtNascimento(dto.getDtNascimento());

        return entity;
    }




    public static PacienteResponseDto toResponseDto(Paciente entity){
        if (entity == null){
            return null;
        }

        PacienteResponseDto dto = new PacienteResponseDto(entity.getId(), entity.getNome(), entity.getEmail(), entity.getCpf(), entity.getTelefone(), entity.getDtNascimento());

        return dto;
    }

    public static List<PacienteResponseDto> toResponseDto(List<Paciente> entity){
        return entity.stream().map(PacienteMapper::toResponseDto).toList();
    }


}
