# CURLs — Marcação e Gestão de Consultas

> **Token:** substitua `<TOKEN>` pelo token JWT obtido no login.  
> **Base URL:** `http://localhost:8080`

---

## consultas-prontuario

### 1. Marcar Consulta

```bash
curl -s -X POST http://localhost:8080/consultas-prontuario \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "pacienteId": 1,
    "funcionarioId": 2,
    "dataHora": "2097-05-18 14:00:00",
    "tipo": "Retorno"
  }'
```

**Resposta esperada (200):**
```json
{
  "id": 1,
  "paciente": { "id": 1, "nome": "Lucas Silva" },
  "funcionario": { "id": 2, "nome": "Juliana", "especialidade": "Fonoaudiologia" },
  "dataHora": "2097-05-18 14:00:00",
  "tipo": "Retorno",
  "confirmada": null
}
```

---

### 2. Revisar consulta antes de confirmar (preview)

```bash
curl -s -X GET http://localhost:8080/consultas-prontuario/revisar \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "pacienteId": 1,
    "funcionarioId": 2,
    "dataHora": "2097-05-18 14:00:00",
    "tipo": "Retorno"
  }'
```

---

### 3. Marcar Consultas Recorrentes (ou únicas) em lote

O endpoint `/recorrentes` aceita um `pacienteId` e uma lista de blocos de consultas (`consultas`).  
Cada bloco pode ter **um único funcionário** (`funcionarioId`) **ou uma lista** (`funcionarioIds`).  
As datas são geradas **semanalmente** entre `dataInicio` e `dataFim` (mesmo dia da semana).  
Para **consulta única**, informe `dataInicio == dataFim`.

---

#### Cenário A — 1 bloco com 1 funcionário (consulta única ou recorrente simples)

```bash
curl -s -X POST http://localhost:8080/consultas-prontuario/recorrentes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "pacienteId": 3,
    "consultas": [
      {
        "funcionarioId": 1,
        "horarioInicio": "10:00:00",
        "horarioFim": "11:00:00",
        "dataInicio": "07/04/2026",
        "dataFim": "07/04/2027",
        "tipo": "Rotina"
      }
    ]
  }'
```

> Gera uma consulta por semana às terças-feiras (dia da semana de 07/04/2026), do início ao fim do período, com 1 funcionário vinculado.

---

#### Cenário B — 3 blocos, cada um com 2 funcionários

```bash
curl -s -X POST http://localhost:8080/consultas-prontuario/recorrentes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "pacienteId": 3,
    "consultas": [
      {
        "funcionarioIds": [1, 2],
        "horarioInicio": "08:00:00",
        "horarioFim": "09:00:00",
        "dataInicio": "07/04/2026",
        "dataFim": "07/04/2027",
        "tipo": "Rotina"
      },
      {
        "funcionarioIds": [3, 4],
        "horarioInicio": "10:00:00",
        "horarioFim": "11:00:00",
        "dataInicio": "08/04/2026",
        "dataFim": "08/04/2027",
        "tipo": "Rotina"
      },
      {
        "funcionarioIds": [5, 6],
        "horarioInicio": "14:00:00",
        "horarioFim": "15:00:00",
        "dataInicio": "09/04/2026",
        "dataFim": "09/04/2027",
        "tipo": "Rotina"
      }
    ]
  }'
```

> 3 blocos recorrentes semanais. Em cada data gerada, **2 funcionários** são vinculados à mesma consulta.

---

#### Cenário C — 3 blocos, modo misto (1 com 1 funcionário, 2 com 2 funcionários)

```bash
curl -s -X POST http://localhost:8080/consultas-prontuario/recorrentes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "pacienteId": 3,
    "consultas": [
      {
        "funcionarioId": 1,
        "horarioInicio": "08:00:00",
        "horarioFim": "09:00:00",
        "dataInicio": "07/04/2026",
        "dataFim": "07/04/2027",
        "tipo": "Rotina"
      },
      {
        "funcionarioIds": [2, 3],
        "horarioInicio": "10:00:00",
        "horarioFim": "11:00:00",
        "dataInicio": "08/04/2026",
        "dataFim": "08/04/2027",
        "tipo": "Rotina"
      },
      {
        "funcionarioIds": [4, 5],
        "horarioInicio": "14:00:00",
        "horarioFim": "15:00:00",
        "dataInicio": "09/04/2026",
        "dataFim": "09/04/2027",
        "tipo": "Rotina"
      }
    ]
  }'
```

