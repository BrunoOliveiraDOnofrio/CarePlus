package com.example.careplus.controller;

import com.example.careplus.dto.dtoEndereco.EnderecoRequestDto;
import com.example.careplus.dto.dtoEndereco.EnderecoResponseDto;
import com.example.careplus.service.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
@Tag(name = "Endereços", description = "API para gerenciamento de endereços")
public class EnderecoController {

    private final EnderecoService service;

    public EnderecoController(EnderecoService service) {
        this.service = service;
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Listar todos os endereços", description = "Retorna uma lista com todos os endereços cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de endereços retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum endereço encontrado")
    })
    public ResponseEntity<List<EnderecoResponseDto>> listarTodos() {
        List<EnderecoResponseDto> enderecos = service.listarTodos();

        if (enderecos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(enderecos);
    }

    @GetMapping("/por-id")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Buscar endereço por ID", description = "Retorna um endereço específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço encontrado"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    public ResponseEntity<EnderecoResponseDto> listarPorId(@RequestParam Long id) {
        EnderecoResponseDto endereco = service.listarPorId(id);
        return ResponseEntity.ok(endereco);
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Cadastrar novo endereço", description = "Cria um novo endereço no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<EnderecoResponseDto> cadastrar(@RequestBody EnderecoRequestDto enderecoDto) {
        try {
            EnderecoResponseDto enderecoSalvo = service.salvar(enderecoDto);
            return ResponseEntity.status(201).body(enderecoSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Atualizar endereço", description = "Atualiza os dados de um endereço existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    public ResponseEntity<EnderecoResponseDto> atualizar(@RequestBody EnderecoRequestDto enderecoDto, @RequestParam Long id) {
        try {
            EnderecoResponseDto enderecoAtualizado = service.atualizar(enderecoDto, id);
            return ResponseEntity.ok(enderecoAtualizado);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Deletar endereço", description = "Remove um endereço do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Endereço deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    public ResponseEntity<Void> deletar(@RequestParam Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/por-cidade")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Buscar endereços por cidade", description = "Retorna todos os endereços de uma cidade específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereços encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum endereço encontrado para a cidade")
    })
    public ResponseEntity<List<EnderecoResponseDto>> listarPorCidade(@RequestParam String cidade) {
        List<EnderecoResponseDto> enderecos = service.listarPorCidade(cidade);
        return ResponseEntity.ok(enderecos);
    }

    @GetMapping("/por-estado")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Buscar endereços por estado", description = "Retorna todos os endereços de um estado específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereços encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum endereço encontrado para o estado")
    })
    public ResponseEntity<List<EnderecoResponseDto>> listarPorEstado(@RequestParam String estado) {
        List<EnderecoResponseDto> enderecos = service.listarPorEstado(estado);
        return ResponseEntity.ok(enderecos);
    }

    @GetMapping("/por-cep")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Buscar endereço por CEP", description = "Retorna um endereço específico pelo CEP")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço encontrado"),
            @ApiResponse(responseCode = "404", description = "Nenhum endereço encontrado para o CEP")
    })
    public ResponseEntity<EnderecoResponseDto> buscarPorCep(@RequestParam String cep) {
        EnderecoResponseDto endereco = service.buscarPorCep(cep);
        return ResponseEntity.ok(endereco);
    }
}

