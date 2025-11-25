package com.example.careplus.controller.dtoConsulta;

import com.example.careplus.controller.dtoFuncionario.FuncionarioResponseDto;
import com.example.careplus.controller.dtoFuncionario.FuncionarioMapper;
import com.example.careplus.controller.dtoPaciente.PacienteMapper;
import com.example.careplus.controller.dtoPaciente.PacienteResponseDto;
import com.example.careplus.model.Consulta;

import java.util.List;


public class ConsultaMapper {

    public static Consulta toEntity(ConsultaRequestDto dto, PacienteResponseDto paciente, FuncionarioResponseDto funcionario){
        if (dto == null){
            return null;
        }

        Consulta entity = new Consulta();
        entity.setPaciente(PacienteMapper.toEntityResponse(paciente));
        entity.setFuncionario(FuncionarioMapper.toEntityResponse(funcionario));
        entity.setDataHora(dto.getDataHora());
        entity.setConfirmada(dto.getConfirmada());
        // Tipo inicial padrão se não informado
        entity.setTipo(entity.getTipo() == null ? "Pendente" : entity.getTipo());
        return entity;
    }

    public static ConsultaResponseDto toResponseDto(Consulta entity){
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

    public static List<ConsultaResponseDto> toResponseDto(List<Consulta> entity){
        return entity.stream().map(ConsultaMapper::toResponseDto).toList();
    }
}
