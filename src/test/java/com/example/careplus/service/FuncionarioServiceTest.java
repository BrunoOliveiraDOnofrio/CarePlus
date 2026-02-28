package com.example.careplus.service;

import com.example.careplus.dto.dtoFuncionario.FuncionarioResponseDto;
import com.example.careplus.dto.dtoFuncionario.FuncionarioResquestDto;
import com.example.careplus.model.Funcionario;
import com.example.careplus.repository.FuncionarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class FuncionarioServiceTest {

    @InjectMocks
    FuncionarioService funcionarioService;

    @Mock
    FuncionarioRepository funcionarioRepository;

    @Mock
    PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("Deve retornar todos os funcionários")
    void buscarTodos() {
        Funcionario funcionario1 = new Funcionario();
        funcionario1.setNome("João Silva");
        funcionario1.setEmail("joao.silva@example.com");
        funcionario1.setCargo("Desenvolvedor");

        Funcionario funcionario2 = new Funcionario();
        funcionario2.setNome("Maria Oliveira");
        funcionario2.setEmail("maria.oliveira@example.com");
        funcionario2.setCargo("Analista");

        List<Funcionario> funcionarios = List.of(funcionario1, funcionario2);
        Mockito.when(funcionarioRepository.findAll()).thenReturn(funcionarios);

        List<FuncionarioResponseDto> recebido = funcionarioService.listarTodos();

        Assertions.assertEquals(2, recebido.size());
    }

    @Test
    void salvarSemSupervisor() {
        FuncionarioResquestDto dto = new FuncionarioResquestDto(
                "João Silva",
                "joao.silva@example.com",
                "123",
                null,
                "Estagiario",
                "Fonoaudiologo",
                "(11)98765-4321",
                "123456789",
                "ABA"
        );

        Funcionario funcionarioSalvo = new Funcionario();
        funcionarioSalvo.setId(1L);
        funcionarioSalvo.setNome("João Silva");
        funcionarioSalvo.setEmail("joao.silva@example.com");
        funcionarioSalvo.setSenha("senhaCriptografada");
        funcionarioSalvo.setCargo("Estagiario");
        funcionarioSalvo.setEspecialidade("Fonoaudiologo");
        funcionarioSalvo.setTipoAtendimento("ABA");

        Mockito.when(passwordEncoder.encode("123"))
                .thenReturn("senhaCriptografada");

        Mockito.when(funcionarioRepository.save(Mockito.any(Funcionario.class)))
                .thenReturn(funcionarioSalvo);

        FuncionarioResponseDto resultado = funcionarioService.salvar(dto);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("João Silva", resultado.getNome());
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode("123");

        Mockito.verify(funcionarioRepository, Mockito.times(1)).save(Mockito.any(Funcionario.class));


    }

    @Test
    void listarTodos() {
        Funcionario funcionario1 = new Funcionario();
        funcionario1.setNome("João Silva");
        funcionario1.setEmail("joao.silva@example.com");
        funcionario1.setCargo("Clinico geral");

        Funcionario funcionario2 = new Funcionario();
        funcionario2.setNome("Maria Oliveira");
        funcionario2.setEmail("maria.oliveira@example.com");
        funcionario2.setCargo("Pscicologa");

        Mockito.when(funcionarioRepository.findByEmailContainingIgnoreCase("joao.silva@example.com")).thenReturn(List.of(funcionario1));
        List<FuncionarioResponseDto> recebido = funcionarioService.buscarPorEmail(funcionario1.getEmail());

        Assertions.assertEquals(1, recebido.size());
    }


    @Test
    void deletar() {
        Funcionario funcionario1 = new Funcionario();
        funcionario1.setId(1L);
        funcionario1.setNome("João Silva");
        funcionario1.setEmail("joao.silva@example.com");
        funcionario1.setCargo("Fonoaudiologa");

        Funcionario funcionario2 = new Funcionario();
        funcionario2.setNome("Maria Oliveira");
        funcionario2.setEmail("maria.oliveira@example.com");
        funcionario2.setCargo("Recepcionista");

        Mockito.when(funcionarioRepository.existsById(funcionario1.getId())).thenReturn(true);

        Mockito.when(funcionarioRepository.findAll()).thenReturn(List.of(funcionario1, funcionario2));

        List<Funcionario> antesDelete = funcionarioRepository.findAll();
        Assertions.assertEquals(2, antesDelete.size());

        funcionarioService.deletar(1L);

        Mockito.verify(funcionarioRepository, Mockito.times(1)).deleteById(1L);

        Mockito.when(funcionarioRepository.findAll()).thenReturn(List.of());
        List<Funcionario> depoisDelete = funcionarioRepository.findAll();
        Assertions.assertTrue(depoisDelete.isEmpty());
    }

    @Test
    void atualizar() {

        Long funcionarioId = 1L;
        FuncionarioResquestDto dtoAtualizado = new FuncionarioResquestDto(
                "João Silva Atualizado",
                "joao.atualizado@example.com",
                "123", // senha não é atualizada neste método
                null,
                "Gerente",
                "Cardiologia",
                "(11)98765-4321",
                "987654321",
                "Fono"
        );

        Funcionario funcionarioExistente = new Funcionario();
        funcionarioExistente.setId(funcionarioId);
        funcionarioExistente.setNome("João Silva");
        funcionarioExistente.setEmail("joao.silva@example.com");
        funcionarioExistente.setCargo("Estagiario");
        funcionarioExistente.setEspecialidade("Fonoaudiologo");
        funcionarioExistente.setTipoAtendimento("ABA");


        Funcionario funcionarioAtualizado = new Funcionario();
        funcionarioAtualizado.setId(funcionarioId);
        funcionarioAtualizado.setNome("João Silva Atualizado");
        funcionarioAtualizado.setEmail("joao.atualizado@example.com");
        funcionarioAtualizado.setCargo("Gerente");
        funcionarioAtualizado.setEspecialidade("Cardiologia");
        funcionarioAtualizado.setTipoAtendimento("Fono");

        // Mocks
        Mockito.when(funcionarioRepository.findById(funcionarioId))
                .thenReturn(Optional.of(funcionarioExistente));

        Mockito.when(funcionarioRepository.save(Mockito.any(Funcionario.class)))
                .thenReturn(funcionarioAtualizado);

        // Act
        FuncionarioResponseDto resultado = funcionarioService.atualizarFuncionario(dtoAtualizado, funcionarioId);

        // Assert
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("João Silva Atualizado", resultado.getNome());
        Assertions.assertEquals("joao.atualizado@example.com", resultado.getEmail());
        Assertions.assertEquals("Gerente", resultado.getCargo());
        Assertions.assertEquals("Cardiologia", resultado.getEspecialidade());

        Mockito.verify(funcionarioRepository, Mockito.times(1)).findById(funcionarioId);
        Mockito.verify(funcionarioRepository, Mockito.times(1)).save(Mockito.any(Funcionario.class));
    }
}