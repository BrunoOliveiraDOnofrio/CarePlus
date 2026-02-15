package com.example.careplus.service;

import com.example.careplus.dto.dtoMedicacao.MedicacaoRequestDto;
import com.example.careplus.model.Medicacao;
import com.example.careplus.model.FichaClinica;
import com.example.careplus.repository.MedicacaoRepository;
import com.example.careplus.repository.FichaClinicaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MedicacaoServiceTest {

    @Mock
    private MedicacaoRepository medicacaoRepository;

    @Mock
    private FichaClinicaRepository fichaClinicaRepository;

    @InjectMocks
    private MedicacaoService service;


    @Test
    @DisplayName("Deve adicionar medicação quando a ficha clínica existe")
    void adicionarComFichaClinicaExistenteTest() {
        MedicacaoRequestDto dto = new MedicacaoRequestDto();
        dto.setNomeMedicacao("Med1");
        dto.setDataInicio(LocalDate.of(2024, 1, 1));
        dto.setAtivo(true);
        dto.setIdProntuario(1L);

        FichaClinica fichaClinica = new FichaClinica();
        Mockito.when(fichaClinicaRepository.findById(1L))
                .thenReturn(Optional.of(fichaClinica));

        Medicacao salvada = new Medicacao();
        salvada.setNomeMedicacao("Med1");

        Mockito.when(medicacaoRepository.save(Mockito.any()))
                .thenReturn(salvada);

        Medicacao resultado = service.adicionar(dto);

        assertEquals("Med1", resultado.getNomeMedicacao());
    }

    @Test
    @DisplayName("Deve lançar erro ao adicionar quando ficha clínica não existe")
    void adicionarSemFichaClinicaTest() {
        MedicacaoRequestDto dto = new MedicacaoRequestDto();
        dto.setIdProntuario(2L);

        Mockito.when(fichaClinicaRepository.findById(2L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.adicionar(dto));
    }


    @Test
    @DisplayName("Deve atualizar medicação quando ela existe")
    void atualizarMedicacaoTestTest() {
        MedicacaoRequestDto dto = new MedicacaoRequestDto();
        dto.setNomeMedicacao("Med2");
        dto.setAtivo(false);

        Medicacao existente = new Medicacao();
        existente.setNomeMedicacao("Med1");

        Mockito.when(medicacaoRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        Mockito.when(medicacaoRepository.save(existente))
                .thenReturn(existente);

        Medicacao resultado = service.atualizar(1L, dto);

        assertEquals("Med2", resultado.getNomeMedicacao());
    }

    @Test
    @DisplayName("Deve lançar erro ao atualizar medicação que não existe")
    void atualizarMedicacaoInexistenteTest() {
        Mockito.when(medicacaoRepository.findById(1L))
                .thenReturn(Optional.empty());

        MedicacaoRequestDto dto = new MedicacaoRequestDto();

        assertThrows(RuntimeException.class,
                () -> service.atualizar(1L, dto));
    }


    @Test
    @DisplayName("Deve deletar quando ID existe")
    void deletarExistenteTest() {
        Mockito.when(medicacaoRepository.existsById(1L))
                .thenReturn(true);

        assertDoesNotThrow(() -> service.deletar(1L));
        Mockito.verify(medicacaoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar deletar ID que não existe")
    void deletarInexistenteTest() {
        Mockito.when(medicacaoRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> service.deletar(1L));
    }


    @Test
    @DisplayName("Deve retornar quantidade de medicações ativas")
    void contarAtivasTest() {
        Mockito.when(medicacaoRepository.countByAtivoTrue())
                .thenReturn(3L);

        long resultado = service.contarAtivas();

        assertEquals(3L, resultado);
    }


    @Test
    @DisplayName("Deve ordenar corretamente por tempo medicando")
    void ordenarPorTempoMedicandoTest() {
        Medicacao m1 = new Medicacao();
        m1.setNomeMedicacao("Med1");
        m1.setDataInicio(LocalDate.now().minusDays(3));

        Medicacao m2 = new Medicacao();
        m2.setNomeMedicacao("Med2");
        m2.setDataInicio(LocalDate.now().minusDays(1));

        List<Medicacao> lista = new ArrayList<>();
        lista.add(m1);
        lista.add(m2);

        Mockito.when(medicacaoRepository.findAll())
                .thenReturn(lista);

        List<Medicacao> ordenada = service.ordenarPorTempoMedicando();

        // Med2 deve vir primeiro (1 dia medicando - menos tempo)
        // Med1 deve vir depois (3 dias medicando - mais tempo)
        assertEquals("Med2", ordenada.get(0).getNomeMedicacao());
        assertEquals("Med1", ordenada.get(1).getNomeMedicacao());
    }
  
}