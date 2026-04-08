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
CREATE TABLE public.solicitacoes (
	id bigserial NOT NULL,
	id_categoria int8 NOT NULL,
	id_solicitante int8 NOT NULL,
	id_atendente int8,
	id_endereco int8 NOT NULL,
	status char(2) NOT NULL,
	prioridade char(2),
	anonimo bool NOT null default false,
	titulo varchar(150) NOT NULL,
	descricao text NOT NULL,
	data_solicitada timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	data_prazo timestamp NULL,
	data_finalizada timestamp NULL,
    observacao TEXT,
	CONSTRAINT solicitacoes_pk PRIMARY KEY (id),
	CONSTRAINT fk_id_categoria FOREIGN KEY (id_categoria) REFERENCES public.categorias(id),
	CONSTRAINT fk_id_solicitante FOREIGN KEY (id_solicitante) REFERENCES public.usuarios(id),
	CONSTRAINT fk_id_atendente FOREIGN KEY (id_atendente) REFERENCES public.usuarios(id),
	CONSTRAINT fk_id_endereco FOREIGN KEY (id_endereco) REFERENCES public.enderecos(id)
);

CREATE TABLE public.historico_movimentacao_solicitacao (
    id bigserial NOT NULL,
    id_solicitacao int8 NOT NULL,
    id_responsavel int8 NOT NULL,
    comentario TEXT NOT NULL,
    status_atual char(2) NOT NULL,
    status_anterior char(2) NOT NULL,
    data_movimentacao timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT historico_movimentacao_pk PRIMARY KEY (id),
    CONSTRAINT fk_historico_solicitacao FOREIGN KEY (id_solicitacao) REFERENCES public.solicitacoes(id),
    CONSTRAINT fk_historico_responsavel FOREIGN KEY (id_responsavel) REFERENCES public.usuarios(id)
);