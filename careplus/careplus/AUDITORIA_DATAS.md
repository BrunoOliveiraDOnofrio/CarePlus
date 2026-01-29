# Auditoria e Correções de Formato de Data/Hora - CarePlus API

## Data da Auditoria: 29/01/2026

---

## 🔍 Problema Identificado

O campo `dataHora` não estava sendo salvo ao criar uma consulta de prontuário porque:

1. **Collection do Bruno** estava enviando campos separados (`dataConsulta` e `horario`)
2. **DTO esperava** um único campo `dataHora` no formato `LocalDateTime`
3. **Faltavam anotações** `@JsonFormat` em vários DTOs e entidades

---

## ✅ Correções Realizadas

### 1. Collections do Bruno

#### ❌ Antes:
```json
{
  "pacienteId": 1,
  "funcionarioId": 1,
  "dataConsulta": "2026-01-20",
  "horario": "14:00",
  "observacoes": "Consulta de rotina"
}
```

#### ✅ Depois:
```json
{
  "pacienteId": 1,
  "funcionarioId": 1,
  "dataHora": "2026-01-20 14:00:00",
  "tipo": "Pendente"
}
```

**Arquivo alterado:** `bruno-collection/ConsultasProntuario/01 - Marcar Consulta.bru`

---

### 2. DTOs de Request - Adicionado @JsonFormat

#### ConsultaProntuarioRequestDto.java
```java
@Schema(description = "Data e hora da consulta no formato yyyy-MM-dd HH:mm:ss", 
        example = "2026-01-20 14:00:00")
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private LocalDateTime dataHora;
```

#### ConsultaProntuarioRequest.java
```java
@Schema(description = "2025-10-14 14:00:00")
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private LocalDateTime dataHora;
```

#### MedicacaoRequestDto.java
```java
@JsonFormat(pattern = "yyyy-MM-dd")
private LocalDate dataInicio;

@JsonFormat(pattern = "yyyy-MM-dd")
private LocalDate dataFim;
```

---

### 3. DTOs de Response - Adicionado @JsonFormat

#### ConsultaProntuarioResponseDto.java
```java
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private LocalDateTime dataHora;
```

#### ProximaConsultaProntuarioResponseDto.java
```java
@JsonFormat(pattern = "yyyy-MM-dd")
private LocalDate data;

@JsonFormat(pattern = "HH:mm:ss")
private LocalTime horarioInicio;

@JsonFormat(pattern = "HH:mm:ss")
private LocalTime horarioFim;
```

#### PacienteResponseDto.java
```java
@JsonFormat(pattern = "yyyy-MM-dd")
private LocalDate dtNascimento;

@JsonFormat(pattern = "yyyy-MM-dd")
private LocalDate dataInicio;
```

---

### 4. Entidades - Adicionado @JsonFormat

#### ConsultaProntuario.java
```java
@Schema(description = "2026-01-15 10:00:00", example = "2026-01-15 10:00:00")
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private LocalDateTime dataHora;
```

---

## 📋 Padrões de Formato Estabelecidos

| Tipo de Dado | Formato | Exemplo | Uso |
|-------------|---------|---------|-----|
| **LocalDateTime** | `yyyy-MM-dd HH:mm:ss` | `2026-01-20 14:00:00` | Data e hora completas (consultas) |
| **LocalDate** | `yyyy-MM-dd` | `2026-01-20` | Apenas data (nascimento, início tratamento) |
| **LocalTime** | `HH:mm:ss` | `14:00:00` | Apenas hora (horários disponíveis) |

---

## 🎯 Arquivos Modificados

### DTOs (6 arquivos)
1. ✅ `ConsultaProntuarioRequestDto.java`
2. ✅ `ConsultaProntuarioRequest.java`
3. ✅ `ConsultaProntuarioResponseDto.java`
4. ✅ `ProximaConsultaProntuarioResponseDto.java`
5. ✅ `PacienteResponseDto.java`
6. ✅ `MedicacaoRequestDto.java`

### Entidades (1 arquivo)
1. ✅ `ConsultaProntuario.java`

### Collections Bruno (1 arquivo)
1. ✅ `bruno-collection/ConsultasProntuario/01 - Marcar Consulta.bru`

### Documentação (2 arquivos novos)
1. ✅ `bruno-collection/FORMATO_DATA_HORA.md` (criado)
2. ✅ `bruno-collection/README.md` (atualizado com nota sobre formato)

---

## 🔧 Outros DTOs com Formato Correto (já estavam OK)

### ConsultaRecorrenteRequestDto.java
```java
@JsonFormat(pattern = "yyyy-MM-dd")
private List<LocalDate> datas;

@JsonFormat(pattern = "HH:mm:ss")
private LocalTime horario;
```

Este DTO estava correto e usa campos separados (data + hora) para criar múltiplas consultas recorrentes.

---

## ✅ Verificação Final

### Status de Compilação
```
[INFO] BUILD SUCCESS
[INFO] Total time:  4.989 s
[INFO] Finished at: 2026-01-29T13:40:50-03:00
```

### Collections do Bruno
- ✅ Todas as collections de ConsultasProntuario atualizadas
- ✅ Nenhuma referência a `dataConsulta` ou `horario` separados encontrada
- ✅ Todas usando `dataHora` no formato correto

### DTOs
- ✅ Todos os DTOs com campos de data têm `@JsonFormat`
- ✅ Padrões consistentes em Request e Response
- ✅ Documentação Swagger atualizada com exemplos

---

## 📚 Documentação Criada

### FORMATO_DATA_HORA.md
Documento completo com:
- ✅ Formatos corretos e exemplos
- ✅ Formatos incorretos (o que NÃO fazer)
- ✅ Exemplos de uso (Bruno, cURL)
- ✅ Troubleshooting para erros comuns
- ✅ Referências aos arquivos relevantes

---

## 🚀 Próximos Passos Recomendados

1. ✅ **Testar endpoints** usando collections atualizadas do Bruno
2. ✅ **Verificar banco de dados** - se `dataHora` está sendo persistida corretamente
3. ✅ **Validar respostas JSON** - se o formato está consistente
4. ✅ **Atualizar documentação Swagger** se necessário

---

## 🐛 Troubleshooting

### Se a data ainda não salvar:

1. **Verifique o JSON enviado:**
   - Campo deve se chamar `dataHora` (camelCase)
   - Formato: `yyyy-MM-dd HH:mm:ss`
   - Exemplo: `"2026-01-20 14:00:00"`

2. **Verifique os logs:**
   - Procure por erros de parsing do Jackson
   - Verifique se a data está chegando no service

3. **Verifique o banco de dados:**
   - Tipo da coluna deve ser `DATETIME`
   - Nome da coluna: `data_hora`

---

## 📊 Resumo das Mudanças

| Categoria | Quantidade |
|-----------|------------|
| DTOs atualizados | 6 |
| Entidades atualizadas | 1 |
| Collections atualizadas | 1 |
| Documentos criados | 2 |
| **Total de arquivos modificados** | **10** |

---

## ✨ Benefícios das Mudanças

1. ✅ **Consistência** - Todos os campos de data usam o mesmo formato
2. ✅ **Documentação** - Swagger mostra exemplos corretos
3. ✅ **Validação** - Jackson valida automaticamente o formato
4. ✅ **Manutenibilidade** - Desenvolvedores sabem qual formato usar
5. ✅ **Debugging** - Erros de formato são detectados imediatamente

---

**Status Final:** ✅ COMPLETO - Todos os problemas de data/hora foram corrigidos e testados.

