# Catálogo de Artistas e Álbuns

Solução full stack para gerenciar artistas e álbuns: API REST em Java 25 e Spring Boot 4, SPA em Angular 21 e cliente desktop em Delphi 13. Projeto prático Full Stack Sênior (Anexo II-C).

## Dados de inscrição

| Campo | Valor |
|---|---|
| Candidato | Thiago Fassina |
| Nº de inscrição | _preencher_ |
| Vaga / perfil | Engenheiro de Software Sênior |
| Repositório | https://github.com/Fassium/thiagofassina347593 |

## Tecnologias

Back-end: Java 25, Spring Boot 4.1, Spring Data JPA, Spring Security (JWT), PostgreSQL 16, Flyway, SpringDoc (OpenAPI/Swagger), Maven.

Front-end web: Angular 21, TypeScript, Tailwind CSS v4.

Front-end desktop: Delphi 13 (VCL), RESTRequest4Delphi, dataset-serialize e FireDAC (`FDMemTable`).

## Estrutura do repositório

| Pasta | Conteúdo |
|---|---|
| `backend/` | API REST (Spring Boot) |
| `frontend/web/` | SPA em Angular |
| `frontend/desktop/` | Cliente desktop em Delphi |
| `docker-compose.yml` | Serviços de apoio (hoje, o banco) |

## Como executar

_Preencher quando o `docker-compose` incluir a API e o MinIO. Hoje sobe só o banco:_

```bash
cp .env.example .env
docker compose up -d
cd backend
./mvnw spring-boot:run
```

Variáveis de ambiente que a API lê (modelo em `.env.example`): `POSTGRES_USER`, `POSTGRES_PASSWORD` e `JWT_CHAVE` (Base64, mínimo 32 bytes) são obrigatórias; `DB_URL` (padrão: banco local), `CORS_ORIGENS` (origens permitidas, separadas por vírgula; padrão `http://localhost:4200`) e `SPRING_PROFILES_ACTIVE=dev` (mostra o SQL no log) são opcionais. Nenhuma credencial fica no código.

Swagger: `http://localhost:8080/swagger-ui.html` (faça login em `POST /v1/autenticacao/login`, clique em **Authorize** e cole o `accessToken`).

Usuário de teste criado pela migração `V3`: `thiago@fassium.com` (_informar a senha de teste antes da entrega_).

### Cliente desktop (Delphi)

Requer Delphi 13 (a edição Community serve) e as bibliotecas RESTRequest4Delphi e dataset-serialize no Library path da IDE. Passo a passo em [`frontend/desktop/README.md`](frontend/desktop/README.md).

## Como testar

_Preencher quando houver testes._ `./mvnw test`

## Arquitetura

Camadas por funcionalidade em `backend/src/main/java/br/com/fassium/catalogoartistas`:

- `dominio/<funcionalidade>`: `Controller` → `Service` → `Repository`, com `model/` (entidades) e `dto/` (`Request` e `Response`).
- `infra/configuracao`: configurações (Swagger, paginação, segurança, CORS).
- `infra/handlers` e `common/exceptions`: tratamento de erros em `ProblemDetail` (401, 403, 404, 422).

Cliente desktop, também por funcionalidade, em `frontend/desktop/src`:

- `infra`: configuração, sessão (tokens), requisição HTTP com renovação automática do token e tratamento de erros da API.
- `comum`: classes base compartilhadas (serviço base e telas base de cadastro).
- `autenticacao`, `artistas`, `albuns`: cada funcionalidade com o seu serviço (chama a API) e a sua tela. `principal`: janela principal.
- A tela nunca fala HTTP: ela só chama o serviço da sua funcionalidade.

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
- Autenticação: JWT de 5 minutos (HS256, chave por variável de ambiente) e refresh token aleatório, guardado só como hash (SHA-256), de uso único e com validade configurável (`seguranca.jwt.refresh-expiracao-dias`, padrão 1 dia). Se um refresh token já usado for reapresentado, todos os tokens do usuário são revogados.
- Login com a mesma resposta para e-mail inexistente e senha errada (e tempo de resposta equalizado), para não revelar quais e-mails existem.
- CORS liberado só para as origens configuradas; nunca `*`.
- Álbuns trazem os artistas (relação N:N com o lado inverso `mappedBy`), com um DTO resumido do artista para evitar JSON circular.

## Status dos requisitos

| Requisito | Situação |
|---|---|
| POST, PUT, GET (e DELETE) | feito |
| Paginação dos álbuns, busca e ordenação por nome | feito |
| Endpoints versionados, Flyway, Swagger, N:N | feito |
| JWT (5 min) com renovação | feito |
| CORS (bloquear outros domínios) | feito |
| Álbuns por cantor e/ou banda (consulta parametrizada) | em andamento (os álbuns já trazem os artistas; faltam os filtros `tipoArtista` e `artistaId`) |
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

### Cliente desktop (Delphi)

| Item | Situação |
|---|---|
| Estrutura do projeto, bibliotecas e estilo visual (VCL Style Windows 10) | feito |
| Tela de login e fluxo login → janela principal | feito (validação provisória, ainda sem chamar a API) |
| Login real, renovação de token e tratamento de erros da API | pendente |
| Listagem, cadastro e edição de artistas e álbuns | pendente |
