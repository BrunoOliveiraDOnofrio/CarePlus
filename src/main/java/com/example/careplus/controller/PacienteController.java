package com.example.careplus.controller;

import com.example.careplus.dto.dtoPaciente.PacienteRequestDto;
import com.example.careplus.dto.dtoPaciente.PacienteResponseDto;
import com.example.careplus.dto.dtoPaciente.TudoPacienteDto;
import com.example.careplus.service.PacienteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("pacientes")
public class PacienteController {
//    List<Usuario> usuarios = new ArrayList<>();

    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    @GetMapping // OK
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PacienteResponseDto>> listarPacientes(){
        List<PacienteResponseDto> pacientes = service.listarTodos();

        if(pacientes.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(pacientes);
    }

    @GetMapping("/por-id") // OK
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PacienteResponseDto> listarPorId(@RequestParam Long id){
            PacienteResponseDto paciente = service.listarPorId(id);
            return ResponseEntity.ok().body(paciente);
    }

    @PostMapping(consumes = "multipart/form-data") // OK
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<PacienteResponseDto> cadastrarPaciente(@ModelAttribute @Valid PacienteRequestDto paciente){
        try {
            PacienteResponseDto pacienteSalvo = service.salvar(paciente);
            return ResponseEntity.status(201).body(pacienteSalvo);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping // OK
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity deletarPaciente(@RequestParam Long id){
        try{
            service.deletar(id);
            return ResponseEntity.status(204).build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping(consumes = "multipart/form-data") //OK
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> atualizarPaciente(@ModelAttribute @Valid PacienteRequestDto paciente, @RequestParam Long id){
        try{
            service.atualizar(paciente, id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/foto")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<byte[]> buscarFotoPorCpf(@RequestParam String cpf) {
        try {
            byte[] foto = service.buscarFoto(cpf);
            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg")
                    .body(foto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/por-email") // OK
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PacienteResponseDto>> listarPorEmail(@RequestParam String email){
        List<PacienteResponseDto> paciente = service.listarPorEmail(email);
        return ResponseEntity.ok().body(paciente);
    }

    @GetMapping("/por-nome")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PacienteResponseDto>> buscarPorNome(@RequestParam String nome){
        List<PacienteResponseDto> pacientes = service.buscarPorNome(nome);
        return ResponseEntity.status(200).body(pacientes);
    }

    @GetMapping("/buscar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<PacienteResponseDto>> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String cpf) {
        List<PacienteResponseDto> pacientes = service.buscar(nome, email, cpf);
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/todos-pacientes")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Page<PacienteResponseDto>> listarTodosPacientesPaginado(
            @RequestParam(defaultValue = "0") Integer pagina) {
        Pageable pageable = PageRequest.of(pagina, 8);
        Page<PacienteResponseDto> pacientes = service.listarTodosPaginado(pageable);
        return ResponseEntity.status(200).body(pacientes);
    }

    @GetMapping("/inativos")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Page<PacienteResponseDto>> listarPacientesInativosPaginado(
            @RequestParam(defaultValue = "0") Integer pagina) {
        Pageable pageable = PageRequest.of(pagina, 8);
        Page<PacienteResponseDto> pacientes = service.listarInativosPaginado(pageable);
        return ResponseEntity.status(200).body(pacientes);
    }

    @PatchMapping("/reativar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> reativarPaciente(@RequestParam Long id) {
        service.reativar(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/todos-pacientes-funcionario")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Page<PacienteResponseDto>> listarTodosPacientesPaginado(
            @RequestParam(defaultValue = "0") Integer pagina,
            @RequestParam Long idFuncionario) {
        Pageable pageable = PageRequest.of(pagina, 8);
        Page<PacienteResponseDto> pacientes = service.listarTodosPaginadoPorFuncionario(pageable, idFuncionario);
        return ResponseEntity.status(200).body(pacientes);
    }


    @PostMapping(path = "/formCadastro", consumes = "multipart/form-data")
    public ResponseEntity<PacienteResponseDto> teste(
            @ModelAttribute TudoPacienteDto dto
    ) {
        PacienteResponseDto pacienteResponseDto = service.cadastrarPacienteFormulario(dto);
        return ResponseEntity.status(200).body(pacienteResponseDto);
    }

}

