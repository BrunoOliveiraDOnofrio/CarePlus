package com.example.careplus.controller;

import com.example.careplus.dto.dtoConsultaProntuario.*;
import com.example.careplus.dto.dtoConsultaRecorrente.AgendarConsultasRequestDto;
import com.example.careplus.dto.dtoConsultaRecorrente.AgendarConsultasResponseDto;
import com.example.careplus.model.ConsultaProntuario;
import com.example.careplus.service.ConsultaProntuarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("consultas-prontuario")
public class ConsultaProntuarioController {
    private final ConsultaProntuarioService service;

    public ConsultaProntuarioController(ConsultaProntuarioService service) {
        this.service = service;
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaProntuarioResponseDto> marcarConsulta(@RequestBody ConsultaProntuarioRequestDto request) {
        ConsultaProntuarioResponseDto consulta = service.marcarConsulta(request);
        return ResponseEntity.ok(consulta);
    }

    @GetMapping("/revisar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaProntuarioResponseDto> revisarConsulta(@RequestBody ConsultaProntuarioRequestDto request) {
        ConsultaProntuarioResponseDto consulta = service.revisarConsulta(request);
        return ResponseEntity.ok(consulta);
    }

    @DeleteMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> deletarConsulta(@RequestParam Long id){
        try{
            service.removerConsulta(id);
            return ResponseEntity.status(204).build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/recorrencia/{recorrenciaId}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> deletarRecorrencia(@PathVariable String recorrenciaId) {
        service.removerRecorrencia(recorrenciaId);
        return ResponseEntity.status(204).build();
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaProntuarioResponseDto> editarConsulta(
            @PathVariable Long id,
            @RequestBody ConsultaProntuarioRequest request) {
        ConsultaProntuarioResponseDto consulta = service.editarConsulta(id, request);
        return ResponseEntity.ok(consulta);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaProntuario>> listarConsultas(){
        List<ConsultaProntuario> consultas = service.listarConsultas();
        if (consultas.isEmpty()){
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(200).body(consultas);
        }
    }

    @GetMapping("/por-data")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaProntuarioResponseDto>> listarPorData(){
        List<ConsultaProntuarioResponseDto> consultas = service.listarPorData();

        if (consultas.isEmpty()){
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(200).body(consultas);
        }

    }

    @GetMapping("/por-paciente")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaProntuarioResponseDto>> listarPorPaciente(@RequestParam Long idPaciente){
        List<ConsultaProntuarioResponseDto> consultas = service.listarPorPaciente(idPaciente);

        if (consultas.isEmpty()){
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(200).body(consultas);
        }

    }

    @PutMapping("/confirmar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaProntuarioResponseDto> confirmarConsulta(@RequestParam Long idConsulta){
        ConsultaProntuarioResponseDto consultaResponseDto = service.confirmarConsulta(idConsulta);
        return ResponseEntity.status(200).body(consultaResponseDto);
    }

    @PutMapping("/recusar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaProntuarioResponseDto> recusarConsulta(@RequestParam Long idConsulta, @RequestBody String justificativa){
        ConsultaProntuarioResponseDto consultaResponseDto = service.recusarConsulta(idConsulta, justificativa);
        return ResponseEntity.status(200).body(consultaResponseDto);
    }

    @PutMapping("/realizarObservacoes")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaProntuarioResponseDto> editarObervacoes(@RequestParam Long idConsulta, @RequestBody RealizarConsultaProntuarioDto obs) throws JsonProcessingException {
        ConsultaProntuarioResponseDto consultaResponseDto = service.salvarObservacoes(idConsulta, obs);
        return ResponseEntity.status(200).body(consultaResponseDto);
    }

    @GetMapping("/agenda-diaria")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaProntuarioResponseDto>> buscarConsultasDoDia(
            @RequestParam Long id,
            @RequestParam String tipo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataReferencia
    ){
        List<ConsultaProntuarioResponseDto> consultaResponseDtos = service.listarAgendaDiaria(id, tipo, dataReferencia);
        return ResponseEntity.status(200).body(consultaResponseDtos);
    }

    @GetMapping("/agenda-semanal")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaProntuarioResponseDto>> listarAgendaSemanal(
            @RequestParam Long id,
            @RequestParam String tipo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataReferencia
    ) {
        List<ConsultaProntuarioResponseDto> agenda = service.listarAgendaSemanal(id, tipo, dataReferencia);
        return ResponseEntity.ok(agenda);
    }

    @GetMapping("/agenda-mensal")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaProntuarioResponseDto>> listarAgendaMensal(
            @RequestParam Long id,
            @RequestParam String tipo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataReferencia
    ) {
        List<ConsultaProntuarioResponseDto> agenda = service.listarAgendaMensal(id, tipo, dataReferencia);
        return ResponseEntity.ok(agenda);
    }

    @GetMapping("/notificar-responsavel")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaProntuarioResponseDto>> notificarResponsavel(
            @RequestParam Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataReferencia
    ) {
        List<ConsultaProntuarioResponseDto> agenda = service.notificarResponsavel(id, dataReferencia);
        return ResponseEntity.ok(agenda);
    }


    @PostMapping("/recorrentes")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<AgendarConsultasResponseDto> agendarConsultas(
            @RequestBody AgendarConsultasRequestDto request) {
        AgendarConsultasResponseDto response = service.agendarConsultas(request);
        return ResponseEntity.status(207).body(response);
    }
    @GetMapping("/pendentes")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaProntuarioResponseDto>> listarConsultasPendentes(@RequestParam Long idFuncionario) {
        List<ConsultaProntuarioResponseDto> consultasPendentes = service.listarConsultasPendentes(idFuncionario);

        if (consultasPendentes.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(200).body(consultasPendentes);
    }

    @GetMapping("/proxima")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ProximaConsultaProntuarioResponseDto> buscarProximaConsultaConfirmada(@RequestParam Long idPaciente) {
        ProximaConsultaProntuarioResponseDto proximaConsulta = service.buscarProximaConsultaConfirmada(idPaciente);
        return ResponseEntity.ok(proximaConsulta);
    }

    @GetMapping("/detalhes")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaProntuarioAtualResponseDto> buscarDetalhesConsultaAtual(@RequestParam Long idConsulta) {
        ConsultaProntuarioAtualResponseDto detalhes = service.buscarDetalhesConsultaAtual(idConsulta);
        return ResponseEntity.ok(detalhes);
    }

    @GetMapping("/detalhes-anterior")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<DetalhesConsultaProntuarioAnteriorResponseDto> buscarDetalhesConsultaAnterior(@RequestParam Long idConsulta) {
        DetalhesConsultaProntuarioAnteriorResponseDto detalhes = service.buscarDetalhesConsultaAnterior(idConsulta);
        return ResponseEntity.ok(detalhes);
    }

    @GetMapping("/ultimas-consultas")
    public ResponseEntity<Page<ConsultaProntuarioResponseDto>> listarUltimasConsultas(
            @RequestParam Long idPaciente,
            @RequestParam(defaultValue = "0") Integer pagina,
            @RequestParam Long idFuncionario
    ) {
        Pageable pageable = PageRequest.of(pagina, 8);

        Page<ConsultaProntuarioResponseDto> consultas = service
                .listarUltimasConsultas(idPaciente, idFuncionario, pageable);

        return ResponseEntity.ok(consultas);
    }
}
