CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE sistema (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(250) NOT NULL,
    descricao TEXT
);

CREATE TABLE template_relatorio (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    titulo VARCHAR(250) NOT NULL,
    subtitulo VARCHAR(250),
    nome_template VARCHAR(250),
    descricao_template TEXT,
    informacao_relatorio TEXT,
    sistema_id BIGINT NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (sistema_id) REFERENCES sistema(id)
);

CREATE TABLE versao_template(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome_arquivo VARCHAR(250) NOT NULL,
    template_id UUID NOT NULL,
    descricao_versao TEXT,
    tipo_arquivo VARCHAR(10) NOT NULL,
    tipo_final_relatorio VARCHAR(10) NOT NULL,
    arquivo_compilado BYTEA,
    arquivo_original BYTEA NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    versao BIGINT NOT NULL,

    FOREIGN KEY (template_id) REFERENCES template_relatorio(id)
);