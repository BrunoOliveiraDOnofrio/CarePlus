package com.example.careplus.service;

import static org.junit.jupiter.api.Assertions.*;


import com.example.careplus.controller.dtoResponsavel.ResponsavelMapper;
import com.example.careplus.controller.dtoResponsavel.ResponsavelRequestDto;
import com.example.careplus.controller.dtoResponsavel.ResponsavelResponseDto;
import com.example.careplus.model.Prontuario;
import com.example.careplus.model.Responsavel;
import com.example.careplus.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResponsavelServiceTest {

    @Mock
    private ResponsavelRepository repository;

    @InjectMocks
    private ResponsavelService service;

    @Test
    @DisplayName("Cadastrar Responsavel")
    void deveCriarResponsavel(){
        ResponsavelRequestDto dto = Mockito.mock(ResponsavelRequestDto.class);
        when(dto.getCpf()).thenReturn("12345678900");
        when(dto.getEmail()).thenReturn("email@email.com");
        when(dto.getNome()).thenReturn("nomerson");
        when(dto.getTelefone()).thenReturn("11980897665");
        when(dto.getDtNascimento()).thenReturn(LocalDate.of(1999, 1, 1));

        when(repository.existsByEmail(anyString())).thenReturn(false);
        when(repository.existsByCpf(anyString())).thenReturn(false);

        Responsavel salvo = new Responsavel();

        salvo.setId(1L);
        when(repository.save(any(Responsavel.class))).thenReturn(salvo);

        Responsavel recebido = service.cadastrar(dto);

        assertNotNull(recebido);
        assertEquals(1L, recebido.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção quando cpf for existir e tenta criar")
    void deveLançarExceptionQuandoCPFDuplicado(){
        ResponsavelRequestDto dto1 = Mockito.mock(ResponsavelRequestDto.class);
        when(dto1.getCpf()).thenReturn("12345678900");
        Responsavel salvo1 = new Responsavel();
        salvo1.setId(1L);
        when(repository.save(any(Responsavel.class))).thenReturn(salvo1);
        Responsavel recebido1 = service.cadastrar(dto1);
        assertNotNull(recebido1);
        ResponsavelRequestDto dto2 = Mockito.mock(ResponsavelRequestDto.class);
        when(dto2.getCpf()).thenReturn("12345678900");
        when(repository.existsByCpf("12345678900")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(dto2));
    }

    @Test
    @DisplayName("Deve retornar lista cheia quando existir algo")
    void deveRetornarListaCheia(){
        Responsavel responsavel = new Responsavel();
        responsavel.setId(1L);
        List<Responsavel> esperado = List.of(responsavel);
        when(repository.findAll()).thenReturn(esperado);

        List<Responsavel> recebido = service.listar();

        assertEquals(esperado.size(), recebido.size());
        assertEquals(1L, recebido.get(0).getId());
    }

    @Test
    @DisplayName("Quando a tabela de responsavel estivar vazia, deve retornar vazia")
    void deveRetornarListaVazia() {
        List<Responsavel> esperado = new ArrayList<>();
        Mockito.when(repository.findAll()).thenReturn(esperado);

        List<Responsavel> recebido = service.listar();

        assertNotNull(recebido);
        assertTrue(recebido.isEmpty());
    }

    @Test
    @DisplayName("Buscar prontuario por id existente retorna prontuario")
    void buscarResponsavelPorIdExistente() {
        Responsavel responsavel = new Responsavel();
        responsavel.setEmail("email@email.com");
        when(repository.findByEmailStartingWith("email@email.com"))
                .thenReturn(Optional.of(responsavel));

        Responsavel recebido = service.buscarPorEmail("email@email.com");

        assertEquals("email@email.com", recebido.getEmail());
    }

    @Test
    @DisplayName("deve lançar exception quando email não existir")
    void buscarResponsavelPorEmailInexistente(){
        when(repository.findByEmailStartingWith("patrati@email.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.buscarPorEmail("patrati@email.com"));
    }

    @Test
    @DisplayName("deve atualizar Responsavel existente")
    void atualizarResponsavelExistente(){
        Responsavel existe = new Responsavel();
        existe.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existe));

        ResponsavelRequestDto dto = Mockito.mock(ResponsavelRequestDto.class);
        when(dto.getCpf()).thenReturn("12345678900");
        when(dto.getEmail()).thenReturn("email@email.com");
        when(dto.getNome()).thenReturn("nomerson");
        when(dto.getTelefone()).thenReturn("11980897665");

        when(repository.save(any(Responsavel.class))).thenAnswer(i -> i.getArgument(0));

        Responsavel atualizado = service.atualizar(1L, dto);

        assertNotNull(atualizado);
        assertEquals("nomerson", atualizado.getNome());
    }

    @Test
    @DisplayName("Atualizar Responsavel inexistente deve lançar exception")
    void atualizarResponsavelInexistente(){
        when(repository.findById(1L)).thenReturn(Optional.empty());
        ResponsavelRequestDto dto = Mockito.mock(ResponsavelRequestDto.class);

        assertThrows(RuntimeException.class, () -> service.atualizar(1L, dto));
    }


    @Test
    @DisplayName("Deletar por id inexistente lança NoSuchElementException")
    void deletarPorIdInexistente() {
        when(repository.existsById(100L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> service.deletar(100L));

        verify(repository, times(1)).existsById(100L);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deletar por id existente remove o prontuario")
    void deletarPorIdExistente() {
        when(repository.existsById(2L)).thenReturn(true);

        service.deletar(2L);

        verify(repository, times(1)).existsById(2L);
        verify(repository, times(1)).deleteById(2L);
    }

}