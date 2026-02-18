# CarePlus

Sistema de gestão para cuidados médicos desenvolvido em Spring Boot.

## 📋 Sobre o Projeto

CarePlus é um sistema completo de gestão de prontuários médicos, consultas e tratamentos, desenvolvido para facilitar o gerenciamento de informações clínicas.

## 🚀 Tecnologias Utilizadas

### Backend
- Java 21
- Spring Boot 3.3.3
- Maven
- MySQL

### API Testing
- Bruno (coleção de testes de API)

## 📁 Estrutura do Projeto

```
CarePlus/
├── src/                    # Código fonte Java
│   ├── main/              # Código da aplicação
│   └── test/              # Testes unitários
├── bruno-collection/       # Coleção de testes de API
│   ├── database/          # Scripts e diagramas do banco de dados
│   ├── Pacientes/         # Endpoints de pacientes
│   ├── Prontuarios/       # Endpoints de prontuários
│   ├── Consultas/         # Endpoints de consultas
│   └── ...                # Outros endpoints
├── webapp/                 # Recursos web (se aplicável)
├── pom.xml                # Configuração Maven
└── README.md              # Este arquivo
```

## 🛠️ Como Executar

### Pré-requisitos
- Java 21 ou superior
- Maven
- MySQL
- Bruno (opcional, para testes de API)

### Configuração do Banco de Dados
1. Execute os scripts SQL localizados em `bruno-collection/database/V2/`
2. Configure as credenciais do banco no arquivo `src/main/resources/application.properties`

### Executando o Backend
```bash
./mvnw spring-boot:run
```

Ou no Windows:
```bash
mvnw.cmd spring-boot:run
```

## 📚 Documentação da API

A documentação completa da API está disponível através da coleção Bruno em `bruno-collection/`.

### Principais Endpoints

- **Pacientes**: Gerenciamento de pacientes
- **Prontuários**: Gestão de fichas clínicas
- **Consultas**: Agendamento e gerenciamento de consultas
- **Funcionários**: Cadastro de profissionais
- **Medicações**: Controle de medicamentos
- **Tratamentos**: Gestão de tratamentos

## 👥 Equipe

Projeto desenvolvido por alunos do 4º semestre da SPTech.

## 📝 Licença

Este projeto é de uso acadêmico.

