-- Adiciona coluna ativo à tabela responsavel se ela não existir
ALTER TABLE responsavel
    ADD COLUMN IF NOT EXISTS ativo TINYINT(1) NOT NULL DEFAULT 1;
