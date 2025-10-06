package com.example.careplus.controller;

import com.example.careplus.controller.dtoPaciente.PacienteMapper;
import com.example.careplus.controller.dtoPaciente.PacienteRequestDto;
import com.example.careplus.controller.dtoPaciente.PacienteResponseDto;
import com.example.careplus.model.Paciente;
import com.example.careplus.service.PacienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("usuarios")
public class PacienteController {
//    List<Usuario> usuarios = new ArrayList<>();

    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    @GetMapping // OK
    public ResponseEntity<List<PacienteResponseDto>> listarUsuarios(){
        List<PacienteResponseDto> pacientes = service.listarTodos();

        if(pacientes.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(pacientes);
    }

    @GetMapping("/{id}") // OK
    public ResponseEntity<PacienteResponseDto> listarPorId(@PathVariable Long id){

            PacienteResponseDto paciente = service.listarPorId(id);
            return ResponseEntity.ok().body(paciente);

    }

    @PostMapping // OK
    public ResponseEntity<PacienteResponseDto> cadastrarUsuario(@RequestBody PacienteRequestDto paciente){
        try {
            PacienteResponseDto pacienteSalvo = service.salvar(paciente);

            return ResponseEntity.status(201).body(pacienteSalvo);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}") // OK
    public ResponseEntity deletarUsuario(@PathVariable Long id){
        try{
            service.deletar(id);
            return ResponseEntity.status(204).build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}") //OK
    public ResponseEntity<?> atualizarUsuario(@RequestBody PacienteRequestDto paciente, @PathVariable Long id){
        try{
            service.atualizar(paciente, id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/por-email") // OK
    public ResponseEntity<List<PacienteResponseDto>> listarPorEmail(@RequestParam String email){
        List<PacienteResponseDto> paciente = service.listarPorEmail(email);
        return ResponseEntity.ok().body(paciente);

    }


}

