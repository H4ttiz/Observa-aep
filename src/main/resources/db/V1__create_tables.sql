CREATE TABLE usuarios (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo_usuario VARCHAR(1) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);
CREATE TABLE categorias (
                            id int8 GENERATED ALWAYS AS IDENTITY NOT NULL,
                            categoria varchar(150) NOT NULL,
                            descricao text NOT NULL,
                            data_criacao timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                            ativo bool DEFAULT true NOT NULL,
                            CONSTRAINT categorias_pk PRIMARY KEY (id)
);

CREATE TABLE enderecos (
                           id int8 GENERATED ALWAYS AS IDENTITY NOT NULL,
                           cep varchar(10) NOT NULL,
                           logradouro varchar(150) NOT NULL,
                           numero varchar(10) NOT NULL,
                           complemento varchar(100) NULL,
                           bairro varchar(100) NOT NULL,
                           cidade varchar(100) NOT NULL,
                           estado varchar(2) NOT NULL,
                           CONSTRAINT enderecos_pk PRIMARY KEY (id)
);