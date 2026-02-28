package com.example.careplus.controller;

import com.example.careplus.dto.dtoFuncionario.*;
import com.example.careplus.model.Funcionario;
import com.example.careplus.service.FuncionarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    public final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PostMapping(consumes = "multipart/form-data")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<FuncionarioResponseDto> salvarFuncionario(
            @ModelAttribute @Valid FuncionarioResquestDto funcionario
    ) {
        FuncionarioResponseDto funcionarioSalvo = funcionarioService.salvar(funcionario);
        return ResponseEntity.status(201).body(funcionarioSalvo);
    }

    @PutMapping(consumes = "multipart/form-data")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<FuncionarioResponseDto> atualizarFuncionario(
            @RequestParam Long idFuncionario,
            @ModelAttribute @Valid FuncionarioResquestDto funcionario
    ) {
        FuncionarioResponseDto funcionarioAtualizado = funcionarioService.atualizarFuncionario(funcionario, idFuncionario);
        return ResponseEntity.status(200).body(funcionarioAtualizado);
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

//    @PutMapping("/{id}")
//    @SecurityRequirement(name = "Bearer")
//    public ResponseEntity<?> atualizarFuncionario(@RequestBody FuncionarioResquestDto funcionario, @PathVariable Long id){
//            funcionarioService.atualizar(funcionario, id);
//            return ResponseEntity.status(200).build();
//    }

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

    @GetMapping("/nomesPorEspecialidade")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<FuncionarioResponseDto>> listarNomesPorEspecialidade(@RequestBody FuncionarioResquestDto especialidade){
        List<FuncionarioResponseDto> nomes = funcionarioService.nomesFuncionariosPorEspecialidade(especialidade.getEspecialidade());
        return ResponseEntity.status(200).body(nomes);
    }

    @GetMapping("/disponiveis")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<FuncionarioResponseDto>> buscarFuncionariosDisponiveis(
            @RequestBody FuncionarioDisponivelRequestDto requestDto){
        List<FuncionarioResponseDto> funcionarios = funcionarioService.buscarFuncionariosDisponiveis(
                requestDto.getEspecialidade(),
                requestDto.getDataHora());
        return ResponseEntity.status(200).body(funcionarios);
    }
}
