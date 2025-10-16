package com.example.careplus.controller.dtoTratamento;

import com.example.careplus.model.Medicacao;
import com.example.careplus.service.MedicacaoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicacoes")
public class MedicacaoController {

    private final MedicacaoService service;

    public MedicacaoController(MedicacaoService service) {
        this.service = service;
    }
    @PostMapping
    public void adicionar(@RequestBody Medicacao medicacao) {
        service.adicionar(medicacao);
    }

    @GetMapping("/ativas")
    public long contarAtivas() {
        return service.contarAtivas();
    }

    @GetMapping("/ordenadas")
    public List<Medicacao> listarOrdenadasPorNome() {
        return service.listarOrdenadasPorNome();
    }

    @GetMapping("/tempo")
    public List<Medicacao> ordenarPorTempo() {
        return service.ordenarPorTempoMedicando();
    }
}
