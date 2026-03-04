# 🏛️ ObservaAção

Sistema digital para registro e acompanhamento de solicitações de serviços públicos.

Projeto integrador com foco em:
- Transparência
- Redução de barreiras de acesso
- Rastreabilidade
- Manutenção sustentável

ODS relacionado: ODS 16 — Paz, Justiça e Instituições Eficazes

---

## 🎯 Objetivo

Criar um sistema simples que permita ao cidadão:

- Registrar solicitações (categoria, descrição, localização, prioridade)
- Escolher envio identificado ou anônimo
- Acompanhar status e prazos (SLA)
- Visualizar histórico de atendimento

E ao servidor:

- Organizar fila por prioridade, bairro e categoria
- Atualizar status com comentário obrigatório
- Manter rastreabilidade completa

---

## 🧠 IHC — Perfis e Personas

Perfis obrigatórios:

1. Cidadão com baixa familiaridade digital  
   (PREENCHER resumo + 3 personas)

2. Cidadão em situação de vulnerabilidade / receio de retaliação  
   (PREENCHER resumo + 3 personas)

3. Servidor público / gestor  
   (PREENCHER resumo + 3 personas)

Cada persona deve conter:
- Contexto social/digital
- Dores
- Objetivos
- Restrições
- Acessibilidade
- Medos

---

## 🏗️ Escopo Mínimo

### Cadastro de Solicitação
- Categoria
- Descrição
- Localização
- Prioridade
- Identificado ou Anônimo
- Anexo (opcional)

### Fluxo de Status
Aberto → Triagem → Em execução → Resolvido → Encerrado

Cada mudança deve registrar:
- Data
- Responsável
- Comentário obrigatório

---

## ⚙️ Regras Críticas

Anonimato:
- Limita dados pessoais
- Exige descrição mínima
- (PREENCHER regra adicional)

Prioridade:
- Define SLA
- Impacta ordem da fila
- (PREENCHER critérios)

Prevenção de abuso:
- Campos obrigatórios
- Validações
- Logs
- (PREENCHER regra)

---

## 💻 Versão Beta (1º Bimestre)

Linguagem: (PREENCHER — Java ou Python)  
Interface: (PREENCHER — CLI ou UI simples)  
Persistência: (PREENCHER — memória/arquivo)

### Classes principais
- Solicitacao
- Usuario
- Categoria
- HistoricoStatus
- FilaAtendimento
- ServicoSolicitacoes
- (PREENCHER outras)

Funcionalidades:
- Criar
- Listar
- Buscar por protocolo
- Atualizar status
- Registrar comentário

---

## 🧹 Clean Code

Relatório analisando 3 funções:
- (PREENCHER)
- (PREENCHER)
- (PREENCHER)

Práticas aplicadas:
- Nomes significativos
- SRP
- Métodos curtos
- Tratamento de erros
- Separação de responsabilidades

---

## 📦 Estrutura


---

## 🎥 Entrega

- Link do GitHub (versão beta)
- Documento com perfis + relatório clean code
- Link do Vídeo: 
- Tema escolhido: (PREENCHER)

---

## 👥 Integrantes
- Thiago Gimenes Santos
- Leonardo Bezerra da Silva
- Carlos Eduardo Carfi Silva

---

Status: Em desenvolvimento 🚧  
Versão: v0.1-beta
