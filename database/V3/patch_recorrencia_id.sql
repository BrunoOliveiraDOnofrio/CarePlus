-- Adiciona coluna recorrencia_id na tabela consulta_prontuario
-- Consultações criadas via POST /consultas-prontuario/recorrentes com mais de uma data
-- recebem o mesmo UUID nessa coluna, permitindo deletar toda a série de uma vez.

-- As mudanças ja foram implementadas no bd_v2.sql
ALTER TABLE consulta_prontuario
    ADD COLUMN IF NOT EXISTS recorrencia_id VARCHAR(36) NULL;
