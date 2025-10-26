package com.example.careplus.utils;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Projeto Solução Clínica",
                description = "Sistema de agendamentos e prontuários",
                contact = @Contact(
                        name = "CARE+",
                        url = "https://github.com/BrunoOliveiraDOnofrio/CarePlus",
                        email = "gabriel.osantos@sptech.school"
                ),
                license = @License(name = "UNLICENSED"),
                version = "1.0.0"
        )
)

public class OpenApiConfig {
}
