package com.example.careplus.service;

import com.example.careplus.model.Consulta;
import com.example.careplus.model.ConsultaRequest;
import com.example.careplus.model.Especialista;
import com.example.careplus.model.Usuario;
import com.example.careplus.repository.ConsultaRepository;
import com.example.careplus.repository.EspecialistaRepository;
import com.example.careplus.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(request.getUsuarioId());
        Usuario usuario;
        if (usuarioOpt.isPresent()) {
            usuario = usuarioOpt.get();
        } else {
            throw new RuntimeException("Usuário não encontrado!");
        }

        Optional<Especialista> especialistaOpt = especialistaRepository.findById(request.getEspecialistaId());
        Especialista especialista;
        if (especialistaOpt.isPresent()) {
            especialista = especialistaOpt.get();
        } else {
            throw new RuntimeException("Especialista não encontrado!");
        }

        Consulta consulta = new Consulta();
        consulta.setEspecialista(especialista);
        consulta.setUsuario(usuario);
        consulta.setDataHora(request.getDataHora());
        consulta.setStatus("Pendente");
        return consultaRepository.save(consulta);
    }

    public void removerConsulta(Long consultaId){
        Boolean consulta = consultaRepository.existsById(consultaId);
        if (!consulta){
            throw new RuntimeException("Consulta não encontrada");
        } else {
            consultaRepository.deleteById(consultaId);
        }
    }

    public List<Consulta> listarConsultas(){
        return consultaRepository.findAll();
    }

    public Consulta editarConsulta(Long consultaId, ConsultaRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        Especialista especialista = especialistaRepository.findById(request.getEspecialistaId())
                .orElseThrow(() -> new RuntimeException("Especialista não encontrado"));

        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        consulta.setUsuario(usuario);
        consulta.setEspecialista(especialista);
        consulta.setDataHora(request.getDataHora());
        consulta.setStatus("Pendente");

        return consultaRepository.save(consulta);
    }

}
