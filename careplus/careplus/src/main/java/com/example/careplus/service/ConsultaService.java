package com.example.careplus.service;

import com.example.careplus.model.Consulta;
import com.example.careplus.model.ConsultaRequest;
import com.example.careplus.model.Especialista;
import com.example.careplus.model.Usuario;
import com.example.careplus.repository.ConsultaRepository;
import com.example.careplus.repository.EspecialistaRepository;
import com.example.careplus.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EspecialistaRepository especialistaRepository;

    public ConsultaService(ConsultaRepository consultaRepository, UsuarioRepository usuarioRepository, EspecialistaRepository especialistaRepository) {
        this.consultaRepository = consultaRepository;
        this.usuarioRepository = usuarioRepository;
        this.especialistaRepository = especialistaRepository;
    }

    //montando a Consulta a partir do ConsultaRequest
    public Consulta marcarConsulta(ConsultaRequest request){
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        Especialista especialista = especialistaRepository.findById(request.getEspecialistaId())
                .orElseThrow(() -> new RuntimeException("Especialista não encontrado"));

        Consulta consulta = new Consulta();
        consulta.setEspecialista(especialista);
        consulta.setUsuario(usuario);
        consulta.setDataHora(request.getDataHora());
        consulta.setStatus("Pedente");
        return consultaRepository.save(consulta);
    }
}
