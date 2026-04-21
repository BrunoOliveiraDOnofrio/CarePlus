package com.example.careplus.controller;

import com.example.careplus.dto.dtoRelatorio.FuncionariosPorEspecialidadeDto;
import com.example.careplus.dto.dtoRelatorio.PacientesPorConvenioDto;
import com.example.careplus.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("relatorios")
@Tag(name = "Relatórios", description = "Endpoints para gerar relatórios e estatísticas")
public class RelatorioController {

    private final RelatorioService service;

    public RelatorioController(RelatorioService service) {
        this.service = service;
    }

    @GetMapping("/pacientes-sem-consulta")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Contar pacientes ativos sem consulta", description = "Retorna a contagem de pacientes ativos que nunca tiveram uma consulta")
    public ResponseEntity<Long> contarPacientesAtivosSemConsulta() {
        long count = service.obterContagemPacientesSemConsulta();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/funcionarios-por-especialidade")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Listar funcionários por especialidade", description = "Retorna estatísticas de funcionários agrupados por especialidade/setor com total de pacientes atendidos em cada setor")
    public ResponseEntity<List<FuncionariosPorEspecialidadeDto>> listarFuncionariosPorEspecialidade() {
        List<FuncionariosPorEspecialidadeDto> funcionarios = service.obterFuncionariosPorEspecialidade();

        if (funcionarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(funcionarios);
    }

    @GetMapping("/pacientes-por-convenio")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Listar pacientes por convênio", description = "Retorna estatísticas de pacientes agrupados por convênio/seguradora, ordenado por total de clientes em ordem decrescente")
    public ResponseEntity<List<PacientesPorConvenioDto>> listarPacientesPorConvenio() {
        List<PacientesPorConvenioDto> pacientes = service.obterPacientesPorConvenio();

        if (pacientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(pacientes);
    }
}
