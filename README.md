# Prism ERP - Sistema de Gestão Empresarial

Este projeto implementa um sistema de gestão empresarial (ERP) simplificado com Spring Boot, focando em módulos essenciais como vendas, estoque, clientes, funcionários e departamentos.

## Funcionalidades

O Prism ERP oferece as seguintes funcionalidades:

* **Gerenciamento de Clientes:** Cadastro, consulta, atualização e exclusão de clientes.
* **Gerenciamento de Funcionários:** Cadastro, consulta, atualização e exclusão de funcionários, com associação a departamentos.
* **Gerenciamento de Departamentos:** Cadastro, consulta, atualização e exclusão de departamentos, com associação de gerentes.
* **Gerenciamento de Pedidos de Venda:** Criação, consulta, atualização de status e listagem de pedidos de venda, com associação a clientes e produtos.
* **Gerenciamento de Estoque:** Registro de transações de estoque (entrada, saída, etc.) e consulta de níveis de estoque.  Inclui funcionalidade para identificar produtos com baixo estoque.
* **Gerenciamento de Faturas:** Geração de faturas a partir de pedidos de venda, atualização de status, aplicação de descontos e cálculo de impostos.

## Tecnologias Utilizadas

* **Java 17:** Linguagem de programação principal.
* **Spring Boot:** Framework para desenvolvimento de aplicações web.
* **Spring Data JPA:** Simplifica o acesso a dados com JPA.
* **PostgreSQL:** Banco de dados relacional.
* **Lombok:** Reduz código boilerplate.
* **MapStruct (Recomendado):**  Mapeamento de objetos (não incluído neste exemplo, mas altamente recomendado).
* **Bean Validation:** Validação de dados.
* **Swagger (Recomendado):** Documentação da API (não incluído, mas recomendado).

## Arquitetura

O projeto segue uma arquitetura em camadas:

* **Controllers:** Recebem requisições HTTP e retornam respostas.
* **Services:** Implementam a lógica de negócio.
* **Repositories:**  Fornecem acesso aos dados.
* **DTOs (Data Transfer Objects):**  Transferem dados entre as camadas.
* **Entities:** Representam as entidades do domínio.

## Como executar o projeto

1. **Clone o repositório:** `git clone https://github.com/seu-usuario/prism-erp.git`
2. **Configure o banco de dados:** Crie um banco de dados PostgreSQL e configure as propriedades de conexão no arquivo `application.properties`.
3. **Execute a aplicação:**  `./mvnw spring-boot:run`

## Endpoints da API

**Clientes:**

* `POST /api/v1/customers`: Criar um cliente.
* `GET /api/v1/customers/{id}`: Buscar um cliente por ID.
* `GET /api/v1/customers`: Listar todos os clientes (com paginação).
* `PUT /api/v1/customers/{id}`: Atualizar um cliente.
* `DELETE /api/v1/customers/{id}`: Excluir um cliente.
* `GET /api/v1/customers/document/{documentNumber}`: Buscar um cliente por número de documento.

**Funcionários:**

* `POST /api/v1/employees`: Criar um funcionário.
* `GET /api/v1/employees/{id}`: Buscar um funcionário por ID.
* `GET /api/v1/employees`: Listar todos os funcionários (com paginação).
* `PUT /api/v1/employees/{id}`: Atualizar um funcionário.
* `DELETE /api/v1/employees/{id}`: Excluir um funcionário.
* `GET /api/v1/employees/employee-number/{employeeNumber}`: Buscar um funcionário por número de funcionário.
* `GET /api/v1/employees/department/{departmentId}`: Buscar funcionários por ID do departamento.

**Departamentos:**

* `POST /api/v1/departments`: Criar um departamento.
* `GET /api/v1/departments/{id}`: Buscar um departamento por ID.
* `GET /api/v1/departments`: Listar todos os departamentos (com paginação).
* `GET /api/v1/departments/all`: Listar todos os departamentos (sem paginação).
* `PUT /api/v1/departments/{id}`: Atualizar um departamento.
* `DELETE /api/v1/departments/{id}`: Excluir um departamento.
* `GET /api/v1/departments/code/{code}`: Buscar um departamento por código.

**Pedidos de Venda:**

* `POST /api/v1/sales-orders`: Criar um pedido de venda.
* `GET /api/v1/sales-orders/{id}`: Buscar um pedido de venda por ID.
* `PUT /api/v1/sales-orders/{id}/status`: Atualizar o status de um pedido de venda.
* `GET /api/v1/sales-orders/customer/{customerId}`: Listar pedidos de venda por ID do cliente (com paginação).
* `GET /api/v1/sales-orders/number/{orderNumber}`: Buscar um pedido de venda por número de pedido.


**Estoque:**

* `POST /api/v1/inventory/transactions`: Criar uma transação de estoque.
* `GET /api/v1/inventory/stock-levels`: Buscar os níveis de estoque (com paginação).
* `GET /api/v1/inventory/low-stock`: Buscar produtos com baixo estoque.
* `GET /api/v1/inventory/transactions/product/{productId}`: Buscar transações de estoque por ID do produto.

**Faturas:**

* `POST /api/v1/invoices/generate/{orderId}`: Gerar uma fatura a partir de um pedido de venda.
* `GET /api/v1/invoices/{id}`: Buscar uma fatura por ID.
* `PUT /api/v1/invoices/{id}/status`: Atualizar o status de uma fatura.
* `GET /api/v1/invoices`: Listar todas as faturas (com paginação).
* `GET /api/v1/invoices/status/{status}`: Buscar faturas por status (com paginação).
* `POST /api/v1/invoices/{id}/discount`: Aplicar desconto a uma fatura.
* `GET /api/v1/invoices/{id}/taxes`: Calcular impostos de uma fatura.
* `GET /api/v1/invoices/number/{invoiceNumber}`: Buscar uma fatura por número de fatura.


## Próximos passos

* Implementar testes unitários e de integração.
* Adicionar documentação detalhada da API com Swagger.
* Implementar segurança (autenticação e autorização).
* Adicionar mais funcionalidades, como relatórios, gestão financeira, etc.
