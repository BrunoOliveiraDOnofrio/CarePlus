package com.example.careplus.config;

import com.example.careplus.model.Funcionario;
import com.example.careplus.repository.FuncionarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UsuarioDetailsService implements UserDetailsService {

    private FuncionarioRepository repository;

    public UsuarioDetailsService(FuncionarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Funcionario funcionario = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Funcionário não encontrado"));

        return new UsuarioDetails(funcionario);
    }


}
