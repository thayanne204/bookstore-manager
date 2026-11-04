# 📚 BookStore Manager

**Sistema de Gestão de Livraria — API REST com Java e Spring Boot**

O BookStore Manager é um projeto acadêmico desenvolvido com o objetivo de facilitar o gerenciamento de uma livraria, permitindo o controle de livros, categorias, clientes, editoras, vendas e estoque.

A aplicação utiliza uma arquitetura em camadas, princípios de organização de código e integração com banco de dados relacional.

## 🎯 Objetivo do projeto

Desenvolver uma API REST para gerenciamento de operações de uma livraria, aplicando conceitos de desenvolvimento backend, persistência de dados, regras de negócio e boas práticas de programação.

## 🛠️ Tecnologias utilizadas

- **Java 25**
- **Spring Boot 4**
- **Spring Data JPA**
- **Spring Web MVC**
- **Spring Validation**
- **Spring Security**
- **MySQL 8**
- **Maven**
- **Swagger / OpenAPI**
- **Lombok**

## 🏗️ Arquitetura do projeto

O projeto utiliza arquitetura em camadas para separar as responsabilidades da aplicação:

```text
br.com.bookstore
│
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── security
├── service
└── util
```

- **Controller:** recebe e processa as requisições HTTP.
- **Service:** concentra as regras de negócio.
- **Repository:** realiza o acesso e a persistência dos dados.
- **Entity:** representa as entidades do banco de dados.
- **DTO:** controla os dados de entrada e saída da API.
- **Exception:** centraliza o tratamento de exceções.

## 📦 Funcionalidades

- Cadastro, consulta, atualização e exclusão de categorias.
- Gerenciamento de livros.
- Gerenciamento de clientes.
- Gerenciamento de editoras.
- Registro e gerenciamento de vendas.
- Consulta de livros por título.
- Consulta de livros com estoque baixo.
- Controle de estoque durante as vendas.
- Validação de dados.
- Tratamento centralizado de exceções.
- Documentação da API com Swagger.

## 🗄️ Banco de dados

O sistema utiliza MySQL 8 como banco de dados relacional.

Entre as principais entidades estão:

- Livros
- Categorias
- Autores
- Editoras
- Clientes
- Vendas
- Itens de venda
- Fornecedores
- Movimentações de estoque
- Usuários

O relacionamento entre as entidades permite organizar as informações e manter a integridade dos dados.

## 📖 Documentação da API

A documentação interativa é disponibilizada pelo Swagger UI.

Após iniciar a aplicação, acesse:

http://localhost:8080/swagger-ui/index.html

## ▶️ Como executar o projeto

### Pré-requisitos

- Java 25 ou compatível com a configuração do projeto.
- MySQL 8.
- Maven.

### 1. Clone o repositório

```bash
git clone https://github.com/thayanne204/bookstore-manager.git
```

### 2. Acesse a pasta do projeto

```bash
cd bookstore-manager
```

### 3. Configure o banco de dados

Crie o banco de dados no MySQL:

```sql
CREATE DATABASE bookstore_manager;
```

Configure as credenciais de acesso ao banco no arquivo `application.properties`.

### 4. Execute a aplicação

No Windows:

```cmd
mvnw.cmd spring-boot:run
```

No Linux ou macOS:

```bash
./mvnw spring-boot:run
```

Após iniciar, a API estará disponível em:

http://localhost:8080

## 👩‍💻 Desenvolvimento

Projeto desenvolvido como atividade acadêmica da área de Engenharia de Software, com foco em desenvolvimento backend utilizando Java e Spring Boot.

## 🔗 Repositório

[GitHub — BookStore Manager](https://github.com/thayanne204/bookstore-manager)

---

**BookStore Manager — Organização, tecnologia e gestão para livrarias.**
