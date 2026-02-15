package com.example.careplus.service;

import com.example.careplus.dto.dtoFichaClinica.FichaClinicaRequestDto;
import com.example.careplus.model.Paciente;
import com.example.careplus.model.FichaClinica;
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
class FichaClinicaServiceTest {

    @Mock
    private FichaClinicaRepository fichaClinicaRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private ClassificacaoDoencasRepository cidRepository;

    @Mock
    private TratamentoRepository tratamentoRepository;

    @Mock
    private MedicacaoRepository medicacaoRepository;

    @InjectMocks
    private FichaClinicaService service;

    @Test
    @DisplayName("Quando a tabela de fichas clinicas estiver vazia deve retornar lista vazia")
    void deveRetornarListaVazia() {
        List<FichaClinica> esperado = new ArrayList<>();
        Mockito.when(fichaClinicaRepository.findAll()).thenReturn(esperado);

        List<FichaClinica> recebido = service.listarFichasClinicas();

        assertNotNull(recebido);
        assertTrue(recebido.isEmpty());
    }

    @Test
    @DisplayName("Quando houver fichas clinicas deve retornar lista preenchida e popular relacionamentos")
    void deveRetornarListaCheia() {
        FichaClinica f = new FichaClinica();
        f.setId(1L);
        List<FichaClinica> esperado = List.of(f);

        when(fichaClinicaRepository.findAll()).thenReturn(esperado);
        when(cidRepository.findByFichaClinica_Id(1L)).thenReturn(new ArrayList<>());
        when(tratamentoRepository.findByFichaClinica_Id(1L)).thenReturn(new ArrayList<>());
        when(medicacaoRepository.findByFichaClinica_Id(1L)).thenReturn(new ArrayList<>());

        List<FichaClinica> recebido = service.listarFichasClinicas();

        assertEquals(esperado.size(), recebido.size());
        assertEquals(1L, recebido.get(0).getId());
    }

    @Test
    @DisplayName("Criar ficha clinica com paciente existente deve salvar e retornar")
    void deveCriarFichaClinicaQuandoPacienteExiste() {
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        FichaClinicaRequestDto dto = Mockito.mock(FichaClinicaRequestDto.class);
        when(dto.getIdPaciente()).thenReturn(1L);
        when(dto.getDesfraldado()).thenReturn(Boolean.FALSE);
        when(dto.getHiperfoco()).thenReturn("dinossauro");
        when(dto.getAnamnese()).thenReturn("anam");
        when(dto.getDiagnostico()).thenReturn("diag");
        when(dto.getResumoClinico()).thenReturn("res");
        when(dto.getNivelAgressividade()).thenReturn(1);

        FichaClinica salvo = new FichaClinica();
        salvo.setId(10L);
        when(fichaClinicaRepository.save(any(FichaClinica.class))).thenReturn(salvo);

        FichaClinica recebido = service.criarFichaClinica(dto);

        assertNotNull(recebido);
        assertEquals(10L, recebido.getId());
    }

    @Test
    @DisplayName("Buscar ficha clinica por id existente retorna ficha clinica")
    void buscarFichaClinicaPorIdExistente() {
        FichaClinica f = new FichaClinica();
        f.setId(5L);
        when(fichaClinicaRepository.findById(5L)).thenReturn(Optional.of(f));

        FichaClinica recebido = service.buscarFichaClinicaPorId(5L);

        assertEquals(5L, recebido.getId());
    }

    @Test
    @DisplayName("Buscar ficha clinica por id inexistente lança RuntimeException")
    void buscarFichaClinicaPorIdInexistente() {
        when(fichaClinicaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.buscarFichaClinicaPorId(99L));
    }

    @Test
    @DisplayName("Atualizar ficha clinica existente salva alterações")
    void atualizarFichaClinicaExistente() {
        FichaClinica existente = new FichaClinica();
        existente.setId(7L);
        when(fichaClinicaRepository.findById(7L)).thenReturn(Optional.of(existente));

        FichaClinicaRequestDto dto = Mockito.mock(FichaClinicaRequestDto.class);
        when(dto.getDesfraldado()).thenReturn(Boolean.TRUE);
        when(dto.getHiperfoco()).thenReturn("dinossauro");
        when(dto.getAnamnese()).thenReturn("novo");
        when(dto.getDiagnostico()).thenReturn("novoDiag");
        when(dto.getResumoClinico()).thenReturn("resumo");
        when(dto.getNivelAgressividade()).thenReturn(2);

        when(fichaClinicaRepository.save(any(FichaClinica.class))).thenAnswer(i -> i.getArgument(0));

        FichaClinica atualizado = service.atualizarFichaClinica(dto, 7L);

        assertNotNull(atualizado);
        assertEquals(Boolean.TRUE, atualizado.getDesfraldado());
    }

    @Test
    @DisplayName("Atualizar ficha clinica inexistente lança RuntimeException")
    void atualizarFichaClinicaInexistente() {
        when(fichaClinicaRepository.findById(123L)).thenReturn(Optional.empty());
        FichaClinicaRequestDto dto = Mockito.mock(FichaClinicaRequestDto.class);

        assertThrows(RuntimeException.class, () -> service.atualizarFichaClinica(dto, 123L));
    }

    @Test
    @DisplayName("Deletar por id inexistente lança NoSuchElementException")
    void deletarPorIdInexistente() {
        when(fichaClinicaRepository.existsById(100L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> service.deletarPorId(100L));
    }

    @Test
    @DisplayName("Deletar por id existente remove a ficha clinica")
    void deletarPorIdExistente() {
        when(fichaClinicaRepository.existsById(2L)).thenReturn(true);
        service.deletarPorId(2L);
    }

    @Test
    @DisplayName("Buscar por nome retorna ficha clinica quando encontrado")
    void buscarPorNomeQuandoEncontrado() {
        FichaClinica f = new FichaClinica();
        when(fichaClinicaRepository.findByPacienteNomeContainsIgnoreCase("Maria")).thenReturn(Optional.of(f));

        FichaClinica recebido = service.buscarFichaClinicaPorNome("Maria");

        assertNotNull(recebido);
    }

    @Test
    @DisplayName("Buscar por nome quando não encontrado lança RuntimeException")
    void buscarPorNomeQuandoNaoEncontrado() {
        when(fichaClinicaRepository.findByPacienteNomeContainsIgnoreCase("ZZZ")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.buscarFichaClinicaPorNome("ZZZ"));
    }

    @Test
    @DisplayName("Buscar por cpf retorna ficha clinica quando encontrado")
    void buscarPorCpfQuandoEncontrado() {
        FichaClinica f = new FichaClinica();
        when(fichaClinicaRepository.findByPacienteCpf("12345678900")).thenReturn(Optional.of(f));

        FichaClinica recebido = service.buscarFichaClinicaPorCpf("12345678900");

        assertNotNull(recebido);
    }

    @Test
    @DisplayName("Buscar por cpf quando não encontrado lança RuntimeException")
    void buscarPorCpfQuandoNaoEncontrado() {
        when(fichaClinicaRepository.findByPacienteCpf("00000000000")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.buscarFichaClinicaPorCpf("00000000000"));
    }
}