> 1º bloco com 1 funcionário, 2º e 3º com 2 funcionários cada.

**Resposta esperada (207 Multi-Status):**
```json
{
  "totalConsultasCriadas": 156,
  "totalFalhas": 0,
  "consultasCriadas": [ ... ],
  "datasComConflito": []
}
```

---

### 4. Confirmar consulta

```bash
curl -s -X PUT "http://localhost:8080/consultas-prontuario/confirmar?idConsulta=1" \
  -H "Authorization: Bearer <TOKEN>"
```

---

### 5. Recusar consulta

```bash
curl -s -X PUT "http://localhost:8080/consultas-prontuario/recusar?idConsulta=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '"Horário incompatível com a agenda do paciente"'
```

---

### 6. Realizar Observações (registrar consulta)

```bash
curl -s -X PUT "http://localhost:8080/consultas-prontuario/realizarObservacoes?idConsulta=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "observacao": "Paciente apresentou boa evolução"
  }'
```

---

### 7. Deletar consulta

```bash
curl -s -X DELETE "http://localhost:8080/consultas-prontuario?id=1" \
  -H "Authorization: Bearer <TOKEN>"
```

**Resposta esperada:** `204 No Content`

---

### 8. Listar todas as consultas

```bash
curl -s -X GET http://localhost:8080/consultas-prontuario \
  -H "Authorization: Bearer <TOKEN>"
```

---

### 9. Listar consultas ordenadas por data

```bash
curl -s -X GET http://localhost:8080/consultas-prontuario/por-data \
  -H "Authorization: Bearer <TOKEN>"
```

---

### 10. Listar consultas por paciente

```bash
curl -s -X GET "http://localhost:8080/consultas-prontuario/por-paciente?idPaciente=1" \
  -H "Authorization: Bearer <TOKEN>"
```

---

### 11. Listar consultas do dia por funcionário

```bash
curl -s -X GET "http://localhost:8080/consultas-prontuario/consultasDoDia?idFuncionario=2" \
  -H "Authorization: Bearer <TOKEN>"
```

---

### 12. Listar agenda semanal por funcionário

```bash
curl -s -X GET "http://localhost:8080/consultas-prontuario/agenda-semanal?funcionarioId=2&dataReferencia=2097-05-18" \
  -H "Authorization: Bearer <TOKEN>"
```

> `dataReferencia` deve estar no formato `yyyy-MM-dd`. Retorna as consultas da semana que contém a data informada.

---

### 13. Listar consultas pendentes de confirmação por funcionário

```bash
curl -s -X GET "http://localhost:8080/consultas-prontuario/pendentes?idFuncionario=2" \
  -H "Authorization: Bearer <TOKEN>"
```

---

### 14. Buscar próxima consulta confirmada de um paciente

```bash
curl -s -X GET "http://localhost:8080/consultas-prontuario/proxima?idPaciente=1" \
  -H "Authorization: Bearer <TOKEN>"
```

**Resposta esperada (200):**
```json
{
  "consultaId": 5,
  "data": "2097-06-02",
  "horarioInicio": "10:00:00",
  "horarioFim": "11:00:00",
  "tipo": "Sessão Terapêutica",
  "nomeProfissional": "Juliana",
  "tratamento": "ABA"
}
```

---

### 15. Detalhes da consulta atual (tela de atendimento)

```bash
curl -s -X GET "http://localhost:8080/consultas-prontuario/detalhes?idConsulta=1" \
  -H "Authorization: Bearer <TOKEN>"
```

---

### 16. Detalhes da consulta anterior

