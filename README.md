# SISTEMA-DE-LOCACAO-DE-FILMES

# Sistema de Locação de Filmes

Sistema desktop para gerenciamento interno de uma locadora de filmes, desenvolvido em **Java** com interface gráfica em **Java Swing** e persistência de dados em **MySQL** via **JDBC**.

> Trabalho apresentado à Pontifícia Universidade Católica de Goiás (PUC Goiás), como requisito parcial da disciplina **Programação Orientada a Objetos**, curso de Ciência da Computação.
> **Autora:** Esther Alves de Souza
> **Professor:** Vicente Paulo De Camargo
> **Local/Período:** Goiânia/GO — 2025/2

## Descrição

O sistema tem como objetivo informatizar e simplificar o gerenciamento interno de uma locadora, permitindo o controle organizado de filmes, clientes e locações, por meio de uma interface gráfica intuitiva integrada a um banco de dados relacional.

## Objetivos

- Centralizar as informações de clientes, filmes e locações.
- Fornecer ferramentas de cadastro, pesquisa e listagem de dados.
- Reduzir erros operacionais e agilizar o acesso às informações.
- Proporcionar uma experiência de uso clara e intuitiva para os funcionários.

## Funcionalidades Principais

- **Login:** autenticação de usuário e senha para acesso seguro ao sistema.
- **Tela Principal:** navegação central para todas as funcionalidades.
- **Gerenciamento de Filmes:** inclusão, consulta, alteração e exclusão de filmes (título, gênero, idioma, entre outros dados), com aba dedicada de pesquisa com filtros.
- **Gerenciamento de Clientes:** cadastro, alteração, exclusão e consulta de clientes (nome, contato e demais dados cadastrais), com aba dedicada de pesquisa.
- **Pesquisas:** cada módulo de cadastro possui aba própria de pesquisa; a pesquisa de filmes permite filtragem por seleção de critérios (ex.: código ou título).
- **Locação e Devolução:** registro de novas locações com verificação de disponibilidade e atualização de status, e processamento de devoluções com atualização de data e disponibilidade.
- **Listagem de Dados:** tela dedicada com visualização completa e ordenável dos cadastros (clientes, filmes ou locações).

## Benefícios Esperados

- Organização eficiente dos dados de clientes e filmes.
- Acesso rápido às informações por meio de pesquisas e listagens filtradas.
- Redução de erros e duplicidade de dados.
- Experiência de uso mais rápida, clara e acessível.

## Tecnologias Utilizadas

- **Linguagem:** Java
- **Interface Gráfica:** Java Swing
- **Banco de Dados:** MySQL
- **Conexão com o Banco:** JDBC

## Modelagem do Sistema

O projeto foi construído a partir de uma documentação de análise e projeto orientada a objetos, contendo:

- **Diagrama de Casos de Uso** — funcionalidades acessadas pelo ator Funcionário: login, manter clientes, manter filmes, gerar listagem de dados, realizar locação e realizar devolução.
- **Diagramas de Sequência** — fluxos de: Realizar Login, Manter Cliente, Manter Filme, Realizar Locação, Realizar Devolução e Gerar Listagem de Dados.
- **Diagrama de Classes** — classes de domínio `Locacao`, `Cliente`, `Filme` e `Usuario`, além da classe utilitária `BancoDados`, responsável pela conexão com o banco de dados.
- **DER (Diagrama Entidade-Relacionamento)** — modelo conceitual e modelo lógico das entidades `CLIENTE`, `FILME`, `LOCACAO` e `USUARIO`.

### Principais Classes

| Classe | Responsabilidade |
|---|---|
| `Usuario` | Autenticação do funcionário (login e senha) |
| `Cliente` | Dados cadastrais dos clientes, com operações de CRUD |
| `Filme` | Dados dos filmes, com operações de CRUD |
| `Locacao` | Controle do processo de empréstimo e devolução de filmes |
| `BancoDados` | Abertura e fechamento da conexão JDBC com o MySQL |

## Estrutura de Relacionamentos

- Um cliente pode estar associado a várias locações (1 : 0..*).
- Um filme pode estar associado a várias locações (1 : 0..*).
- Cada locação está vinculada a exatamente um cliente e um filme, e é registrada por um usuário do sistema.

## Conclusão

Este projeto integra os principais conceitos de Programação Orientada a Objetos e de modelagem de software, partindo da definição da visão do sistema até a construção dos diagramas de casos de uso, sequência, classes e entidade-relacionamento. Essa etapa de análise e projeto serviu de base consistente para a implementação em Java com interface gráfica e acesso ao MySQL.

