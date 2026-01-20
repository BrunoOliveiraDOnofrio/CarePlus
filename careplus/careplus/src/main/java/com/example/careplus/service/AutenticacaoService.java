package com.example.careplus.service;

import com.example.careplus.dto.dtoFuncionario.FuncionarioDetalhesDto;
import com.example.careplus.model.Funcionario;
import com.example.careplus.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    // Metodo da interface implementada
    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {

        Funcionario funcionario = funcionarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        List<GrantedAuthority> authorities = funcionario.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getNome()))
                .collect(Collectors.toList());

        return new FuncionarioDetalhesDto(funcionario, authorities);
    }

}

