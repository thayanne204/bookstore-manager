# 📚 Tecnoteca (BookStore Manager)

**Sistema de Gestão da livraria Tecnoteca — API REST com Java e Spring Boot**

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
└── service
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

## 📏 Regras de negócio

| # | Regra | Onde | Resposta |
|---|-------|------|----------|
| 1 | Não permite categoria, editora ou ISBN duplicados | `CategoriaService`, `EditoraService`, `LivroService` | 409 |
| 2 | Não permite cliente com CPF ou e-mail duplicados | `ClienteService` | 409 |
| 3 | Não vende livro com estoque insuficiente ou inativo | `VendaService` | 409 |
| 4 | Não vende para cliente inativo | `VendaService` | 409 |
| 5 | Valor total da venda calculado automaticamente (soma dos itens − desconto); desconto não pode ser negativo nem maior que a venda | `VendaService` | 409 |
| 6 | A venda baixa o estoque e registra uma movimentação de SAÍDA | `VendaService` | - |
| 7 | Cancelar venda devolve os livros ao estoque (movimentação de ENTRADA) | `VendaService` | - |
| 8 | Só é possível alterar venda CONCLUIDA e só excluir venda CANCELADA | `VendaService` | 409 |
| 9 | Não exclui categoria/editora com livros vinculados, cliente com vendas, nem livro já vendido | Services | 409 |

## 🔎 Filtros disponíveis

| Endpoint | Parâmetros |
|----------|-----------|
| `GET /api/livros/buscar` | `titulo` |
| `GET /api/livros/filtrar` | `categoriaId`, `precoMin`, `precoMax` (todos opcionais) |
| `GET /api/livros/estoque-baixo` | `limite` (padrão 5) |
| `GET /api/livros/ativos` | - |
| `GET /api/clientes/buscar` | `nome` |
| `GET /api/vendas/periodo` | `inicio`, `fim` (AAAA-MM-DD) |
| `GET /api/vendas/status/{status}` | `CONCLUIDA` ou `CANCELADA` |
| `GET /api/vendas/cliente/{clienteId}` | - |

## 🧪 Exemplos de uso

Criar categoria e editora:

```bash
curl -X POST http://localhost:8080/api/categorias -H "Content-Type: application/json" \
  -d '{"nome":"Romance","descricao":"Livros de romance","ativo":true}'

curl -X POST http://localhost:8080/api/editoras -H "Content-Type: application/json" \
  -d '{"nome":"Editora Exemplo","cnpj":"12.345.678/0001-90","email":"contato@exemplo.com"}'
```

Criar livro:

```bash
curl -X POST http://localhost:8080/api/livros -H "Content-Type: application/json" \
  -d '{"titulo":"Dom Casmurro","isbn":"9788535910663","preco":49.90,"quantidadeEstoque":10,"estoqueMinimo":3,"categoriaId":1,"editoraId":1,"ativo":true}'
```

Criar cliente:

```bash
curl -X POST http://localhost:8080/api/clientes -H "Content-Type: application/json" \
  -d '{"nome":"Maria Silva","cpf":"12345678901","email":"maria@email.com","estado":"PI","ativo":true}'
```

Registrar venda (baixa o estoque e calcula o total):

```bash
curl -X POST http://localhost:8080/api/vendas -H "Content-Type: application/json" \
  -d '{"clienteId":1,"formaPagamento":"PIX","desconto":5.00,"itens":[{"livroId":1,"quantidade":2}]}'
```

Cancelar e excluir venda:

```bash
curl -X PUT http://localhost:8080/api/vendas/1/cancelar
curl -X DELETE http://localhost:8080/api/vendas/1
```

Filtrar livros por faixa de preço:

```bash
curl "http://localhost:8080/api/livros/filtrar?precoMin=20&precoMax=60"
```

Resposta de erro (exemplo, 409 Conflict):

```json
{ "erro": "Estoque insuficiente para o livro: Dom Casmurro" }
```

| Status | Quando |
|--------|--------|
| 200 | Consulta ou atualização com sucesso |
| 201 | Registro criado |
| 204 | Exclusão com sucesso |
| 400 | Dados inválidos (validação) ou JSON mal formatado |
| 404 | Registro não encontrado |
| 409 | Violação de regra de negócio |

## 🧱 Testes automatizados

Testes unitários da camada Service (Mockito), que não precisam de banco:

```bash
./mvnw test
```

## 🐳 Docker (opcional)

Para subir um MySQL já com as tabelas criadas:

```bash
docker compose up -d
```

Depois defina `DB_PASSWORD=bookstore123` (ou a senha que usou) e execute a aplicação. Se o MySQL do Docker usar a porta 3306 e já houver outro MySQL instalado no computador, pare um dos dois.

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

## 🧪 Rodar sem MySQL (perfil H2)

Para testar rapidamente sem instalar ou configurar o MySQL, use o perfil `h2`, que sobe um banco em memória (os dados são perdidos ao encerrar a aplicação):

```cmd
.\mvnw spring-boot:run "-Dspring-boot.run.profiles=h2"
```

Sem esse parâmetro, a aplicação usa o MySQL normalmente.

## 🖥️ Interface web (front-end)

A aplicação inclui uma interface web simples (HTML, CSS e JavaScript puro) que consome a API. Ela fica em `src/main/resources/static` e é servida pelo próprio Spring Boot. Com a aplicação rodando, acesse:

http://localhost:8080/

Pela interface é possível cadastrar, listar e excluir livros, clientes, categorias e editoras, buscar livros por título e faixa de preço, registrar vendas (com baixa de estoque), cancelar e excluir vendas. As mensagens de erro da API (400, 404 e 409) aparecem na tela.

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

Tenha o MySQL 8 em execução. **Não é preciso criar o banco nem as tabelas à mão**: na primeira execução a aplicação cria o banco `bookstore_manager` e as tabelas usando o script `docs/schema.sql` (ele pode ser executado várias vezes sem problema). A aplicação usa `ddl-auto=validate`, ou seja, confere se as entidades batem com as tabelas.

Informe a senha do MySQL **sem gravá-la no repositório**, de uma destas formas:

**Opção A — arquivo local (mais simples).** Copie `application-local.properties.example` para `application-local.properties`, na mesma pasta do `pom.xml`, e coloque a senha:

```properties
spring.datasource.username=root
spring.datasource.password=sua_senha
```

Esse arquivo é ignorado pelo Git.

**Opção B — variáveis de ambiente.**

```bash
export DB_URL=jdbc:mysql://localhost:3306/bookstore_manager?createDatabaseIfNotExist=true
export DB_USERNAME=root
export DB_PASSWORD=sua_senha
```

No Windows (cmd): `set DB_PASSWORD=sua_senha`.

Se preferir criar as tabelas manualmente, execute `docs/schema.sql` no MySQL antes de iniciar.

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
