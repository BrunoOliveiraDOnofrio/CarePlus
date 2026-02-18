# Database - CarePlus

## 📁 Versões do Banco de Dados

### V1 - Diagramas
Contém os diagramas do MySQL Workbench (arquivos .mwb) com a evolução do modelo de dados.

**Arquivos:**
- `DER-V2.mwb` - Diagrama Entidade-Relacionamento versão 2
- `DER-V3.mwb` - Diagrama Entidade-Relacionamento versão 3
- `DER-V4.mwb` - Diagrama Entidade-Relacionamento versão 4
- `DiagramaV5.mwb` - Diagrama mais recente (versão 5)

### V2 - Scripts SQL
Contém os scripts de criação e população do banco de dados.

**Arquivos:**
- `bd_v2.sql` - Script de criação das tabelas
- `inserts.sql` - Script de inserção de dados de teste

## 🚀 Como Usar

### Opção 1: Usando os Scripts SQL

1. Abra o MySQL Workbench ou seu cliente MySQL preferido
2. Execute o script de criação:
   ```sql
   source V2/bd_v2.sql
   ```
3. Execute o script de inserção:
   ```sql
   source V2/inserts.sql
   ```

### Opção 2: Usando o Diagrama

1. Abra o MySQL Workbench
2. Abra o arquivo `V1/DiagramaV5.mwb` (versão mais recente)
3. Vá em **Database > Forward Engineer** para gerar o banco

## 📊 Modelo de Dados

O banco de dados inclui as seguintes entidades principais:

- **Pacientes** - Dados cadastrais dos pacientes
- **Prontuários/Fichas Clínicas** - Informações médicas dos pacientes
- **Consultas** - Agendamento e histórico de consultas
- **Funcionários** - Profissionais de saúde e administrativos
- **Medicações** - Medicamentos prescritos
- **Tratamentos** - Tratamentos em andamento
- **Cuidadores/Responsáveis** - Responsáveis pelos pacientes
- **Endereços** - Endereços de pacientes e funcionários
- **Classificação de Doenças (CID)** - Classificação internacional de doenças

## ⚙️ Configuração da Aplicação

Após criar o banco de dados, configure a conexão no arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/careplus
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

