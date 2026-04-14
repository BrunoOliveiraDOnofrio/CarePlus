package com.example.careplus.controller;

import com.example.careplus.exception.ResourceNotFoundException;
import com.example.careplus.model.ConsultaFuncionario;
import com.example.careplus.model.ConsultaProntuario;
import com.example.careplus.model.Funcionario;
import com.example.careplus.repository.ConsultaFuncionarioRepository;
import com.example.careplus.repository.ConsultaProntuarioRepository;
import com.example.careplus.repository.FuncionarioRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("consulta-funcionario")
public class ConsultaFuncionarioController {

    private final ConsultaFuncionarioRepository repository;
    private final ConsultaProntuarioRepository consultaRepository;
    private final FuncionarioRepository funcionarioRepository;

    public ConsultaFuncionarioController(ConsultaFuncionarioRepository repository,
                                         ConsultaProntuarioRepository consultaRepository,
                                         FuncionarioRepository funcionarioRepository) {
        this.repository = repository;
        this.consultaRepository = consultaRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaFuncionario>> listarTodos() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/por-consulta")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaFuncionario>> listarPorConsulta(@RequestParam Long consultaId) {
        List<ConsultaFuncionario> resultado = repository.findByConsultaId(consultaId);
        if (resultado.isEmpty()) return ResponseEntity.status(404).build();
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/por-funcionario")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<ConsultaFuncionario>> listarPorFuncionario(@RequestParam Long funcionarioId) {
        List<ConsultaFuncionario> resultado = repository.findByFuncionarioId(funcionarioId);
        if (resultado.isEmpty()) return ResponseEntity.status(404).build();
        return ResponseEntity.ok(resultado);
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<ConsultaFuncionario> adicionarFuncionario(@RequestBody Map<String, Long> body) {
        Long consultaId = body.get("consultaId");
        Long funcionarioId = body.get("funcionarioId");

        ConsultaProntuario consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));
        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));

        if (repository.existsByConsultaIdAndFuncionarioId(consultaId, funcionarioId)) {
            return ResponseEntity.status(409).build();
        }

        ConsultaFuncionario vinculo = new ConsultaFuncionario(funcionario, consulta);
        return ResponseEntity.status(201).body(repository.save(vinculo));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.status(404).build();
        repository.deleteById(id);
        return ResponseEntity.status(204).build();
    }
}

