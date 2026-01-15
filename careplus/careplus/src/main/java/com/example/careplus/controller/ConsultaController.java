package com.example.careplus.controller;

import com.example.careplus.controller.dtoConsulta.ConsultaRequestDto;
import com.example.careplus.controller.dtoConsulta.ConsultaResponseDto;
import com.example.careplus.controller.dtoConsultaRecorrente.ConsultaRecorrenteRequestDto;
import com.example.careplus.controller.dtoConsultaRecorrente.ConsultaRecorrenteResponseDto;
import com.example.careplus.model.Consulta;
import com.example.careplus.service.ConsultaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("consultas")
public class ConsultaController {
    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaResponseDto> marcarConsulta(@RequestBody ConsultaRequestDto request) {
        ConsultaResponseDto consulta = service.marcarConsulta(request);
        return ResponseEntity.ok(consulta);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> deletarConsulta(@PathVariable Long id){
        try{
            service.removerConsulta(id);
            return ResponseEntity.status(204).build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<Consulta>> listarConsultas(){
        List<Consulta> consultas = service.listarConsultas();
        if (consultas.isEmpty()){
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(200).body(consultas);
        }
    }

    @GetMapping("/por-data")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaResponseDto>> listarPorData(){
        List<ConsultaResponseDto> consultas = service.listarPorData();

        if (consultas.isEmpty()){
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(200).body(consultas);
        }

    }

    @GetMapping("/por-paciente")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaResponseDto>> listarPorPaciente(@RequestParam Long idPaciente){
        List<ConsultaResponseDto> consultas = service.listarPorPaciente(idPaciente);

        if (consultas.isEmpty()){
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(200).body(consultas);
        }

    }

//    @PutMapping("/{id}")
//    @SecurityRequirement(name = "Bearer")
//    public ResponseEntity<ConsultaResponseDto> editarConsulta(
//            @PathVariable Long id,
//            @RequestBody ConsultaRequest request) {
//
//        ConsultaResponseDto consultaEditada = service.editarConsulta(id, request);
//        return ResponseEntity.status(200).body(consultaEditada);
//    }

    @PutMapping("/confirmar/{idConsulta}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaResponseDto> confirmarConsulta(@PathVariable Long idConsulta, @RequestBody Boolean status){
        ConsultaResponseDto consultaResponseDto = service.confirmarConsulta(idConsulta, status);
        return ResponseEntity.status(200).body(consultaResponseDto);
    }

    @PutMapping("/observacoes/{idConsulta}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaResponseDto> editarObervacoes(@PathVariable Long idConsulta, @RequestBody String obs){
        ConsultaResponseDto consultaResponseDto = service.salvarObservacoes(idConsulta, obs);
        return ResponseEntity.status(200).body(consultaResponseDto);
    }

    @GetMapping("/consultasDoDia/{idFuncionario}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaResponseDto>> buscarConsultasDoDia(@PathVariable Long idFuncionario){
        List<ConsultaResponseDto> consultaResponseDtos = service.consultasDoDia(idFuncionario);
        return ResponseEntity.status(200).body(consultaResponseDtos);
    }

    @PostMapping("/recorrentes")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaRecorrenteResponseDto> criarConsultasRecorrentes(
            @RequestBody ConsultaRecorrenteRequestDto request) {
        ConsultaRecorrenteResponseDto response = service.criarConsultasRecorrentes(request);
        return ResponseEntity.status(207).body(response);
    }

}
