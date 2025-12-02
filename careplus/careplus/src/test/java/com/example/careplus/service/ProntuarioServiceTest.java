package com.example.careplus.service;

import com.example.careplus.controller.dtoProntuario.ProntuarioRequestDto;
import com.example.careplus.model.Paciente;
import com.example.careplus.model.Prontuario;
import com.example.careplus.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProntuarioServiceTest {

    @Mock
    private ProntuarioRepository prontuatioRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private ClassificacaoDoencasRepository cidRepository;

    @Mock
    private TratamentoRepository tratamentoRepository;

    @Mock
    private MedicacaoRepository medicacaoRepository;

    @InjectMocks
    private ProntuarioService service;

    @Test
    @DisplayName("Quando a tabela de prontuarios estiver vazia deve retornar lista vazia")
    void deveRetornarListaVazia() {
        List<Prontuario> esperado = new ArrayList<>();
        Mockito.when(prontuatioRepository.findAll()).thenReturn(esperado);

        List<Prontuario> recebido = service.listarProntuarios();

        assertNotNull(recebido);
        assertTrue(recebido.isEmpty());
    }

    @Test
    @DisplayName("Quando houver prontuarios deve retornar lista preenchida e popular relacionamentos")
    void deveRetornarListaCheia() {
        Prontuario p = new Prontuario();
        p.setId(1L);
        List<Prontuario> esperado = List.of(p);

        when(prontuatioRepository.findAll()).thenReturn(esperado);
        when(cidRepository.findByProntuario_Id(1L)).thenReturn(new ArrayList<>());
        when(tratamentoRepository.findByProntuario_Id(1L)).thenReturn(new ArrayList<>());
        when(medicacaoRepository.findByProntuario_Id(1L)).thenReturn(new ArrayList<>());

        List<Prontuario> recebido = service.listarProntuarios();

        assertEquals(esperado.size(), recebido.size());
        assertEquals(1L, recebido.get(0).getId());

    }

    @Test
    @DisplayName("Criar prontuario com paciente existente deve salvar e retornar")
    void deveCriarProntuarioQuandoPacienteExiste() {
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        ProntuarioRequestDto dto = Mockito.mock(ProntuarioRequestDto.class);
        when(dto.getIdPaciente()).thenReturn(1L);
        when(dto.getDesfraldado()).thenReturn(Boolean.FALSE);
        when(dto.getHiperfoco()).thenReturn("dinossauro");
        when(dto.getAnamnese()).thenReturn("anam");
        when(dto.getDiagnostico()).thenReturn("diag");
        when(dto.getResumoClinico()).thenReturn("res");
        when(dto.getNivelAgressividade()).thenReturn(1);

        Prontuario salvo = new Prontuario();
        salvo.setId(10L);
        when(prontuatioRepository.save(any(Prontuario.class))).thenReturn(salvo);

        Prontuario recebido = service.criarProntuario(dto);

        assertNotNull(recebido);
        assertEquals(10L, recebido.getId());

    }

    @Test
    @DisplayName("Buscar prontuario por id existente retorna prontuario")
    void buscarProntuarioPorIdExistente() {
        Prontuario p = new Prontuario();

        p.setId(5L);
        when(prontuatioRepository.findById(5L)).thenReturn(Optional.of(p));

        Prontuario recebido = service.buscarProntuarioPorId(5L);

        assertEquals(5L, recebido.getId());
    }

    @Test
    @DisplayName("Buscar prontuario por id inexistente lança RuntimeException")
    void buscarProntuarioPorIdInexistente() {
        when(prontuatioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.buscarProntuarioPorId(99L));
    }

    @Test
    @DisplayName("Atualizar prontuario existente salva alterações")
    void atualizarProntuarioExistente() {
        Prontuario existente = new Prontuario();
        existente.setId(7L);
        when(prontuatioRepository.findById(7L)).thenReturn(Optional.of(existente));

        ProntuarioRequestDto dto = Mockito.mock(ProntuarioRequestDto.class);
        when(dto.getDesfraldado()).thenReturn(Boolean.TRUE);
        when(dto.getHiperfoco()).thenReturn("dinossauro");
        when(dto.getAnamnese()).thenReturn("novo");
        when(dto.getDiagnostico()).thenReturn("novoDiag");
        when(dto.getResumoClinico()).thenReturn("resumo");
        when(dto.getNivelAgressividade()).thenReturn(2);

        when(prontuatioRepository.save(any(Prontuario.class))).thenAnswer(i -> i.getArgument(0));

        Prontuario atualizado = service.atualizarProntuario(dto, 7L);

        assertNotNull(atualizado);
        assertEquals(Boolean.TRUE, atualizado.getDesfraldado());

    }

    @Test
    @DisplayName("Atualizar prontuario inexistente lança RuntimeException")
    void atualizarProntuarioInexistente() {
        when(prontuatioRepository.findById(123L)).thenReturn(Optional.empty());
        ProntuarioRequestDto dto = Mockito.mock(ProntuarioRequestDto.class);

        assertThrows(RuntimeException.class, () -> service.atualizarProntuario(dto, 123L));
    }

    @Test
    @DisplayName("Deletar por id inexistente lança NoSuchElementException")
    void deletarPorIdInexistente() {
        when(prontuatioRepository.existsById(100L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> service.deletarPorId(100L));
    }

    @Test
    @DisplayName("Deletar por id existente remove o prontuario")
    void deletarPorIdExistente() {
        when(prontuatioRepository.existsById(2L)).thenReturn(true);

        service.deletarPorId(2L);


    }

    @Test
    @DisplayName("Buscar por nome retorna prontuario quando encontrado")
    void buscarPorNomeQuandoEncontrado() {
        Prontuario p = new Prontuario();
        when(prontuatioRepository.findByPacienteNomeContainsIgnoreCase("Maria")).thenReturn(Optional.of(p));

        Prontuario recebido = service.buscarProntuarioPorNome("Maria");

        assertNotNull(recebido);
    }

    @Test
    @DisplayName("Buscar por cpf retorna prontuario quando encontrado")
    void buscarPorCpfQuandoEncontrado() {
        Prontuario p = new Prontuario();
        when(prontuatioRepository.findByPacienteCpf("12345678900")).thenReturn(Optional.of(p));

        Prontuario recebido = service.buscarProntuarioPorCpf("12345678900");

        assertNotNull(recebido);
    }
}