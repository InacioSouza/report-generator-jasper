CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE sistema (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(250) NOT NULL,
    descricao TEXT,
    versao BIGINT NOT NULL
);

CREATE TABLE relatorio (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    titulo_padrao VARCHAR(250) NOT NULL,
    subtitulo_padrao VARCHAR(250),
    nome VARCHAR(250),
    descricao_tecnica TEXT,
    informacao TEXT,
    sistema_id UUID,
    numero_ultima_versao BIGINT NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    versao BIGINT NOT NULL,

    FOREIGN KEY (sistema_id) REFERENCES sistema(id)
);

CREATE TABLE versao_relatorio(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(250),
    nome_arquivo VARCHAR(250) NOT NULL,
    relatorio_id UUID NOT NULL,
    numero_versao BIGINT NOT NULL,
    descricao_versao TEXT,
    tipo_arquivo VARCHAR(10) NOT NULL,
    tipo_final_relatorio VARCHAR(10) NOT NULL,
    arquivo_compilado BYTEA,
    arquivo_original BYTEA NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    versao BIGINT NOT NULL,

    FOREIGN KEY (relatorio_id) REFERENCES relatorio(id)
);

CREATE TABLE arquivo_subreport(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_parametro VARCHAR(250) NOT NULL,
    versao_relatorio_id UUID NOT NULL,
    arquivo_compilado BYTEA,
    arquivo_original BYTEA NOT NULL,
    versao BIGINT NOT NULL,

    FOREIGN KEY (versao_relatorio_id) REFERENCES versao_relatorio(id)
);

CREATE TABLE usuario_app(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(250) NOT NULL,
    senha VARCHAR(350) not null,
    permisao VARCHAR(100) not null
);

CREATE TABLE api_key(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    hash VARCHAR(450) NOT NULL,
    ativa BOOLEAN NOT NULL,
    sistema_id UUID NOT NULL,
    criada_em TIMESTAMP NOT NULL,

    FOREIGN KEY (sistema_id) REFERENCES sistema(id)
);