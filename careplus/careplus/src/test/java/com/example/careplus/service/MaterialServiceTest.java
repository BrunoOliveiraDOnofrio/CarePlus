//package com.example.careplus.service;
//
//import com.example.careplus.dto.dtoMaterial.MaterialRequestDto;
//import com.example.careplus.dto.dtoMaterial.MaterialResponseDto;
//import com.example.careplus.model.Consulta;
//import com.example.careplus.model.Material;
//import com.example.careplus.repository.ConsultaRepository;
//import com.example.careplus.repository.MaterialRepository;
//import jakarta.persistence.EntityNotFoundException;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class MaterialServiceTest {
//
//    @Mock
//    private MaterialRepository materialRepository;
//
//    @Mock
//    private ConsultaRepository consultaRepository;
//
//    @InjectMocks
//    private MaterialService service;
//
//    @Test
//    @DisplayName("Deve cadastrar material com consulta existente")
//    void deveCadastrarMaterial() {
//        MaterialRequestDto dto = new MaterialRequestDto();
//        dto.setItem("Brinquedo");
//        dto.setTempoExposicao(100);
//        dto.setIdConsulta(1L);
//
//        Consulta consulta = new Consulta();
//        consulta.setId(1L);
//
//        Material materialSalvo = new Material();
//        materialSalvo.setId(1L);
//        materialSalvo.setItem("Brinquedo");
//        materialSalvo.setTempoExposicao(100);
//        materialSalvo.setDataImplementacao(LocalDate.of(2024, 1, 1));
//        materialSalvo.setConsulta(consulta);
//
//        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
//        when(materialRepository.save(any(Material.class))).thenReturn(materialSalvo);
//
//        MaterialResponseDto resultado = service.cadastrar(dto);
//
//        assertNotNull(resultado);
//        assertEquals(1L, resultado.getId());
//        assertEquals("Brinquedo", resultado.getItem());
//        assertEquals(100, resultado.getTempoExposicao());
//        verify(consultaRepository).findById(1L);
//        verify(materialRepository).save(any(Material.class));
//    }
//
//    @Test
//    @DisplayName("Deve lançar exception se consulta não existir ao cadastrar")
//    void deveLancarExcecaoCadastro() {
//        MaterialRequestDto dto = new MaterialRequestDto();
//        dto.setIdConsulta(99L);
//
//        when(consultaRepository.findById(99L)).thenReturn(Optional.empty());
//
//        RuntimeException exception = assertThrows(RuntimeException.class,
//                () -> service.cadastrar(dto));
//        assertEquals("Consulta não encontrado", exception.getMessage());
//
//        verify(consultaRepository).findById(99L);
//        verify(materialRepository, never()).save(any(Material.class));
//    }
//
//    @Test
//    @DisplayName("Deve listar materiais")
//    void deveListarMateriais() {
//        Material material = new Material();
//        material.setId(1L);
//        material.setItem("Brinquedo");
//        material.setTempoExposicao(50);
//        material.setDataImplementacao(LocalDate.now());
//
//        List<Material> lista = List.of(material);
//
//        when(materialRepository.findAll()).thenReturn(lista);
//
//        List<MaterialResponseDto> recebido = service.listar();
//
//        assertEquals(1, recebido.size());
//        assertEquals(1L, recebido.get(0).getId());
//        assertEquals("Brinquedo", recebido.get(0).getItem());
//        verify(materialRepository).findAll();
//    }
//
//    @Test
//    @DisplayName("Deve lançar exception se lista estiver vazia")
//    void listarVazio() {
//        when(materialRepository.findAll()).thenReturn(List.of());
//
//        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
//                () -> service.listar());
//        assertEquals("Nenhuma atividade encontrada", exception.getMessage());
//        verify(materialRepository).findAll();
//    }
//
//    @Test
//    @DisplayName("Deve atualizar material existente")
//    void deveAtualizarMaterial() {
//        Long materialId = 10L;
//
//        Material existente = new Material();
//        existente.setId(materialId);
//        existente.setItem("Item antigo");
//        existente.setTempoExposicao(10);
//        existente.setDataImplementacao(LocalDate.of(2023, 1, 1));
//
//        MaterialRequestDto dto = new MaterialRequestDto();
//        dto.setItem("Item atualizado");
//        dto.setTempoExposicao(20);
//        dto.setDataImplementacao(LocalDate.of(2024, 1, 1));
//
//        Material materialAtualizado = new Material();
//        materialAtualizado.setId(materialId);
//        materialAtualizado.setItem("Item atualizado");
//        materialAtualizado.setTempoExposicao(20);
//        materialAtualizado.setDataImplementacao(LocalDate.of(2024, 1, 1));
//
//        when(materialRepository.findById(materialId)).thenReturn(Optional.of(existente));
//        when(materialRepository.save(any(Material.class))).thenReturn(materialAtualizado);
//
//        MaterialResponseDto recebido = service.atualizar(materialId, dto);
//
//        assertNotNull(recebido);
//        assertEquals(materialId, recebido.getId());
//        assertEquals("Item atualizado", recebido.getItem());
//        assertEquals(20, recebido.getTempoExposicao());
//
//        verify(materialRepository).findById(materialId);
//        verify(materialRepository).save(existente);
//
//        assertEquals("Item atualizado", existente.getItem());
//        assertEquals(20, existente.getTempoExposicao());
//        assertEquals(LocalDate.of(2024, 1, 1), existente.getDataImplementacao());
//    }
//
//    @Test
//    @DisplayName("Deve lançar exception ao tentar atualizar inexistente")
//    void atualizarInexistente() {
//        Long materialId = 50L;
//        MaterialRequestDto dto = new MaterialRequestDto();
//
//        when(materialRepository.findById(materialId)).thenReturn(Optional.empty());
//
//        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
//                () -> service.atualizar(materialId, dto));
//        assertEquals("Atividade não encontrada", exception.getMessage());
//
//        verify(materialRepository).findById(materialId);
//        verify(materialRepository, never()).save(any(Material.class));
//    }
//
//    @Test
//    @DisplayName("Deve deletar material existente")
//    void deveDeletar() {
//        Long materialId = 2L;
//        when(materialRepository.existsById(materialId)).thenReturn(true);
//
//        service.deletar(materialId);
//
//        verify(materialRepository).existsById(materialId);
//        verify(materialRepository).deleteById(materialId);
//    }
//
//    @Test
//    @DisplayName("Deve lançar exception ao tentar deletar inexistente")
//    void deletarInexistente() {
//        Long materialId = 3L;
//        when(materialRepository.existsById(materialId)).thenReturn(false);
//
//        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
//                () -> service.deletar(materialId));
//        assertEquals("Atividade não encontrada", exception.getMessage());
//
//        verify(materialRepository).existsById(materialId);
//        verify(materialRepository, never()).deleteById(materialId);
//    }
//
//    @Test
//    @DisplayName("Deve contar corretamente")
//    void deveContar() {
//        when(materialRepository.count()).thenReturn(5L);
//
//        long resultado = service.contarAtividades();
//
//        assertEquals(5L, resultado);
//        verify(materialRepository).count();
//    }
//
//    @Test
//    @DisplayName("Deve listar materiais com tempo maior")
//    void deveListarPorTempo() {
//        Integer tempo = 30;
//        Material material = new Material();
//        material.setId(1L);
//        material.setTempoExposicao(40);
//        material.setItem("Brinquedo");
//        material.setDataImplementacao(LocalDate.now());
//
//        List<Material> materials = List.of(material);
//
//        when(materialRepository.findByTempoExposicaoGreaterThan(tempo))
//                .thenReturn(materials);
//
//        List<MaterialResponseDto> recebido = service.listarPorTempoExposicaoMaiorQue(tempo);
//
//        assertEquals(1, recebido.size());
//        assertEquals(1L, recebido.get(0).getId());
//        assertEquals(40, recebido.get(0).getTempoExposicao());
//
//        verify(materialRepository).findByTempoExposicaoGreaterThan(tempo);
//    }
//
//    @Test
//    @DisplayName("Deve lançar exception se não houver materiais no filtro")
//    void listarPorTempoVazio() {
//        Integer tempo = 50;
//        when(materialRepository.findByTempoExposicaoGreaterThan(tempo))
//                .thenReturn(List.of());
//
//        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
//                () -> service.listarPorTempoExposicaoMaiorQue(tempo));
//        assertEquals("Nenhuma atividade com tempo maior que " + tempo, exception.getMessage());
//
//        verify(materialRepository).findByTempoExposicaoGreaterThan(tempo);
//    }
//}