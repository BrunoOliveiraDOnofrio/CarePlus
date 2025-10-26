package com.example.careplus.controller.dtoEspecialista;

import com.example.careplus.model.Especialista;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class EspecialistaDetalhesDto implements UserDetails {
    private final String nome;
    private final String email;
    private final String senha;

    public EspecialistaDetalhesDto(Especialista especialista) {
        this.nome = especialista.getNome();
        this.email = especialista.getEmail();
        this.senha = especialista.getSenha();
    }


    public String getNome() {
        return nome;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
