# Catálogo de Artistas e Álbuns: front desktop (Delphi)

Cliente VCL da API do catálogo. Mesmas telas do front web: login, artistas e álbuns.

## Requisitos
- Delphi 13 (Community serve) e a API rodando em `http://localhost:8080`.
- Bibliotecas de terceiros, só fontes, no **Library path** da IDE (Windows 32-bit):
  - [RESTRequest4Delphi](https://github.com/viniciussanchez/RESTRequest4Delphi)
  - [dataset-serialize](https://github.com/viniciussanchez/dataset-serialize)

## Como rodar
1. Copie `catalogo.ini.example` para `catalogo.ini` (na mesma pasta do `.exe`) e ajuste a URL da API, se precisar.
2. Abra `Catalogo.dproj`, compile e execute.

## Organização
- `src/infra`: configuração, sessão (tokens), requisição HTTP com refresh automático e tratamento de erros da API.
- `src/servicos`: um serviço por recurso (autenticação, artistas, álbuns); é aqui que se chama a API.
- `src/views`: as telas. A tela nunca fala HTTP, só chama o serviço.
