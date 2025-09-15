package com.example.careplus.service;

import com.example.careplus.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.example.careplus.exception.MissingFieldException;
import com.example.careplus.model.Usuario;
import com.example.careplus.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<Usuario> listarTodos(){
        return repository.findAll();
    }

    public Usuario listarPorId(Integer id){
        Optional<Usuario> existeUsuario = repository.findById(id);

        if(existeUsuario.isPresent()){
            return existeUsuario.get();
        }

        throw new ResourceNotFoundException("Usuário não encontrado!");

    }

    public Usuario salvar(Usuario usuario){

        if(usuario.getEmail() == null){
            throw new MissingFieldException("email");
        }


        if (usuario.getEmail().endsWith("@edu")){
            throw new IllegalArgumentException("Domínido educacional");
        }

        if (repository.existsByEmail(usuario.getEmail())){
            throw new IllegalStateException("Usuario com este email ja existe");
        }

        return repository.save(usuario);
    }

    public void deletar(Integer id){
        boolean existe = repository.existsById(id);

        if(!existe){
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
        repository.deleteById(id);
    }

    public Usuario atualizar(Usuario usuario, Integer id){
        Optional<Usuario> existe = repository.findById(id);

        if(existe.isPresent()){
            Usuario usuarioExistente = existe.get();

            usuarioExistente.setNome(usuario.getNome());
            usuarioExistente.setEmail(usuario.getEmail());
            usuarioExistente.setCargo(usuario.getCargo());
            usuarioExistente.setDtNascimento(usuario.getDtNascimento());
            usuarioExistente.setTelefone(usuario.getTelefone());
            return repository.save(usuarioExistente);
        }else {
            throw new RuntimeException("Usuário não encontrado");
        }
    }

    public List<Usuario> listarPorEmail(String email){
        List<Usuario> existeEmail = repository.findByEmailContainsIgnoreCase(email);

        if(existeEmail.isEmpty()){
            throw new ResourceNotFoundException("Email não existe");
        }
        return existeEmail;
    }


}

