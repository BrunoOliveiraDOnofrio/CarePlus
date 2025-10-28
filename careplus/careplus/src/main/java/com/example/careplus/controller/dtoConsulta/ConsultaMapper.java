package com.example.careplus.controller.dtoConsulta;

import com.example.careplus.controller.dtoFuncionario.FuncionarioMapper;
import com.example.careplus.controller.dtoFuncionario.FuncionarioResponseDto;
import com.example.careplus.controller.dtoPaciente.PacienteMapper;
import com.example.careplus.controller.dtoPaciente.PacienteResponseDto;
import com.example.careplus.model.Consulta;
import com.example.careplus.model.Funcionario;
import com.example.careplus.model.Paciente;
import org.apache.commons.lang3.concurrent.ConcurrentUtils;

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
        return entity;

    }

    public static ConsultaResponseDto toResponseDto(Consulta entity){
        if (entity == null){
            return null;
        }



        return new ConsultaResponseDto(entity.getId(),PacienteMapper.toResponseDto(entity.getPaciente()), FuncionarioMapper.toResponseDto(entity.getFuncionario()), entity.getDataHora(), null, null, null);

    }

    public static List<ConsultaResponseDto> toResponseDto(List<Consulta> entity){
        return entity.stream().map(ConsultaMapper::toResponseDto).toList();
    }



}
