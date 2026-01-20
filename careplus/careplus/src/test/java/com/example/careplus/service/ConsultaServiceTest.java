package com.example.careplus.service;

import com.example.careplus.dto.dtoConsulta.ConsultaRequestDto;
import com.example.careplus.dto.dtoConsulta.ConsultaResponseDto;
import com.example.careplus.exception.ResourceNotFoundException;
import com.example.careplus.model.Consulta;
import com.example.careplus.dto.dtoConsulta.ConsultaRequest;
import com.example.careplus.model.Funcionario;
import com.example.careplus.model.Paciente;
import com.example.careplus.repository.ConsultaRepository;
import com.example.careplus.repository.FuncionarioRepository;
import com.example.careplus.repository.PacienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ConsultaService service;

    // ==================== TESTES DE MARCAR CONSULTA ====================

    @Test
    @DisplayName("Quando marcar consulta com dados válidos deve retornar ConsultaResponseDto")
    void deveMarcarConsultaComSucesso() {
        // Arrange
        Long pacienteId = 1L;
        Long funcionarioId = 2L;
        LocalDateTime dataHora = LocalDateTime.now().plusDays(1);

        ConsultaRequestDto requestDto = new ConsultaRequestDto(pacienteId, funcionarioId, dataHora, "Pendente");

        Paciente paciente = new Paciente();
        paciente.setId(pacienteId);
        paciente.setNome("João Silva");

        Funcionario funcionario = new Funcionario();
        funcionario.setId(funcionarioId);
        funcionario.setNome("Dr. Maria");

        Consulta consultaSalva = new Consulta();
        consultaSalva.setId(1L);
        consultaSalva.setPaciente(paciente);
        consultaSalva.setFuncionario(funcionario);
        consultaSalva.setDataHora(dataHora);
        consultaSalva.setConfirmada(false);
        consultaSalva.setTipo("Pendente");

        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(paciente));
        when(funcionarioRepository.findById(funcionarioId)).thenReturn(Optional.of(funcionario));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consultaSalva);
        doNothing().when(emailService).EnviarNotificacao(any(), any(), any());

        // Act
        ConsultaResponseDto resultado = service.marcarConsulta(requestDto);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertNotNull(resultado.getPaciente());
        assertNotNull(resultado.getFuncionario());
        assertEquals(dataHora, resultado.getDataHora());
        assertEquals("Pendente", resultado.getTipo());
        assertEquals(false, resultado.getConfirmada());

        verify(pacienteRepository, times(1)).findById(pacienteId);
        verify(funcionarioRepository, times(1)).findById(funcionarioId);
        verify(consultaRepository, times(1)).save(any(Consulta.class));
        verify(emailService, times(1)).EnviarNotificacao(any(), any(), any());
    }

    @Test
    @DisplayName("Quando marcar consulta com confirmada null deve definir como false")
    void deveDefinirConfirmadaComoFalseQuandoNull() {
        // Arrange
        Long pacienteId = 1L;
        Long funcionarioId = 2L;
        LocalDateTime dataHora = LocalDateTime.now().plusDays(1);

        ConsultaRequestDto requestDto = new ConsultaRequestDto(pacienteId, funcionarioId, dataHora, null);

        Paciente paciente = new Paciente();
        paciente.setId(pacienteId);

        Funcionario funcionario = new Funcionario();
        funcionario.setId(funcionarioId);

        Consulta consultaSalva = new Consulta();
        consultaSalva.setId(1L);
        consultaSalva.setConfirmada(false);
        consultaSalva.setTipo("Pendente");

        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(paciente));
        when(funcionarioRepository.findById(funcionarioId)).thenReturn(Optional.of(funcionario));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consultaSalva);
        doNothing().when(emailService).EnviarNotificacao(any(), any(), any());

        // Act
        ConsultaResponseDto resultado = service.marcarConsulta(requestDto);

        // Assert
        assertNotNull(resultado);
        assertEquals(false, resultado.getConfirmada());
    }

    @Test
    @DisplayName("Quando marcar consulta com paciente inexistente deve lançar ResourceNotFoundException")
    void deveLancarExcecaoQuandoPacienteNaoExiste() {
        // Arrange
        Long pacienteId = 999L;
        Long funcionarioId = 2L;
        LocalDateTime dataHora = LocalDateTime.now().plusDays(1);

        ConsultaRequestDto requestDto = new ConsultaRequestDto(pacienteId, funcionarioId, dataHora, "Pendente");

        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.marcarConsulta(requestDto)
        );

        assertEquals("Usuário não encontrado!", exception.getMessage());
        verify(pacienteRepository, times(1)).findById(pacienteId);
        verify(funcionarioRepository, never()).findById(anyLong());
        verify(consultaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Quando marcar consulta com funcionario inexistente deve lançar ResourceNotFoundException")
    void deveLancarExcecaoQuandoFuncionarioNaoExiste() {
        Long pacienteId = 1L;
        Long funcionarioId = 999L;
        LocalDateTime dataHora = LocalDateTime.now().plusDays(1);

        ConsultaRequestDto requestDto = new ConsultaRequestDto(pacienteId, funcionarioId, dataHora,  "Pendente");

        Paciente paciente = new Paciente();
        paciente.setId(pacienteId);

        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(paciente));
        when(funcionarioRepository.findById(funcionarioId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.marcarConsulta(requestDto)
        );

        assertEquals("Funcionario não encontrado!", exception.getMessage());
        verify(pacienteRepository, times(1)).findById(pacienteId);
        verify(funcionarioRepository, times(1)).findById(funcionarioId);
        verify(consultaRepository, never()).save(any());
    }

    // ==================== TESTES DE REMOVER CONSULTA ====================

    @Test
    @DisplayName("Quando remover consulta existente deve deletar com sucesso")
    void deveRemoverConsultaComSucesso() {
        // Arrange
        Long consultaId = 1L;

        when(consultaRepository.existsById(consultaId)).thenReturn(true);
        doNothing().when(consultaRepository).deleteById(consultaId);

        // Act
        service.removerConsulta(consultaId);

        // Assert
        verify(consultaRepository, times(1)).existsById(consultaId);
        verify(consultaRepository, times(1)).deleteById(consultaId);
    }

    @Test
    @DisplayName("Quando remover consulta inexistente deve lançar RuntimeException")
    void deveLancarExcecaoAoRemoverConsultaInexistente() {
        // Arrange
        Long consultaId = 999L;

        when(consultaRepository.existsById(consultaId)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.removerConsulta(consultaId)
        );

        assertEquals("Consulta não encontrada", exception.getMessage());
        verify(consultaRepository, times(1)).existsById(consultaId);
        verify(consultaRepository, never()).deleteById(anyLong());
    }

    // ==================== TESTES DE LISTAR CONSULTAS ====================

    @Test
    @DisplayName("Quando listar consultas e existirem registros deve retornar lista")
    void deveListarConsultasQuandoExistirem() {
        Consulta consulta1 = new Consulta();
        consulta1.setId(1L);
        consulta1.setTipo("Pendente");

        Consulta consulta2 = new Consulta();
        consulta2.setId(2L);
        consulta2.setTipo("Retorno");

        List<Consulta> consultas = List.of(consulta1, consulta2);

        when(consultaRepository.findAll()).thenReturn(consultas);

        // Act
        List<Consulta> resultado = service.listarConsultas();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(consultaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Quando listar consultas e não existirem registros deve retornar lista vazia")
    void deveRetornarListaVaziaQuandoNaoExistiremConsultas() {
        // Arrange
        List<Consulta> consultasVazias = new ArrayList<>();

        when(consultaRepository.findAll()).thenReturn(consultasVazias);

        // Act
        List<Consulta> resultado = service.listarConsultas();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(consultaRepository, times(1)).findAll();
    }

    // ==================== TESTES DE LISTAR POR DATA ====================

    @Test
    @DisplayName("Quando listar por data e existirem consultas deve retornar lista de DTOs")
    void deveListarPorDataQuandoExistiremConsultas() {
        // Arrange
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNome("João");

        Funcionario funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome("Dr. Maria");

        Consulta consulta = new Consulta();
        consulta.setId(1L);
        consulta.setPaciente(paciente);
        consulta.setFuncionario(funcionario);
        consulta.setDataHora(LocalDateTime.now());
        consulta.setTipo("Pendente");

        List<Consulta> consultas = List.of(consulta);

        when(consultaRepository.buscarPorData()).thenReturn(consultas);

        // Act
        List<ConsultaResponseDto> resultado = service.listarPorData();

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(consultaRepository, times(1)).buscarPorData();
    }

    @Test
    @DisplayName("Quando listar por data e não existirem consultas deve lançar ResourceNotFoundException")
    void deveLancarExcecaoQuandoNaoExistiremConsultasPorData() {
        // Arrange
        List<Consulta> consultasVazias = new ArrayList<>();

        when(consultaRepository.buscarPorData()).thenReturn(consultasVazias);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.listarPorData()
        );

        assertEquals("Nenhuma consulta cadastrada!", exception.getMessage());
        verify(consultaRepository, times(1)).buscarPorData();
    }

    // ==================== TESTES DE LISTAR POR PACIENTE ====================

    @Test
    @DisplayName("Quando listar por paciente e existirem consultas deve retornar lista de DTOs")
    void deveListarPorPacienteQuandoExistiremConsultas() {
        // Arrange
        Long pacienteId = 1L;

        Paciente paciente = new Paciente();
        paciente.setId(pacienteId);
        paciente.setNome("João");

        Funcionario funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome("Dr. Maria");

        Consulta consulta = new Consulta();
        consulta.setId(1L);
        consulta.setPaciente(paciente);
        consulta.setFuncionario(funcionario);
        consulta.setDataHora(LocalDateTime.now());

        List<Consulta> consultas = List.of(consulta);

        when(consultaRepository.buscarPorPaciente(pacienteId)).thenReturn(consultas);

        // Act
        List<ConsultaResponseDto> resultado = service.listarPorPaciente(pacienteId);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(consultaRepository, times(1)).buscarPorPaciente(pacienteId);
    }

    @Test
    @DisplayName("Quando listar por paciente inexistente deve lançar ResourceNotFoundException")
    void deveLancarExcecaoQuandoNaoExistiremConsultasDoPaciente() {
        // Arrange
        Long pacienteId = 999L;
        List<Consulta> consultasVazias = new ArrayList<>();

        when(consultaRepository.buscarPorPaciente(pacienteId)).thenReturn(consultasVazias);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.listarPorPaciente(pacienteId)
        );

        assertEquals("Nenhuma consulta cadastrada para esse paciente!", exception.getMessage());
        verify(consultaRepository, times(1)).buscarPorPaciente(pacienteId);
    }

    // ==================== TESTES DE EDITAR CONSULTA ====================

    @Test
    @DisplayName("Quando editar consulta com dados válidos deve retornar ConsultaResponseDto atualizado")
    void deveEditarConsultaComSucesso() {
        // Arrange
        Long consultaId = 1L;
        Long pacienteId = 1L;
        Long funcionarioId = 2L;
        LocalDateTime novaDataHora = LocalDateTime.now().plusDays(2);

        ConsultaRequest request = new ConsultaRequest(pacienteId, funcionarioId, novaDataHora);

        Paciente paciente = new Paciente();
        paciente.setId(pacienteId);
        paciente.setNome("João");

        Funcionario funcionario = new Funcionario();
        funcionario.setId(funcionarioId);
        funcionario.setNome("Dr. Maria");

        Consulta consultaExistente = new Consulta();
        consultaExistente.setId(consultaId);
        consultaExistente.setPaciente(paciente);
        consultaExistente.setFuncionario(funcionario);
        consultaExistente.setDataHora(LocalDateTime.now());
        consultaExistente.setConfirmada(true);
        consultaExistente.setTipo("Pendente");

        Consulta consultaAtualizada = new Consulta();
        consultaAtualizada.setId(consultaId);
        consultaAtualizada.setPaciente(paciente);
        consultaAtualizada.setFuncionario(funcionario);
        consultaAtualizada.setDataHora(novaDataHora);
        consultaAtualizada.setConfirmada(true);
        consultaAtualizada.setTipo("Retorno");

        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(paciente));
        when(funcionarioRepository.findById(funcionarioId)).thenReturn(Optional.of(funcionario));
        when(consultaRepository.findById(consultaId)).thenReturn(Optional.of(consultaExistente));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consultaAtualizada);

        // Act
        ConsultaResponseDto resultado = service.editarConsulta(consultaId, request);

        // Assert
        assertNotNull(resultado);
        assertEquals(consultaId, resultado.getId());
        assertEquals("Retorno", resultado.getTipo());
        assertEquals(novaDataHora, resultado.getDataHora());
        assertEquals(true, resultado.getConfirmada());

        verify(pacienteRepository, times(1)).findById(pacienteId);
        verify(funcionarioRepository, times(1)).findById(funcionarioId);
        verify(consultaRepository, times(1)).findById(consultaId);
        verify(consultaRepository, times(1)).save(any(Consulta.class));
    }

    @Test
    @DisplayName("Quando editar consulta com paciente inexistente deve lançar ResourceNotFoundException")
    void deveLancarExcecaoAoEditarComPacienteInexistente() {
        // Arrange
        Long consultaId = 1L;
        Long pacienteId = 999L;
        Long funcionarioId = 2L;

        ConsultaRequest request = new ConsultaRequest(pacienteId, funcionarioId, LocalDateTime.now());

        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.editarConsulta(consultaId, request)
        );

        assertEquals("Usuário não encontrado!", exception.getMessage());
        verify(pacienteRepository, times(1)).findById(pacienteId);
        verify(consultaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Quando editar consulta inexistente deve lançar ResourceNotFoundException")
    void deveLancarExcecaoAoEditarConsultaInexistente() {
        // Arrange
        Long consultaId = 999L;
        Long pacienteId = 1L;
        Long funcionarioId = 2L;

        ConsultaRequest request = new ConsultaRequest(pacienteId, funcionarioId, LocalDateTime.now());

        Paciente paciente = new Paciente();
        paciente.setId(pacienteId);

        Funcionario funcionario = new Funcionario();
        funcionario.setId(funcionarioId);

        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(paciente));
        when(funcionarioRepository.findById(funcionarioId)).thenReturn(Optional.of(funcionario));
        when(consultaRepository.findById(consultaId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.editarConsulta(consultaId, request)
        );

        assertEquals("Consulta não encontrada", exception.getMessage());
        verify(consultaRepository, times(1)).findById(consultaId);
        verify(consultaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Quando editar consulta com confirmada null deve manter false")
    void deveManterConfirmadaComoFalseNaEdicao() {
        // Arrange
        Long consultaId = 1L;
        Long pacienteId = 1L;
        Long funcionarioId = 2L;

        ConsultaRequest request = new ConsultaRequest(pacienteId, funcionarioId, LocalDateTime.now());

        Paciente paciente = new Paciente();
        paciente.setId(pacienteId);

        Funcionario funcionario = new Funcionario();
        funcionario.setId(funcionarioId);

        Consulta consultaExistente = new Consulta();
        consultaExistente.setId(consultaId);
        consultaExistente.setConfirmada(null); // null na consulta existente

        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(paciente));
        when(funcionarioRepository.findById(funcionarioId)).thenReturn(Optional.of(funcionario));
        when(consultaRepository.findById(consultaId)).thenReturn(Optional.of(consultaExistente));
        when(consultaRepository.save(any(Consulta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        service.editarConsulta(consultaId, request);

        // Assert
        verify(consultaRepository, times(1)).save(any(Consulta.class));
        // A lógica do service deve ter setado confirmada como false quando era null
    }
}