```bash
curl -s -X GET "http://localhost:8080/consultas-prontuario/detalhes-anterior?idConsulta=1" \
  -H "Authorization: Bearer <TOKEN>"
```

---

## consulta-funcionario

### 17. Adicionar funcionário a uma consulta

```bash
curl -s -X POST http://localhost:8080/consulta-funcionario \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "consultaId": 1,
    "funcionarioId": 3
  }'
```

**Resposta esperada:** `201 Created` com o vínculo criado.  
> Retorna **409 Conflict** se o funcionário já estiver vinculado à consulta.

---

### 18. Listar todos os vínculos consulta-funcionário

```bash
curl -s -X GET http://localhost:8080/consulta-funcionario \
  -H "Authorization: Bearer <TOKEN>"
```

---

### 19. Listar funcionários de uma consulta

```bash
curl -s -X GET "http://localhost:8080/consulta-funcionario/por-consulta?consultaId=1" \
  -H "Authorization: Bearer <TOKEN>"
```

---

### 20. Listar consultas de um funcionário

```bash
curl -s -X GET "http://localhost:8080/consulta-funcionario/por-funcionario?funcionarioId=2" \
  -H "Authorization: Bearer <TOKEN>"
```

---

### 21. Deletar vínculo consulta-funcionário

```bash
curl -s -X DELETE "http://localhost:8080/consulta-funcionario/1" \
  -H "Authorization: Bearer <TOKEN>"
```

**Resposta esperada:** `204 No Content`

---

## Campos do body de marcação (`POST /consultas-prontuario`)

| Campo         | Tipo                | Obrigatório | Exemplo             | Descrição                   |
|---------------|---------------------|-------------|---------------------|-----------------------------|
| pacienteId    | Long                | ✅           | `1`                 | ID do paciente              |
| funcionarioId | Long                | ✅           | `2`                 | ID do funcionário principal |
| data          | yyyy-MM-dd          | ✅           | `"2026-04-13"`      | Data da consulta            |
| horarioInicio | HH:mm:ss            | ✅           | `"14:00:00"`        | Horário de início           |
| horarioFim    | HH:mm:ss            | ❌           | `"15:00:00"`        | Horário de fim              |
| tipo          | String              | ✅           | `"Retorno"`         | Tipo da consulta            |

## Campos do body de consultas em lote (`POST /consultas-prontuario/recorrentes`)

### Objeto raiz

| Campo      | Tipo                          | Obrigatório | Descrição               |
|------------|-------------------------------|-------------|-------------------------|
| pacienteId | Long                          | ✅           | ID do paciente          |
| consultas  | List\<ConsultaItemRequestDto\>| ✅           | Lista de blocos         |

### Cada item da lista (`consultas[]`)

| Campo          | Tipo              | Obrigatório         | Exemplo                      | Descrição                                                                 |
|----------------|-------------------|---------------------|------------------------------|---------------------------------------------------------------------------|
| funcionarioId  | Long              | ✅ (ou funcionarioIds) | `1`                       | ID de um único funcionário                                                |
| funcionarioIds | List\<Long\>      | ✅ (ou funcionarioId)  | `[1, 2]`                  | IDs de múltiplos funcionários — todos vinculados a cada sessão            |
| horarioInicio  | HH:mm:ss          | ✅                   | `"10:00:00"`                 | Horário de início                                                         |
| horarioFim     | HH:mm:ss          | ❌                   | `"11:00:00"`                 | Horário de fim                                                            |
| dataInicio     | dd/MM/yyyy        | ✅                   | `"07/04/2026"`               | Início da recorrência (ou data única)                                     |
| dataFim        | dd/MM/yyyy        | ✅                   | `"07/04/2027"`               | Fim da recorrência. Igual a `dataInicio` para consulta única              |
| tipo           | String            | ❌                   | `"Rotina"`                   | Tipo da consulta                                                          |

> **Geração de datas:** semanal, mesmo dia da semana de `dataInicio`, até `dataFim` (inclusive).  
> **Consulta única:** informe `dataInicio == dataFim`.
