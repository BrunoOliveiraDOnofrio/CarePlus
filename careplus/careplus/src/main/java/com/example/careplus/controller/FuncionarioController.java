package com.example.careplus.controller;

import com.example.careplus.controller.dtoFuncionario.*;
import com.example.careplus.model.Funcionario;
import com.example.careplus.service.FuncionarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    public final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<FuncionarioResponseDto> salvarFuncionario(@RequestBody @Valid FuncionarioResquestDto funcionario){
        FuncionarioResponseDto funcionarioSalvo = funcionarioService.salvar(funcionario);
        return ResponseEntity.status(201).body(funcionarioSalvo);
    }

    @PostMapping("/login")
    public ResponseEntity<FuncionarioTokenDto> login(@RequestBody FuncionarioLoginDto funcionarioLoginDto){

        final Funcionario funcionario = FuncionarioMapper.of(funcionarioLoginDto);
        FuncionarioTokenDto funcionarioTokenDto = this.funcionarioService.autenticar(funcionario);

        return ResponseEntity.status(200).body(funcionarioTokenDto);

    }

    @GetMapping("/por-email")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<FuncionarioResponseDto>> buscarPorEmail(@RequestParam String email){
        List<FuncionarioResponseDto> funcionarios = funcionarioService.buscarPorEmail(email);
        return ResponseEntity.status(200).body(funcionarios);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<FuncionarioResponseDto>> listarTodos(){
        List<FuncionarioResponseDto> funcionarios = funcionarioService.listarTodos();
        return ResponseEntity.status(200).body(funcionarios);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity deletarFuncionario(@PathVariable Long id){
        try{
            funcionarioService.deletar(id);
            return ResponseEntity.status(200).build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<?> atualizarFuncionario(@RequestBody FuncionarioResquestDto funcionario, @PathVariable Long id){
            funcionarioService.atualizar(funcionario, id);
            return ResponseEntity.status(200).build();
    }

    @GetMapping("/subordinados/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<FuncionarioResponseDto>> listarTodosSubordinados(@PathVariable Long id){
        List<FuncionarioResponseDto> funcionarios = funcionarioService.listarSubordinados(id, funcionarioService.buscarTodos());
        return ResponseEntity.status(200).body(funcionarios);
    }

    @GetMapping("/especialidades")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<String>> listarEspecialidades(){
        List<String> especialidades = funcionarioService.listarEspecialidades();
        return ResponseEntity.status(200).body(especialidades);
    }

    @GetMapping("/nomesPorEspecialidade/{especialidade}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<String>> listarNomesPorEspecialidade(@PathVariable String especialidade){
        List<String> nomes = funcionarioService.nomesFuncionariosPorEspecialidade(especialidade);
        return ResponseEntity.status(200).body(nomes);
    }

    @GetMapping("/{id}/horarios-disponiveis")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<String>> buscarHorariosDisponiveis(
            @PathVariable Long id,
            @RequestParam String data){
        LocalDate dataConsulta = LocalDate.parse(data);
        List<String> horarios = funcionarioService.buscarHorariosDisponiveis(id, dataConsulta);
        return ResponseEntity.status(200).body(horarios);
    }
}
