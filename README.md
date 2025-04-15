# LP2 - Sistema de Gestão de Leilões - Grupo III


## Descrição do Projeto
Este projeto está a ser desenvolvido para a unidade curricular de Laboratório de Projeto II (2024/2025). Consiste num sistema de gestão de leilões para a empresa "Valor em Alta", especializada na realização de três tipos de leilões: leilões eletrónicos, leilões de carta fechada e venda direta.

## Estado do Projeto
💻 **Em Desenvolvimento** 💻

## Índice
- [Funcionalidades](#funcionalidades)
- [Planeamento de Sprints](#planeamento-de-sprints)
- [Registos Daily Scrum](#registos-daily-scrum)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Instalação e Configuração](#instalação-e-configuração)
- [Equipa](#equipa)
- [Processo de Desenvolvimento](#processo-de-desenvolvimento)

## Funcionalidades
O sistema inclui as seguintes funcionalidades principais:

### 1. Gestão de Leilões
- Criar, consultar, atualizar e eliminar leilões com os dados:
  - Nome do produto a leiloar
  - Descrição
  - Tipo de leilão
  - Data de início
  - Data de fim (não obrigatório)
  - Valor máximo (não obrigatório) --> Não será usado
  - Valor mínimo
  - Múltiplo de lance (para leilões eletrónicos)


### 2. Gestão de Clientes (também poderão ser administradores)
- Criar, consultar, atualizar e eliminar clientes com os dados:
  - Nome
  - Morada
  - Data de nascimento
  - E-mail
  - Password

### 3. Estatísticas
- Por leilão:
  - Cliente que realizou mais lances (ordenado pelo valor mais alto)
  - Total tempo ativo
- Globais e por tipo:
  - Quantidade de leilões terminados
  - Leilão que teve mais tempo ativo
  - Leilão com mais lances
  - Média de tempo para lance acontecer
  - Leilões sem lances
  - Clientes registados
  - Média de idades de clientes
  - Percentagem de clientes que usam o maior domínio de e-mail

### 4. Notificações
- E-mail de boas-vindas
- E-mail para o vencedor do leilão
- E-mail para cliente quando não faz login há mais de 3 meses
- E-mail para cliente com a informação que ficou sem créditos disponíveis
- E-mail com relatório diário para o gestor da leiloeira

## Planeamento de Sprints
O nosso projeto está organizado em 7 sprints:
- **Sprint 1:** 5 de março - 18 de março (Sprint Review RJR)
- **Sprint 2:** 19 de março - 2 de abril (Sprint Review VHC)
- **Sprint 3:** 3 de abril - 16 de abril (Sprint Review – apresentação)
- **Sprint 4:** 17 de abril - 30 de abril (Sprint Review VHC)
- **Sprint 5:** 1 de maio - 13 de maio (Sprint Review RJR)
- **Sprint 6:** 14 de maio - 28 de maio (Sprint Review VHC)
- **Sprint 7:** 29 de maio - 17 de junho (Sprint Review – apresentação)

## Registos Daily Scrum

### Formato do Daily Scrum
Cada membro da equipa responde às seguintes questões:
- O que fiz desde o último Daily Scrum?
- O que vou fazer até ao próximo Daily Scrum?
- Quais os obstáculos que estou a enfrentar?

### Sprint 1
#### Daily Scrum 
- **Francisco Rocha (1241587):**
  - O que fiz:  Fez clone do projeto, CRUD´s dos leilões, menu de administradores, criação do ficheiro em csv dos Leilões
  - O que vou fazer: adicionar lógica para carregar/salvar leilões
  - Obstáculos: *

- **Simão Valente (1241597):**
  - O que fiz:  Fez clone do projeto, CRUD´s dos clientes, menu de clientes, criação do ficheiro em csv dos Clientes
  - O que vou fazer: Adicionar lógica para carregar/salvar clientes
  - Obstáculos: *

- **Emanuel Maia (1231531):**
  - O que fiz: Criação do projeto, gestão das notificações
  - O que vou fazer: Continuar com a gestão da mesma
  - Obstáculos: *

- **Gisele Branco:**
  - O que fiz: Fez clone do projeto, gestão das estatísticas
  - O que vou fazer: Continuar com a gestão da mesma
  - Obstáculos: *

### Sprint 2
#### Daily Scrum 
- **Francisco Rocha (1241587):**
  - O que fiz: Adição lógica para carregamento de dados dos leilões, verificação data de inicio para não ser antes de hoje, criação do ficheiro em csv de lances, atualização dos campos do ficheiro Leilao.csv
  - O que vou fazer: Implementar lógica dos lances nos clientes
  - Obstáculos: *

- **Simão Valente (1241597):**
  - O que fiz: Adição lógica para carregamento de dados dos clientes, validação na data de nascimento para o cliente ser maior de idade, validar email caso exista ou seja inválido
  - O que vou fazer: Implementar lógica dos lances nos clientes
  - Obstáculos: Um pouco de dificuldade na lógica dos carregamentos de dados dos clientes

- **Emanuel Maia (1231531):**
  - O que fiz: Gestão das notificações, testes gerais
  - O que vou fazer: Continuar com a gestão das notificações
  - Obstáculos: *

- **Gisele Branco:**
  - O que fiz: Gestão das estatísticas, testes gerais
  - O que vou fazer: Continuar com a gestão das estatísticas
  - Obstáculos: *

[Registos adicionais de Daily Scrum serão acrescentados à medida que o projeto avança]

## Tecnologias Utilizadas
- Java
- IntelliJ
- Git/GitHub/GitExtensions
- Manipulação de ficheiros CSV para persistência de dados

## Instalação e Configuração
1. Clone o repositório:
   ```
   git clone https://github.com/Emanuel852483/LP2.git
   ```
2. Abra o projeto no NetBeans IDE ou IntelliJ
3. Execute o ficheiro principal da aplicação

## Equipa
- Francisco Rocha - 1241587
- Gisele Branco -1231763
- Emanuel Maia - 1231531
- Simão Valente - 1241597


## Processo de Desenvolvimento
Este projeto segue a metodologia Scrum com sprints de duas semanas. Estamos a implementar o padrão de arquitetura MVC (Model-View-Controller) e a aderir aos princípios SOLID para desenvolvimento orientado a objetos.

### Fluxo de Trabalho Git
- Utilizamos o Git flow com as seguintes branches:
  - `master`: Código de produção
  - `quality`: Código pronto para garantia de qualidade
  - `dev-[nome]`: Branches de desenvolvimento individuais

Todos os commits para a branch master devem ser feitos através de Pull Requests. O repositório está organizado para facilitar o desenvolvimento colaborativo enquanto mantém a qualidade do código.

### Cerimónias Scrum
- **Sprint Review**: Demonstração do trabalho concluído
- **Sprint Retrospective**: Análise do que continuar, parar e começar a fazer
- **Backlog Refinement**: Refinar e priorizar o backlog do produto
- **Sprint Planning**: Planear o trabalho para o próximo sprint
- **Daily Scrum**: Sincronização diária da equipa (registada neste README)
