package com.example.careplus.dto.dtoFuncionario;


import com.example.careplus.model.Funcionario;



import java.util.List;

public class FuncionarioMapper {


    // LoginDto para usuário
    public static Funcionario of(FuncionarioLoginDto funcionarioLoginDto){
        Funcionario funcionario = new Funcionario();

        funcionario.setEmail(funcionarioLoginDto.getEmail());
        funcionario.setSenha(funcionarioLoginDto.getSenha());

        return funcionario;
    }

    // Usuario para o UsuarioTokenDto, além de também colocar o valor do token no dto
    public static FuncionarioTokenDto of(Funcionario funcionario, String token){
        FuncionarioTokenDto funcionarioTokenDto = new FuncionarioTokenDto();

        funcionarioTokenDto.setUserId(funcionario.getId());
        funcionarioTokenDto.setEmail(funcionario.getEmail());
        funcionarioTokenDto.setNome(funcionario.getNome());
        funcionarioTokenDto.setToken(token);

        return funcionarioTokenDto;
    }

    public static Funcionario toEntity(FuncionarioResquestDto dto, Funcionario supervisor){
        if (dto == null){
            return null;
        }


        Funcionario entity = new Funcionario();
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setSenha(dto.getSenha());
        entity.setSupervisor(supervisor);
        entity.setCargo(dto.getCargo());
        entity.setEspecialidade(dto.getEspecialidade());
        entity.setTipoAtendimento(dto.getTipoAtendimento());


        return entity;
    }

    public static Funcionario toEntity(FuncionarioResquestDto dto){
        if (dto == null){
            return null;
        }

        Funcionario entity = new Funcionario();
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setSenha(dto.getSenha());
        entity.setCargo(dto.getCargo());
        entity.setEspecialidade(dto.getEspecialidade());
        entity.setTipoAtendimento(dto.getTipoAtendimento());


        return entity;
    }

    public static Funcionario toEntityResponse(FuncionarioResponseDto dto){
        if (dto == null){
            return null;
        }

        Funcionario entity = new Funcionario();
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setCargo(dto.getCargo());
        entity.setEspecialidade(dto.getEspecialidade());
        entity.setTipoAtendimento(dto.getTipoAtendimento());


        return entity;
    }

    public static FuncionarioResponseDto toResponseDto(Funcionario entity){
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

        return new FuncionarioResponseDto(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                supervisorDto,
                entity.getCargo(),
                entity.getEspecialidade(),
                entity.getTipoAtendimento()
        );
    }

    public static List<FuncionarioResponseDto> toResponseDto(List<Funcionario> entity){
        return entity.stream().map(FuncionarioMapper::toResponseDto).toList();
    }

}
