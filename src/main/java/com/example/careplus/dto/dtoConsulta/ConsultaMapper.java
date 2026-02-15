package com.example.careplus.dto.dtoConsulta;

import com.example.careplus.dto.dtoFuncionario.FuncionarioResponseDto;
import com.example.careplus.dto.dtoFuncionario.FuncionarioMapper;
import com.example.careplus.dto.dtoPaciente.PacienteMapper;
import com.example.careplus.dto.dtoPaciente.PacienteResponseDto;
import com.example.careplus.model.ConsultaProntuario;

import java.util.List;


public class ConsultaMapper {

    public static ConsultaProntuario toEntity(ConsultaRequestDto dto, PacienteResponseDto paciente, FuncionarioResponseDto funcionario){
        if (dto == null){
            return null;
        }

        ConsultaProntuario entity = new ConsultaProntuario();
        entity.setPaciente(PacienteMapper.toEntityResponse(paciente));
        entity.setFuncionario(FuncionarioMapper.toEntityResponse(funcionario));
        entity.setDataHora(dto.getDataHora());
        // Tipo inicial padrão se não informado
        entity.setTipo(entity.getTipo() == null ? "Pendente" : entity.getTipo());
        return entity;
    }

    public static ConsultaResponseDto toResponseDto(ConsultaProntuario entity){
        if (entity == null){
            return null;
        }
        return new ConsultaResponseDto(
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

    public static List<ConsultaResponseDto> toResponseDto(List<ConsultaProntuario> entity){
        return entity.stream().map(ConsultaMapper::toResponseDto).toList();
    }
}
