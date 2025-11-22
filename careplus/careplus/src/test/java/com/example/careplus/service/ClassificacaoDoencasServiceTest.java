package com.example.careplus.service;

import com.example.careplus.controller.dtoCid.ClassificacaoDoencasRequestDto;
import com.example.careplus.model.ClassificacaoDoencas;
import com.example.careplus.model.Prontuario;
import com.example.careplus.repository.ClassificacaoDoencasRepository;
import com.example.careplus.repository.ProntuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Classificação Doenças")
@ExtendWith(MockitoExtension.class)
class ClassificacaoDoencasServiceTest {

    @Mock
    private ClassificacaoDoencasRepository classificacaoDoencasRepository;
    @Mock
    private ProntuarioRepository prontuarioRepository;
    @InjectMocks
    private ClassificacaoDoencasService service;

    @Test
    @DisplayName("Quando a tabela de ClassificacaoDoencas estiver vazia deve retornar lista vazia")
    void deveRetornarListaVaziaTeste() {
        // ARRANGE
        List<ClassificacaoDoencas> esperado = new ArrayList<>();
        when(classificacaoDoencasRepository.findAll())
                .thenReturn(esperado);

        // ACT
        List<ClassificacaoDoencas> recebido = service.listar();

        // ASSERT
        assertTrue(recebido.isEmpty());
    }

    @Test
    @DisplayName("Quando a tabela de ClassificacaoDoencas estiver algo deve retornar lista cheia")
    void deveRetornarListaComAlgoTeste(){
        // ARRANGE
        String cid = "F84.0";
        LocalDate dtModificacao = LocalDate.of(2025, 1, 10);


        ClassificacaoDoencas doenca = new ClassificacaoDoencas();
        doenca.setId(1L);
        doenca.setCid(cid);
        doenca.setDtModificacao(dtModificacao);
        Prontuario prontuarioMock = mock(Prontuario.class);
        doenca.setProntuario(prontuarioMock);
        List<ClassificacaoDoencas> esperado = new ArrayList<>();
        esperado.add(doenca);

        Mockito.when(classificacaoDoencasRepository.findAll())
                .thenReturn(esperado);

        // ACT
        List<ClassificacaoDoencas> recebido = service.listar();

        // ASSERT
        assertFalse(recebido.isEmpty());

    }

    @Test
    @DisplayName("Criar a classificação da doença quando existir um prontuário")
    void deveCriarClassificacaoDoencasQuandoProntuarioExisteTeste(){
        Prontuario prontuario = new Prontuario();
        prontuario.setId(1L);
        when(prontuarioRepository.findById(1L)).thenReturn(Optional.of(prontuario));

        ClassificacaoDoencasRequestDto dto = Mockito.mock(ClassificacaoDoencasRequestDto.class);
        when(dto.getIdProntuario()).thenReturn(1L);
        when(dto.getCid()).thenReturn("F84.0");

        ClassificacaoDoencas salvo = new ClassificacaoDoencas();
        salvo.setId(10L);
        when(classificacaoDoencasRepository.save(any(ClassificacaoDoencas.class))).thenReturn(salvo);

        ClassificacaoDoencas recebido = service.cadastrar(dto);

        assertNotNull(recebido);
        assertEquals(10L, recebido.getId());

    }

    @Test
    @DisplayName("Buscar Classificacao por id existe retorna ClassificacaoDoenca")
    void buscarClassificacaoDoencaPorIdExistenteTeste(){
        ClassificacaoDoencas classificacaoDoencas = new ClassificacaoDoencas();
        classificacaoDoencas.setId(5L);
        when(classificacaoDoencasRepository.findById(5L)).thenReturn(Optional.of(classificacaoDoencas));

        ClassificacaoDoencas recebido = service.buscarPorId(5L);

        assertEquals(5L, recebido.getId());
    }

    @Test
    @DisplayName("Buscar Classificacao por id inexistente deve lançar Exception")
    void buscarClassificacaoDoencaPorIdInexistenteTeste(){
        when(classificacaoDoencasRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.buscarPorId(99L));
    }

    @Test
    @DisplayName("Atualizar ClassificacaoDoenca existente salva alterações")
    void atualizarClassificacaoDoencaExistenteTeste(){
        ClassificacaoDoencas existe = new ClassificacaoDoencas();
        existe.setId(7L);
        when(classificacaoDoencasRepository.findById(7L)).thenReturn(Optional.of(existe));

        ClassificacaoDoencasRequestDto dto = Mockito.mock(ClassificacaoDoencasRequestDto.class);
        when(dto.getCid()).thenReturn("A60R");
        when(dto.getIdProntuario()).thenReturn(2L);

        Prontuario prontuario = new Prontuario();
        prontuario.setId(2L);
        when(prontuarioRepository.findById(2L)).thenReturn(Optional.of(prontuario));

        when(classificacaoDoencasRepository.save(any(ClassificacaoDoencas.class))).thenAnswer(i -> i.getArgument(0));

        ClassificacaoDoencas atualizado = service.atualizar(7L, dto);

        assertNotNull(atualizado);
        assertEquals("A60R", atualizado.getCid());
    }

    @Test
    @DisplayName("Atualizar ClassificacaoDoenca inexistente deve lançar Exeption")
    void atualizarClassificacaoDoencaInexistenteTeste(){
        when(classificacaoDoencasRepository.findById(99L)).thenReturn(Optional.empty());
        ClassificacaoDoencasRequestDto dto = Mockito.mock(ClassificacaoDoencasRequestDto.class);

        assertThrows(RuntimeException.class, () -> service.atualizar(99L, dto));
    }

    @Test
    @DisplayName("Deletar por id inexistente deve lançar Exception")
    void deletarClassificacaoDoencaPorIdInexistenteTeste(){
        when(classificacaoDoencasRepository.existsById(99L)).thenReturn(Boolean.FALSE);
        assertThrows(NoSuchElementException.class, () -> service.deletar(99L));
    }

    @Test
    @DisplayName("Deletar por id existente remove classificacaoDoenca")
    void deletarClassificacaoDoencaPorIdExistenteTeste(){
        when(classificacaoDoencasRepository.existsById(2L)).thenReturn(true);

        service.deletar(2L);
    }


}