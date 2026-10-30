# Catálogo de Artistas e Álbuns

Solução full stack (API REST em Java 25 e Spring Boot 4 + SPA em Angular 21) para gerenciar artistas e álbuns. Projeto prático Full Stack Sênior (Anexo II-C).

## Dados de inscrição

| Campo | Valor |
|---|---|
| Candidato | Thiago Fassina |
| Nº de inscrição | _preencher_ |
| Vaga / perfil | _preencher (Analista de Tecnologia da Informação, perfil Engenheiro da Computação, Sênior)_ |
| Repositório | https://github.com/Fassium/thiagofassina347593 |

## Tecnologias

Back-end: Java 25, Spring Boot 4.1, Spring Data JPA, Spring Security (JWT), PostgreSQL 16, Flyway, SpringDoc (OpenAPI/Swagger), Maven.

Front-end: Angular 21, TypeScript, Tailwind CSS v4.

## Como executar

_Preencher quando o `docker-compose` incluir a API e o MinIO. Hoje sobe só o banco:_

```bash
cp .env.example .env
docker compose up -d
cd backend
./mvnw spring-boot:run
```

Variáveis de ambiente que a API lê (modelo em `.env.example`): `POSTGRES_USER`, `POSTGRES_PASSWORD` e `JWT_CHAVE` (Base64, mínimo 32 bytes) são obrigatórias; `DB_URL` (padrão: banco local) e `SPRING_PROFILES_ACTIVE=dev` (mostra o SQL no log) são opcionais. Nenhuma credencial fica no código.

Swagger: `http://localhost:8080/swagger-ui.html` (faça login em `POST /v1/autenticacao/login`, clique em **Authorize** e cole o `accessToken`).

## Como testar

_Preencher quando houver testes._ `./mvnw test`

## Arquitetura

Camadas por funcionalidade em `backend/src/main/java/br/com/fassium/catalogoartistas`:

- `dominio/<funcionalidade>`: `Controller` → `Service` → `Repository`, com `model/` (entidades) e `dto/` (`Request` e `Response`).
- `infra/configuracao`: configurações (Swagger, paginação, segurança).
- `infra/handlers` e `common/exceptions`: tratamento de erros em `ProblemDetail` (404, 422).

## Modelo de dados

- `artista` (`id`, `nome`, `tipo`: `CANTOR` ou `BANDA`)
- `album` (`id`, `titulo`, `ano_lancamento`)
- `artista_album` (N:N)
- `usuario` e `refresh_token` (autenticação)

Todas as tabelas são criadas e populadas por migrações Flyway em `backend/src/main/resources/db/migration`.

## Decisões técnicas

- Endpoints versionados em `/v1`.
- DTOs de entrada e saída separados das entidades; mapeamento manual.
- Validação com Bean Validation; erro de validação responde `422` com os campos inválidos.
- Paginação e ordenação (`page`, `size`, `sort=nome,asc|desc`) via `Pageable`.
- Sem `cascade` no banco: apagar artista não apaga álbuns.

## Status dos requisitos

| Requisito | Situação |
|---|---|
| POST, PUT, GET (e DELETE) | feito |
| Paginação dos álbuns, busca e ordenação por nome | feito |
| Endpoints versionados, Flyway, Swagger, N:N | feito |
| JWT (5 min) com renovação | em andamento |
| CORS (bloquear outros domínios) | pendente |
| Álbuns por cantor e/ou banda (consulta parametrizada) | pendente |
| Upload de capas, MinIO, link pré-assinado de 30 min | pendente |
| Docker (API + MinIO + banco + front) | pendente |
| Health checks (liveness/readiness) | pendente |
| Testes unitários e de integração | pendente |
| WebSocket de novo álbum | pendente |
| Rate limit (10 req/min por usuário) | pendente |
| Regionais (importar e sincronizar) | pendente |
| Front Angular: listagem, detalhe, cadastro/edição, upload de capas | pendente |
| Front: login com renovação de token, lazy loading, Facade + BehaviorSubject | pendente |
| Front: notificação de novo álbum (WebSocket) e testes | pendente |
