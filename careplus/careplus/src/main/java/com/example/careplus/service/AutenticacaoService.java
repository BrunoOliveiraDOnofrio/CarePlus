package com.example.careplus.service;

import com.example.careplus.controller.dtoFuncionario.FuncionarioDetalhesDto;
import com.example.careplus.model.Funcionario;
import com.example.careplus.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private FuncionarioRepository usuarioRepository;

    // Metodo da interface implementada
    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {

        Optional<Funcionario> usuarioOpt = usuarioRepository.findByEmail(username);

        if (usuarioOpt.isEmpty()){
            throw new UsernameNotFoundException(String.format("Usuário: %s não encontrado", username));
        }

        return new FuncionarioDetalhesDto(usuarioOpt.get());
    }

}

