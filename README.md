# 🌈 Prism ERP - Sistema de Gestão Empresarial

> Sistema ERP moderno e eficiente desenvolvido com Spring Boot e boas práticas de desenvolvimento

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias](#-tecnologias)
- [Arquitetura](#-arquitetura)
- [Instalação](#-instalação)
- [API Endpoints](#-api-endpoints)
- [Cronograma e Status](#-cronograma-e-status)
- [Próximos Passos](#-próximos-passos)
- [Contribuição](#-contribuição)
- [Licença](#-licença)

## 🎯 Sobre o Projeto

O Prism ERP é um sistema de gestão empresarial moderno e intuitivo, desenvolvido para atender às necessidades essenciais de empresas de diferentes portes. Com uma arquitetura robusta e flexível, o sistema oferece uma solução completa para gestão de vendas, estoque, clientes e muito mais.

## ✨ Funcionalidades

🔹 **Gestão de Clientes**
- Cadastro completo de clientes
- Histórico de interações
- Gestão de documentos

🔹 **Gestão de Funcionários**
- Cadastro de funcionários
- Vínculo com departamentos
- Gestão de cargos e funções

🔹 **Gestão de Departamentos**
- Estrutura organizacional
- Hierarquia de gestão
- KPIs departamentais

🔹 **Vendas**
- Pedidos de venda
- Acompanhamento de status
- Histórico de transações

🔹 **Estoque**
- Controle de entrada/saída
- Alertas de baixo estoque
- Relatórios de movimentação

🔹 **Faturamento**
- Geração automática de faturas
- Cálculo de impostos
- Gestão de descontos

## 💻 Tecnologias

- ☕ Java 17
- 🍃 Spring Boot
- 🎯 Spring Data JPA
- 🐘 PostgreSQL
- 🏗️ Lombok
- 🗺️ MapStruct
- ✅ Bean Validation
- 📝 Swagger

## 🏛️ Arquitetura

```
src/
├── controllers/    # Controladores REST
├── services/      # Lógica de negócio
├── repositories/  # Acesso a dados
├── dto/           # Objetos de transferência
└── entities/      # Entidades do domínio
```

## 🚀 Instalação

1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/prism-erp.git
```

2. Configure o banco de dados em `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/prism_erp
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

3. Execute o projeto
```bash
./mvnw spring-boot:run
```

# 🔍 Prism ERP - Status de Implementação das APIs

## 🟢 APIs Implementadas e Testadas

### Módulo de Clientes
- `POST /api/v1/customers` - Criar cliente
  - Validações de documentos implementadas
  - Verificação de duplicidade
  - Formatação de dados

- `GET /api/v1/customers/{id}` - Buscar cliente por ID
  - Cache implementado
  - Tratamento de cliente não encontrado

- `GET /api/v1/customers` - Listar clientes (paginado)
  - Paginação e ordenação
  - Filtros implementados

- `GET /api/v1/customers/document/{documentNumber}` - Buscar por documento
  - Validação de formato de documento
  - Cache implementado

### Módulo de Funcionários
- `POST /api/v1/employees` - Criar funcionário
  - Validação de dados pessoais
  - Integração com departamentos

- `GET /api/v1/employees/{id}` - Buscar funcionário
  - Cache implementado
  - Retorna dados completos

### Módulo de Departamentos
- `POST /api/v1/departments` - Criar departamento
  - Validação de código único
  - Estrutura hierárquica

- `GET /api/v1/departments/all` - Listar todos
  - Retorno em árvore hierárquica
  - Sem paginação para uso em combos

## 🟡 APIs em Desenvolvimento

### Módulo de Vendas
- `POST /api/v1/sales-orders` - Criar pedido
  - Validação de estoque em andamento
  - Cálculos de valores implementados
  - Falta integração com faturamento

### Módulo de Estoque
- `GET /api/v1/inventory/stock-levels` - Consultar níveis
  - Estrutura básica implementada
  - Falta implementar alertas

## 🔴 APIs Pendentes

### Módulo de Faturamento
- Todas as APIs do módulo de faturamento estão pendentes:
  - Geração de faturas
  - Cálculo de impostos
  - Gestão de descontos

### Módulo de Relatórios
- APIs para geração de relatórios gerenciais
- APIs para dashboards
- APIs para exportação de dados

## 📊 Métricas de Implementação

- Total de APIs Planejadas: 35
- APIs Implementadas: 10 (28.5%)
- APIs em Desenvolvimento: 5 (14.3%)
- APIs Pendentes: 20 (57.2%)

## 🏗️ Detalhes Técnicos das Implementações

### Padrões Implementados
- DTO para todas as respostas
- Tratamento global de exceções
- Validações com Bean Validation
- Documentação Swagger
- Logs estruturados
- Cache com Redis

### Segurança
- Autenticação JWT implementada
- Controle de acesso por roles
- Rate limiting configurado
- Validação de inputs
- Sanitização de dados

### Performance
- Queries otimizadas
- Índices criados
- Cache configurado
- Paginação em todas as listagens

## 📝 Notas de Implementação

### Boas Práticas Adotadas
- Versionamento de APIs (v1)
- Respostas padronizadas
- HTTP Status codes apropriados
- Documentação inline
- Testes unitários

### Pontos de Atenção
1. Necessário revisar performance das queries de estoque
2. Implementar cache distribuído
3. Aumentar cobertura de testes
4. Documentar melhor os erros possíveis

## 🔄 Próximas Implementações Planejadas

1. **Módulo de Faturamento**
   - Prioridade: Alta
   - Previsão: 2 semanas
   - Dependências: Módulo de Vendas

2. **APIs de Relatórios**
   - Prioridade: Média
   - Previsão: 3 semanas
   - Dependências: Todos os módulos base

3. **Integrações**
   - Prioridade: Baixa
   - Previsão: 4 semanas
   - Dependências: APIs base completas

[Outros endpoints omitidos para brevidade - ver documentação completa]

## 📅 Cronograma e Status

### ✅ Concluído (Semana 1-2)
- [x] Configuração do ambiente
- [x] Estrutura base do projeto
- [x] Implementação de autenticação
- [x] APIs principais

### 🚧 Em Desenvolvimento (Semana 3)
- [ ] Frontend base
- [ ] Módulos principais
- [ ] Dashboard

### 📋 Planejado (Semanas 4-6)
- [ ] Módulos de negócio
- [ ] Relatórios
- [ ] Otimizações
- [ ] Deploy

## 🔜 Próximos Passos

1. 🧪 Implementação de testes automatizados
2. 📚 Documentação detalhada com Swagger
3. 🔒 Implementação de segurança avançada
4. 📊 Módulo de relatórios gerenciais
5. 💰 Gestão financeira completa

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

⭐️ Desenvolvido com ❤️ pela equipe Prism ERP
