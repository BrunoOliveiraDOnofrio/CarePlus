package com.example.careplus.dto.dtoConsultaProntuario;

import com.example.careplus.dto.dtoFuncionario.FuncionarioResponseDto;
import com.example.careplus.dto.dtoFuncionario.FuncionarioMapper;
import com.example.careplus.dto.dtoPaciente.PacienteMapper;
import com.example.careplus.dto.dtoPaciente.PacienteResponseDto;
import com.example.careplus.model.ConsultaProntuario;

import java.util.List;

public class ConsultaProntuarioMapper {

    public static ConsultaProntuario toEntity(ConsultaProntuarioRequestDto dto, PacienteResponseDto paciente, FuncionarioResponseDto funcionario){
        if (dto == null){
            return null;
        }

        ConsultaProntuario entity = new ConsultaProntuario();
        entity.setPaciente(PacienteMapper.toEntityResponse(paciente));
        entity.setFuncionario(FuncionarioMapper.toEntityResponse(funcionario));
        entity.setDataHora(dto.getDataHora());
        entity.setTipo(entity.getTipo() == null ? "Pendente" : entity.getTipo());
        return entity;
    }

    public static ConsultaProntuarioResponseDto toResponseDto(ConsultaProntuario entity){
        if (entity == null){
            return null;
        }
        return new ConsultaProntuarioResponseDto(
                entity.getId(),
                PacienteMapper.toResponseDto(entity.getPaciente()),
                FuncionarioMapper.toResponseDto(entity.getFuncionario()),
                entity.getDataHora(),
                entity.getTipo(),
                entity.getObservacoesComportamentais(),
                entity.getPresenca(),
                entity.getConfirmada()
        );
    }

    public static List<ConsultaProntuarioResponseDto> toResponseDto(List<ConsultaProntuario> entity){
        return entity.stream().map(ConsultaProntuarioMapper::toResponseDto).toList();
    }
}

