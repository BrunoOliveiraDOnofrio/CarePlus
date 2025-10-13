package com.example.careplus.controller.dtoResponsavel;

import com.example.careplus.model.Medicacao;
import com.example.careplus.service.MedicacaoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicacacoes")
public class MedicacaoController {

    private final MedicacaoService service = new MedicacaoService();

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
