package com.example.careplus.service;

import com.example.careplus.model.Funcionario;
import com.example.careplus.repository.FuncionarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class FuncionarioServiceIntegrationTest {
    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Test
    @DisplayName("Deve deletar um funcionario e a lista ficar vazia")
    void deletar(){
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Teste Deletar");
        funcionario.setEmail("teste@deletar.com");

        funcionario = funcionarioRepository.save(funcionario);

        Assertions.assertEquals(1, funcionarioRepository.findAll().size());

        funcionarioService.deletar(funcionario.getId());

        Assertions.assertTrue(funcionarioRepository.findAll().isEmpty());

    }
}
