# 🌈 Prism ERP - Sistema de Gestão Empresarial

> Sistema ERP moderno e eficiente desenvolvido com Spring Boot e Angular, seguindo as melhores práticas de desenvolvimento

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-17-red)](https://angular.io/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.x-blue)](https://www.typescriptlang.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias](#-tecnologias)
- [Arquitetura](#-arquitetura)
- [Instalação](#-instalação)
- [Frontend (Angular)](#-frontend-angular)
- [Backend (Spring Boot)](#-backend-spring-boot)
- [API Endpoints](#-api-endpoints)
- [Cronograma e Status](#-cronograma-e-status)
- [Próximos Passos](#-próximos-passos)
- [Contribuição](#-contribuição)
- [Licença](#-licença)

## 🎯 Sobre o Projeto

O Prism ERP é um sistema de gestão empresarial completo, construído com Angular 17 e Spring Boot 3. O sistema oferece uma experiência moderna para gestão empresarial, com interface responsiva, módulos integrados e alta performance.

## ✨ Funcionalidades

### Backend (Spring Boot)

🔹 **Gestão de Clientes**
- Cadastro completo de clientes
- Histórico de interações
- Gestão de documentos
- Cache implementado
- Validações de documentos

🔹 **Gestão de Funcionários**
- Cadastro de funcionários
- Vínculo com departamentos
- Gestão de cargos e funções
- Histórico de alterações
- Integração departamental

🔹 **Gestão de Departamentos**
- Estrutura organizacional
- Hierarquia de gestão
- KPIs departamentais
- Relatórios gerenciais

🔹 **Vendas e Faturamento**
- Pedidos de venda
- Acompanhamento de status
- Histórico de transações
- Geração de faturas
- Cálculo de impostos
- Gestão de descontos

🔹 **Gestão de Estoque**
- Controle de entrada/saída
- Alertas de baixo estoque
- Relatórios de movimentação
- Gestão de lotes
- Inventário

### Frontend (Angular)

🔹 **Interface do Usuário**
- Dashboard personalizado
- Sistema de navegação intuitivo
- Temas claro/escuro
- Interface responsiva
- Formulários dinâmicos
- Validações em tempo real

🔹 **Gestão de Usuários**
- Login com JWT
- Controle de permissões
- Múltiplos perfis
- Registro de atividades
- Recuperação de senha
- Autenticação 2FA (planejado)

🔹 **Dashboards e Relatórios**
- Gráficos interativos
- Relatórios personalizáveis
- Exportação de dados
- KPIs em tempo real
- Análises avançadas

## 💻 Tecnologias

### Backend
- Java 17
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- PostgreSQL
- Lombok
- MapStruct
- Bean Validation
- Swagger/OpenAPI
- Maven

### Frontend
- Angular 17
- TypeScript 5
- Angular Material
- NgRx
- RxJS
- PrimeNG
- Chart.js
- Angular JWT
- SCSS

## 🏛️ Arquitetura

### Backend
```
src/
├── main/java/com/prism/erp/
│   ├── config/      # Configurações
│   ├── controller/  # Controllers REST
│   ├── service/     # Lógica de negócio
│   ├── repository/  # Acesso a dados
│   ├── model/       # Entidades
│   └── dto/         # DTOs
```

### Frontend
```
src/
├── app/
│   ├── core/              # Serviços e guardas
│   ├── features/          # Módulos principais
│   ├── shared/           # Componentes compartilhados
│   └── store/            # Estado global (NgRx)
├── assets/
└── environments/
```

## Cronograma de Desenvolvimento (6 Semanas)

**Progresso atual: ~30-40% (Semana 3)**

**Semana 1: Configuração do Ambiente e Estrutura Base**

*   [x] Configurar ambiente de desenvolvimento Java/Spring
*   [x] Configurar ambiente Node.js/Angular
*   [x] Instalar ferramentas necessárias (IDE, Git, Docker)
*   [x] Criar repositórios Git
*   [x] Configurar CI/CD básico
*   [x] Configurar projeto Spring Boot
*   [x] Implementar autenticação JWT
*   [x] Configurar Spring Security
*   [x] Definir estrutura base de APIs
*   [x] Configurar banco de dados
*   [x] Implementar estrutura base de testes

**Semana 2: Core Backend**

*   [x] Desenvolver entidades principais
*   [x] Implementar repositórios base
*   [x] Criar serviços core
*   [x] Configurar Redis para cache
*   [x] Implementar logging
*   [x] Desenvolver APIs de autenticação
*   [x] Implementar APIs de usuários
*   [x] Criar APIs de produtos
*   [x] Desenvolver APIs de clientes
*   [x] Implementar validações

**Semana 3: Frontend Base e Módulos Principais**

*   [x] Configurar projeto Angular
*   [x] Implementar autenticação frontend
*   [x] Configurar rotas principais
*   [x] Implementar interceptors
*   [x] Configurar Tailwind e Bootstrap
*   [x] Desenvolver dashboard principal
*   [x] Implementar gestão de usuários
*   [x] Criar módulo de produtos
*   [x] Desenvolver módulo de clientes
*   [x] Implementar navegação principal

**Semana 4: Módulos de Negócio**

*   **Backend:**
    *   **Vendas:** Criar entidades, DTOs, Repositórios, Serviços e APIs REST para vendas e itens de venda. Implementar regras de negócio, validações e tratamento de erros.
    *   **Financeiro:** Criar entidades, DTOs, Repositórios, Serviços e APIs REST para contas e lançamentos. Implementar regras de negócio, validações, tratamento de erros e integração com gateway de pagamento (se aplicável).
    *   **Estoque:** Criar entidades, DTOs, Repositórios, Serviços e APIs REST para movimentações de estoque. Implementar regras de negócio, validações, tratamento de erros e integração com sistema de gestão de estoque (se aplicável).
    *   **RH:** Criar entidades, DTOs, Repositórios, Serviços e APIs REST para folha de pagamento e funcionários. Implementar regras de negócio, validações e tratamento de erros.
    *   **Compras:** Criar entidades, DTOs, Repositórios, Serviços e APIs REST para pedidos de compra e itens de pedido. Implementar regras de negócio, validações e tratamento de erros.
*   **Frontend:**
    *   Criar componentes Angular para cada módulo, com interfaces para cadastro, edição, listagem, visualização e outras operações relevantes. Integrar com os serviços do backend, implementar tratamento de erros e aplicar estilos consistentes.

**Semana 5: Relatórios e Recursos Avançados**

*   **Backend:**
    *   **Relatórios:** Implementar geração de relatórios em PDF, Excel, etc. usando bibliotecas como iText, Apache POI ou JasperReports. Criar APIs REST para download.
    *   **Notificações:** Implementar sistema de notificações (email, SMS, push) usando serviços como SendGrid, Twilio ou Firebase. Integrar com os módulos de negócio.
    *   **Jobs Agendados:** Implementar jobs agendados (Spring Scheduler ou Quartz) para tarefas como envio de relatórios e backups.
    *   **Upload de Arquivos:** Implementar upload de arquivos (Apache Commons FileUpload ou Spring MultipartFile). Armazenar arquivos localmente ou em nuvem (AWS S3, Google Cloud Storage).
    *   **Dashboards:** Criar APIs REST para fornecer dados para dashboards. Implementar lógica de agregação e processamento de dados.
*   **Frontend:**
    *   **Relatórios:** Criar componentes para visualizar relatórios, com filtros e opções de personalização.
    *   **Notificações:** Criar componentes para exibir notificações, com interação (marcar como lida, excluir).
    *   **Upload de Arquivos:** Criar componentes para upload, com exibição de progresso e tratamento de erros.
    *   **Dashboards:** Criar dashboards interativos (Chart.js, Highcharts, D3.js). Integrar com APIs do backend, com filtros e opções de visualização.
    *   **Exportação PDF/Excel:** Integrar bibliotecas para exportação de dados.

**Semana 6: Testes, Otimização e Deploy**

*   **Testes:** Implementar testes unitários, de integração, end-to-end e de carga. Usar frameworks como JUnit, Mockito, Jest e Cypress.
*   **Otimização:** Otimizar queries SQL, implementar caching, otimizar código Java e JavaScript, lazy loading, tamanho de bundles e SEO. Realizar testes de performance.
*   **Deploy:** Configurar ambiente de produção, criar scripts de deploy automatizados, realizar deploy, configurar monitoramento, documentar o processo e usar ferramentas de CI/CD (Jenkins, GitLab CI/CD, GitHub Actions).


### Sprint 4-6 (Planejado)
- [ ] Módulo Financeiro completo
- [ ] NFe e Fiscal
- [ ] Relatórios avançados
- [ ] Dashboards personalizados
- [ ] Otimizações gerais

## 📈 Métricas de Desenvolvimento

```
Módulos Base............: 100%  ███████████
Backend.................: 40%   ████░░░░░░░
Frontend...............: 40%   ████░░░░░░░
Testes.................: 10%   █░░░░░░░░░
Documentação...........: 20%   ██░░░░░░░░

```

## 🔜 Próximos Passos

1. Implementar módulo financeiro completo
2. Desenvolver NFe e gestão fiscal
3. Criar dashboards avançados
4. Implementar relatórios customizáveis
5. Otimizar performance geral
6. Expandir cobertura de testes

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