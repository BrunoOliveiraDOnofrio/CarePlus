package com.example.careplus.controller.dtoEspecialista;


import com.example.careplus.model.Especialista;



import java.util.List;

public class EspecialistaMapper {


    // LoginDto para usuário
    public static Especialista of(EspecialistaLoginDto especialistaLoginDto){
        Especialista especialista = new Especialista();

        especialista.setEmail(especialistaLoginDto.getEmail());
        especialista.setSenha(especialistaLoginDto.getSenha());

        return especialista;
    }

    // Usuario para o UsuarioTokenDto, além de também colocar o valor do token no dto
    public static EspecialistaTokenDto of(Especialista especialista, String token){
        EspecialistaTokenDto especialistaTokenDto = new EspecialistaTokenDto();

        especialistaTokenDto.setUserId(especialista.getId());
        especialistaTokenDto.setEmail(especialista.getEmail());
        especialistaTokenDto.setNome(especialista.getNome());
        especialistaTokenDto.setToken(token);

        return especialistaTokenDto;
    }

    public static Especialista toEntity(EspecialistaResquestDto dto, Especialista supervisor){
        if (dto == null){
            return null;
        }


        Especialista entity = new Especialista();
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setSenha(dto.getSenha());
        entity.setSupervisor(supervisor);
        entity.setCargo(dto.getCargo());
        entity.setEspecialidade(dto.getEspecialidade());


        return entity;
    }

    public static Especialista toEntity(EspecialistaResquestDto dto){
        if (dto == null){
            return null;
        }

        Especialista entity = new Especialista();
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setSenha(dto.getSenha());
        entity.setCargo(dto.getCargo());
        entity.setEspecialidade(dto.getEspecialidade());


        return entity;
    }

    public static Especialista toEntityResponse(EspecialistaResponseDto dto){
        if (dto == null){
            return null;
        }

        Especialista entity = new Especialista();
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
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
        }//else{
//            supervisorDto = new SupervisorDto(null, "Nenhum");
//        }

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


