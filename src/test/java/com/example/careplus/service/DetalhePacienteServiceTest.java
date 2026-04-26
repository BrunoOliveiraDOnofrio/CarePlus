package com.example.careplus.service;

import com.example.careplus.dto.dtoPaciente.DetalhePacienteDTO;
import com.example.careplus.model.*;
import com.example.careplus.repository.ConsultaProntuarioRepository;
import com.example.careplus.repository.PacienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DetalhePacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private ConsultaProntuarioRepository consultaProntuarioRepository;

    @Mock
    private FichaClinicaService fichaClinicaService;

    @InjectMocks
    private DetalhePacienteService service;

    @Test
    @DisplayName("Deve filtrar ultima e proxima consulta por paciente e funcionario")
    void deveBuscarDetalhesCompletoPacientePorFuncionario() {
        Long pacienteId = 1L;
        Long funcionarioId = 2L;

        Paciente paciente = new Paciente();
        paciente.setId(pacienteId);
        paciente.setNome("Paciente Teste");
        paciente.setDtNascimento(LocalDate.of(2010, 1, 1));
        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(paciente));

        FichaClinica fichaClinica = new FichaClinica();
        fichaClinica.setId(10L);
        fichaClinica.setAnamnese("Anamnese");
        fichaClinica.setDiagnostico("Diagnostico");
        fichaClinica.setResumoClinico("Resumo");
        fichaClinica.setNivelAgressividade(1);
        fichaClinica.setDesfraldado(Boolean.TRUE);
        fichaClinica.setHiperfoco("Foco");
        fichaClinica.setMedicacoes(new ArrayList<>());
        fichaClinica.setCid(new ArrayList<>());
        when(fichaClinicaService.buscarFichaClinicaPorPacienteId(pacienteId)).thenReturn(fichaClinica);

        ConsultaProntuario ultima = new ConsultaProntuario();
        ultima.setId(100L);
        ultima.setData(LocalDate.now().minusDays(2));

        Funcionario funcionario = new Funcionario();
        funcionario.setId(funcionarioId);
        funcionario.setNome("Func Teste");
        funcionario.setEspecialidade("Terapia");

        ConsultaFuncionario consultaFuncionario = new ConsultaFuncionario();
        consultaFuncionario.setFuncionario(funcionario);
        consultaFuncionario.setConsulta(ultima);
        ultima.setConsultaFuncionarios(List.of(consultaFuncionario));

        ConsultaProntuario proxima = new ConsultaProntuario();
        proxima.setId(200L);
        proxima.setData(LocalDate.now().plusDays(3));

        when(consultaProntuarioRepository.buscarUltimaConsultaPorPacienteEFuncionario(pacienteId, funcionarioId))
                .thenReturn(List.of(ultima));
        when(consultaProntuarioRepository.buscarProximaConsultaPorPacienteEFuncionario(pacienteId, funcionarioId))
                .thenReturn(List.of(proxima));

        DetalhePacienteDTO dto = service.buscarDetalhesCompletoPaciente(pacienteId, funcionarioId);

        assertNotNull(dto);
        assertEquals(pacienteId, dto.getPacienteId());
        assertNotNull(dto.getUltimaConsulta());
        assertEquals(proxima.getData(), dto.getProximaConsulta());

        verify(consultaProntuarioRepository).buscarUltimaConsultaPorPacienteEFuncionario(pacienteId, funcionarioId);
        verify(consultaProntuarioRepository).buscarProximaConsultaPorPacienteEFuncionario(pacienteId, funcionarioId);
    }
}
