package com.example.careplus.service;

import com.example.careplus.dto.dtoCuidador.CuidadorMapper;
import com.example.careplus.dto.dtoCuidador.CuidadorRequestDto;
import com.example.careplus.dto.dtoCuidador.CuidadorResponseDto;
import com.example.careplus.model.Cuidador;
import com.example.careplus.model.Paciente;
import com.example.careplus.model.Responsavel;
import com.example.careplus.repository.CuidadorRepository;
import com.example.careplus.repository.PacienteRepository;
import com.example.careplus.repository.ResponsavelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CuidadorServiceTest {

    @Mock
    private CuidadorRepository repository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private ResponsavelRepository responsavelRepository;

    @InjectMocks
    private CuidadorService service;

    @Test
    @DisplayName("Criar relação de cuidador quando paciente e responsavel")
    void deveCriarCuidadorSePacienteResponsavelExistir(){
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        Responsavel responsavel = new Responsavel();
        responsavel.setId(1L);
        when(responsavelRepository.findById(1L)).thenReturn(Optional.of(responsavel));

        CuidadorRequestDto dto = Mockito.mock(CuidadorRequestDto.class);
        when(dto.getPacienteId()).thenReturn(1L);
        when(dto.getResponsavelId()).thenReturn(1L);
        when(dto.getParentesco()).thenReturn("Avó");

        when(repository.save(any(Cuidador.class)))
                .thenAnswer(invocation -> {
                    Cuidador c = invocation.getArgument(0);
                    c.setId(1L);
                    return c;
                });

        Cuidador recebido = service.cadastrar(dto);

        assertNotNull(recebido);
        assertEquals(1L, recebido.getId());
        assertEquals(1L, recebido.getResponsavel().getId());
        assertEquals(1L, recebido.getPaciente().getId());
    }

    @Test
    @DisplayName("Quando não houver cuidadores deve retornar lista vazia")
    void deveRetornarListaVazia() {
        List<Cuidador> esperado = new ArrayList<>();
        when(repository.findAll()).thenReturn(esperado);

        List<Cuidador> recebido = service.listar();

        assertNotNull(recebido);
        assertTrue(recebido.isEmpty());
    }

    @Test
    @DisplayName("Quando houver cuidadores deve retornar lista preenchida")
    void deveRetornarListaCheia() {
        Cuidador c = new Cuidador();
        c.setId(1L);

        List<Cuidador> esperado = List.of(c);

        when(repository.findAll()).thenReturn(esperado);

        List<Cuidador> recebido = service.listar();

        assertNotNull(recebido);
        assertEquals(1, recebido.size());
        assertEquals(1L, recebido.get(0).getId());
    }

    @Test
    @DisplayName("Atualizar cuidador existente salva alterações")
    void atualizarCuidadorExistente() {
        Cuidador existente = new Cuidador();
        existente.setId(7L);

        Paciente paciente = new Paciente();
        paciente.setId(1L);

        Responsavel responsavel = new Responsavel();
        responsavel.setId(10L);

        existente.setPaciente(paciente);
        existente.setResponsavel(responsavel);
        existente.setParentesco("Tio");

        when(repository.findById(7L)).thenReturn(Optional.of(existente));

        CuidadorRequestDto dto = Mockito.mock(CuidadorRequestDto.class);
        when(dto.getPacienteId()).thenReturn(1L);
        when(dto.getResponsavelId()).thenReturn(10L);
        when(dto.getParentesco()).thenReturn("Avó");

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(responsavelRepository.findById(10L)).thenReturn(Optional.of(responsavel));

        when(repository.save(any(Cuidador.class))).thenAnswer(i -> i.getArgument(0));

        Cuidador atualizado = service.atualizar(7L, dto);

        assertNotNull(atualizado);
        assertEquals("Avó", atualizado.getParentesco());
        assertEquals(1L, atualizado.getPaciente().getId());
        assertEquals(10L, atualizado.getResponsavel().getId());
    }

    @Test
    @DisplayName("Atualizar cuidador inexistente lança RuntimeException")
    void atualizarCuidadorInexistente() {
        when(repository.findById(500L)).thenReturn(Optional.empty());

        CuidadorRequestDto dto = Mockito.mock(CuidadorRequestDto.class);

        assertThrows(RuntimeException.class,
                () -> service.atualizar(500L, dto));
    }

    @Test
    @DisplayName("Deletar cuidador por id inexistente lança NoSuchElementException")
    void deletarCuidadorPorIdInexistente() {
        when(repository.existsById(100L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> service.deletar(100L));
    }

    @Test
    @DisplayName("Deletar cuidador por id existente remove o cuidador")
    void deletarCuidadorPorIdExistente() {
        when(repository.existsById(2L)).thenReturn(true);

        service.deletar(2L);

        Mockito.verify(repository).deleteById(2L);
    }

    @Test
    @DisplayName("Listar pacientes por responsavel retorna lista preenchida")
    void listarPacientesPorResponsavel_RetornaListaCheia() {
        Cuidador c = new Cuidador();
        c.setId(1L);

        when(repository.findByResponsavelId(10L)).thenReturn(List.of(c));

        try (MockedStatic<CuidadorMapper> mapperMock = Mockito.mockStatic(CuidadorMapper.class)) {
            mapperMock.when(() -> CuidadorMapper.toResponseDto(List.of(c)))
                    .thenReturn(List.of(new CuidadorResponseDto()));

            List<CuidadorResponseDto> resultado = service.listarPacientesPorResponsavel_Id(10L);

            assertNotNull(resultado);
            assertEquals(1, resultado.size());
        }
    }


    @Test
    @DisplayName("Listar responsaveis por nome do paciente retorna lista preenchida")
    void listarResponsaveisPorPaciente_RetornaListaCheia() {
        Cuidador c = new Cuidador();
        c.setId(1L);

        when(repository.findByPacienteNomeIgnoreCaseStartingWith("João")).thenReturn(List.of(c));

        try (MockedStatic<CuidadorMapper> mapperMock = Mockito.mockStatic(CuidadorMapper.class)) {
            mapperMock.when(() -> CuidadorMapper.toResponseDto(List.of(c)))
                    .thenReturn(List.of(new CuidadorResponseDto()));

            List<CuidadorResponseDto> resultado = service.listarResponsaveisPorPaciente_Nome("João");

            assertNotNull(resultado);
            assertEquals(1, resultado.size());
        }
    }


}