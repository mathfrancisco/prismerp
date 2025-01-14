# 🌈 Prism ERP - Sistema de Gestão Empresarial

> Sistema ERP corporativo de última geração desenvolvido com Spring Boot 3 e Angular 17, oferecendo uma solução completa para gestão empresarial moderna e eficiente.

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-17-red)](https://angular.io/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.x-blue)](https://www.typescriptlang.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Recursos e Funcionalidades](#-recursos-e-funcionalidades)
- [Stack Tecnológica](#-stack-tecnológica)
- [Arquitetura](#-arquitetura)
- [Status do Projeto](#-status-do-projeto)
- [Guia de Instalação](#-guia-de-instalação)
- [Roadmap](#-roadmap)
- [Como Contribuir](#-como-contribuir)
- [Licença](#-licença)

## 🎯 Visão Geral

O Prism ERP é uma plataforma empresarial moderna que integra todos os aspectos críticos da gestão empresarial em uma única solução. Desenvolvido com foco em:

- **Performance**: Arquitetura otimizada para alta performance e escalabilidade
- **Usabilidade**: Interface moderna e intuitiva com design responsivo
- **Segurança**: Implementação robusta de autenticação e autorização
- **Extensibilidade**: Arquitetura modular permitindo fácil extensão
- **Integrações**: Conectividade com sistemas externos através de APIs RESTful

## ✨ Recursos e Funcionalidades

### 🔹 Módulos Core

**Gestão Comercial**
- Sistema completo de vendas com fluxo de aprovação
- Gestão de pedidos e orçamentos
- Política de preços e descontos personalizável
- Integração com NFe/NFCe
- CRM com gestão de leads e oportunidades

**Gestão Financeira**
- Contas a pagar e receber
- Fluxo de caixa e DRE
- Conciliação bancária automática
- Centro de custos e profit centers
- Gestão de investimentos e ativos

**Controle de Estoque**
- Gestão avançada de inventário
- Controle de lotes e validade
- Sistema de reservas
- Rastreabilidade completa
- Gestão de armazéns múltiplos

**Recursos Humanos**
- Cadastro completo de funcionários
- Gestão de cargos e salários
- Controle de ponto integrado
- Folha de pagamento
- Avaliação de desempenho

**Compras**
- Gestão de fornecedores
- Cotações e pedidos de compra
- Workflow de aprovação
- Controle de recebimento
- Gestão de contratos

### 🔹 Recursos Técnicos

**Backend**
- Arquitetura em camadas
- Cache distribuído com Redis
- Auditoria completa de operações
- Multitenancy
- Jobs agendados
- Documentação OpenAPI

**Frontend**
- Design system próprio
- Temas personalizáveis
- Gráficos e dashboards interativos
- Formulários dinâmicos
- PWA support
- Exportação para Excel/PDF

## 💻 Stack Tecnológica

### Backend Core
- Java 17
- Spring Boot 3.x
- Spring Security com JWT
- Spring Data JPA/Hibernate
- QueryDSL
- MapStruct
- Redis
- PostgreSQL
- RabbitMQ

### Frontend Core
- Angular 17
- TypeScript 5
- NgRx para estado
- RxJS
- Angular Material
- PrimeNG
- TailwindCSS
- Chart.js

### DevOps & Ferramentas
- Docker/Docker Compose
- GitLab CI/CD
- SonarQube
- ELK Stack
- Prometheus/Grafana
- JUnit/Mockito
- Cypress

## 🏛️ Arquitetura

### Estrutura Backend
```
src/
├── main/java/com/prism/erp/
│   ├── config/         # Configurações Spring e infraestrutura
│   ├── controller/     # APIs REST e endpoints
│   ├── service/        # Lógica de negócio e regras
│   ├── repository/     # Camada de persistência
│   ├── model/          # Entidades e modelos
│   ├── dto/            # Objetos de transferência
│   ├── security/       # Configurações de segurança
│   ├── validation/     # Validadores customizados
│   └── exception/      # Tratamento de exceções
```

### Estrutura Frontend
```
src/
├── app/
│   ├── core/          # Serviços e guardas core
│   ├── features/      # Módulos funcionais
│   ├── shared/        # Componentes compartilhados
│   ├── store/         # Estado global NgRx
│   └── layout/        # Componentes de layout
├── assets/            # Recursos estáticos
└── environments/      # Configurações por ambiente
```

## 📊 Status do Projeto (Sprint 4 - 65% Concluído)

### Módulos Implementados
```
Gestão de Clientes.....: 100%  ██████████
Vendas................: 90%   █████████░
Financeiro............: 85%   ████████░░
Estoque..............: 80%   ████████░░
Compras..............: 75%   ███████░░░
RH...................: 60%   ██████░░░░
```

### Aspectos Técnicos
```
APIs Core.............: 95%   █████████░
Autenticação.........: 100%  ██████████
Frontend Base........: 85%   ████████░░
Testes...............: 40%   ████░░░░░░
Documentação.........: 55%   █████░░░░░
```

## 🔄 Integrações Principais

- **Vendas → Financeiro**
    - Geração automática de contas a receber
    - Baixa automática de títulos
    - Controle de inadimplência
    - Integração com NFe

- **Compras → Estoque**
    - Entrada automática de mercadorias
    - Controle de pedidos pendentes
    - Gestão de divergências
    - Rastreabilidade completa

- **RH → Financeiro**
    - Processamento de folha
    - Provisões automáticas
    - Gestão de benefícios
    - Reembolsos

## 🛡️ Segurança

- Autenticação JWT
- Controle granular de permissões
- Criptografia de dados sensíveis
- Proteção contra CSRF/XSS
- Auditoria completa
- Rate limiting
- 2FA (em implementação)

## 🔜 Próximas Entregas

1. **Integração Fiscal Completa**
    - NFe/NFCe
    - CTe
    - MDFe
    - SPED

2. **Módulos Avançados**
    - BI e Analytics
    - CRM avançado
    - Gestão de Projetos
    - Controle de Produção

3. **Melhorias Técnicas**
    - Microsserviços
    - Cache distribuído
    - Message queues
    - Observabilidade

## 🤝 Como Contribuir

1. Fork o projeto
2. Configure o ambiente local:
   ```bash
   # Clone o repositório
   git clone https://github.com/seu-usuario/prism-erp.git
   
   # Instale as dependências
   cd prism-erp
   ./mvnw install  # Backend
   npm install     # Frontend
   ```
3. Crie uma branch (`git checkout -b feature/AmazingFeature`)
4. Commit suas mudanças (`git commit -m 'Add: nova funcionalidade'`)
5. Push para a branch (`git push origin feature/AmazingFeature`)
6. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

⭐️ Desenvolvido com ❤️ pela equipe Prism ERP

[Documentação Completa](docs/index.md) | [Guia de Desenvolvimento](docs/development.md) | [Changelog](CHANGELOG.md)