package com.example.careplus.controller.dtoEspecialista;


import com.example.careplus.model.Especialista;



import java.util.List;

public class EspecialistaMapper {

    public static Especialista toEntity(EspecialistaResquestDto dto, Especialista supervisor){
        if (dto == null){
            return null;
        }

//        private String nome;
//        private String email;
//        private String senha;
//        private Especialista supervisor;
//        private String cargo;
//        private String especialidade;


        Especialista entity = new Especialista();
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setSenha(dto.getSenha());
        entity.setSupervisor(supervisor);
        entity.setCargo(dto.getCargo());
        entity.setEspecialidade(dto.getEspecialidade());


        return entity;
    }

    public static EspecialistaResponseDto toResponseDto(Especialista entity){
        if (entity == null){
            return null;
        }

        SupervisorDto supervisorDto = null;

        if (entity.getSupervisor() != null) {
            supervisorDto = new SupervisorDto(
                    entity.getSupervisor().getId(),
                    entity.getSupervisor().getNome()
            );
        }

        return new EspecialistaResponseDto(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                supervisorDto,
                entity.getCargo(),
                entity.getEspecialidade()
        );
    }

    public static List<EspecialistaResponseDto> toResponseDto(List<Especialista> entity){
        return entity.stream().map(EspecialistaMapper::toResponseDto).toList();
    }

}


