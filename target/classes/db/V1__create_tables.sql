CREATE TABLE public.usuarios (
	id bigserial NOT NULL,
	criado_por int8,
	nome varchar(150) NOT NULL,
	cpf bpchar(11) NOT NULL,
	email varchar(150) NOT NULL,
	senha varchar(255) NOT NULL,
	tipo_usuario bpchar(1) NOT NULL,
	data_criacao timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	ativo bool DEFAULT true NULL,
	CONSTRAINT usuarios_email_key UNIQUE (email),
	CONSTRAINT usuarios_pk PRIMARY KEY (id),
	CONSTRAINT usuarios_unique UNIQUE (cpf),
	CONSTRAINT fk_criado_por FOREIGN KEY (criado_por) REFERENCES public.usuarios(id)
);
CREATE TABLE categorias (
    id bigserial  NOT NULL,
    categoria varchar(150) NOT NULL,
    descricao text NOT NULL,
    data_criacao timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ativo bool DEFAULT true NOT NULL,
    CONSTRAINT categorias_pk PRIMARY KEY (id)
);

CREATE TABLE enderecos (
    id bigserial NOT NULL,
    cep varchar(10) NOT NULL,
    logradouro varchar(150) NOT NULL,
    numero varchar(10) NOT NULL,
    complemento varchar(100) NULL,
    bairro varchar(100) NOT NULL,
    cidade varchar(100) NOT NULL,
    estado varchar(2) NOT NULL,
    CONSTRAINT enderecos_pk PRIMARY KEY (id)
);