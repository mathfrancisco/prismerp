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

## 📅 Cronograma e Status

### Sprint 1-2 (Concluído)
- [x] Setup do ambiente
- [x] Estrutura base (Frontend/Backend)
- [x] Autenticação JWT
- [x] CRUD de Clientes
- [x] CRUD de Funcionários
- [x] Layout responsivo

### Sprint 3 (Em Desenvolvimento)
- [ ] Módulo de Vendas
- [ ] Dashboard com gráficos
- [ ] Gestão de Estoque
- [ ] Relatórios básicos
- [ ] Integrações iniciais

### Sprint 4-6 (Planejado)
- [ ] Módulo Financeiro completo
- [ ] NFe e Fiscal
- [ ] Relatórios avançados
- [ ] Dashboards personalizados
- [ ] Otimizações gerais

## 📈 Métricas de Desenvolvimento

```
Módulos Base............: 100%  ███████████
Backend.................: 75%   ████████░░░
Frontend...............: 60%   ██████░░░░░
Testes.................: 70%   ███████░░░░
Documentação...........: 80%   ████████░░░
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