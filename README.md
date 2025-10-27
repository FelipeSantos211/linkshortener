# 🔗 Link Shortener API

API REST completa para encurtamento de URLs com autenticação JWT, desenvolvida em Spring Boot 3.5.4 e Java 21.

## 📋 Sobre o Projeto

Sistema de encurtamento de links que permite usuários autenticados criarem URLs curtas personalizadas ou automáticas, com redirecionamento e contador de cliques, além do gerenciamento dos seus próprios links.

## 🚀 Tecnologias

- Java 21
- Spring Boot 3.5.4 (Web, Security, Data JPA, Validation)
- PostgreSQL 16
- JWT (JSON Web Token)
- Docker & Docker Compose
- Maven
- Lombok

## ✨ Funcionalidades

- Autenticação e registro de usuários com JWT
- Criação de links curtos (personalizados ou aleatórios)
- Redirecionamento público para URL original
- Contador de cliques/acessos
- Listagem dos links do usuário autenticado
- Validações robustas de entrada
- Tratamento global de exceções
- Suporte a variáveis de ambiente e containerização

## 📦 Pré-requisitos

- Docker e Docker Compose
- OU: Java 21 + PostgreSQL 16 + Maven

## 🐳 Execução com Docker (recomendado)

> Observação: em versões mais recentes do Docker, o comando é `docker compose`. Se usar versões mais antigas, utilize `docker-compose`.

```powershell
# 1) Clone o repositório
git clone https://github.com/FelipeSantos211/linkshortener.git
cd linkshortener/linkshortener-master

# 2) Configure variáveis de ambiente (opcional)
Copy-Item .env.example .env
# Edite .env conforme necessário

# 3) Inicie os containers
docker compose up -d --build

# 4) Acompanhe os logs da aplicação
docker compose logs -f app
```

A API estará disponível em: http://localhost:8080

Documentação Docker: `DOCKER.md`

## 💻 Execução local (sem Docker)

```powershell
# 1) Configure o PostgreSQL (crie o banco se necessário)
# Abra seu cliente psql ou GUI e crie o database 'linkshortener'

# 2) Exporte variáveis de ambiente (PowerShell)
$env:DATABASE_URL = "jdbc:postgresql://localhost:5432/linkshortener"
$env:DATABASE_USERNAME = "postgres"
$env:DATABASE_PASSWORD = "sua_senha"
$env:JWT_SECRET = "SUA_CHAVE_SECRETA_MUITO_GRANDE_E_SEGURA_AQUI_123456"  # >= 32 chars

# 3) Execute a aplicação
./mvnw.cmd spring-boot:run
```

## 🔧 Variáveis de ambiente

| Variável | Padrão | Descrição |
|---|---|---|
| `DATABASE_URL` | `jdbc:postgresql://localhost:5432/linkshortener` | URL JDBC do banco |
| `DATABASE_USERNAME` | `postgres` | Usuário do PostgreSQL |
| `DATABASE_PASSWORD` | `maneger` | Senha do PostgreSQL |
| `JWT_SECRET` | ver `.env.example` | Chave secreta JWT (≥ 32 caracteres para HS256) |
| `JWT_EXPIRATION_HOURS` | `1` | Expiração do token em horas |
| `APP_BASE_URL` | `http://localhost:8080` | URL base usada para montar links curtos |
| `SERVER_PORT` | `8080` | Porta HTTP do servidor |

Mais detalhes: `ENVIRONMENT.md`.

## 📡 Endpoints da API

### Autenticação (`/auth`)

Registrar usuário

```http
POST /auth/register
Content-Type: application/json

{
  "username": "johndoe",
  "password": "senha123",
  "email": "john@example.com"
}
```

Login

```http
POST /auth/login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "senha123"
}
```

Resposta

```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "username": "johndoe",
  "message": "Autenticação realizada com sucesso"
}
```

Health Check

```http
GET /auth/health
```

### Links (requer autenticação)

Criar link curto

```http
POST /links
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "urlOriginal": "https://www.google.com",
  "urlCurta": "google"  // opcional, gerado automaticamente se omitido
}
```

Listar meus links

```http
GET /my-links
Authorization: Bearer {seu_token}
```

Resposta

```json
[
  {
    "id": 1,
    "urlOriginal": "https://www.google.com",
    "urlCurta": "http://localhost:8080/google",
    "contadorCliques": 5,
    "dataCriacao": "2025-10-21T10:30:00",
    "username": "johndoe"
  }
]
```

  Deletar link (apenas proprietário)

  ```http
  DELETE /links/{id}
  Authorization: Bearer {seu_token}
  ```

  Comportamento

  - Remove o link identificado por `id` se o usuário autenticado for o proprietário.
  - Retorna HTTP 204 No Content em sucesso.
  - Retorna erro (4xx/5xx) em caso de link não encontrado ou usuário não autorizado.

Redirecionar (público)

```http
GET /{shortUrl}
```

Redireciona para a URL original e incrementa o contador.

## 🏗️ Estrutura do projeto

```
src/main/java/com/santos/linkshortener/
├── controller/          # Controladores REST
├── dto/                 # Data Transfer Objects
├── exception/           # Exceções customizadas e handlers
├── model/               # Entidades JPA
├── repository/          # Repositórios Spring Data
├── security/            # Config de segurança e JWT
├── service/             # Lógica de negócio
├── util/                # Utilitários (JWT, gerador de links)
└── validation/          # Validadores customizados
```

## 🧪 Testando a API

PowerShell (Windows):

```powershell
# 1) Registrar
curl -X POST http://localhost:8080/auth/register `
  -H "Content-Type: application/json" `
  -d '{"username":"testuser","password":"senha123","email":"test@example.com"}'

# 2) Login (capturar token)
$resp = curl -X POST http://localhost:8080/auth/login `
  -H "Content-Type: application/json" `
  -d '{"username":"testuser","password":"senha123"}'
$token = ($resp.Content | ConvertFrom-Json).token

# 3) Criar link
curl -X POST http://localhost:8080/links `
  -H "Authorization: Bearer $token" `
  -H "Content-Type: application/json" `
  -d '{"urlOriginal":"https://github.com","urlCurta":"gh"}'

# 4) Deletar link
curl -X DELETE http://localhost:8080/links/1 `
  -H "Authorization: Bearer $token"

# 5) Acessar link curto
curl -L http://localhost:8080/gh
```

## 🔒 Segurança

- Senhas com BCrypt
- Autenticação stateless com JWT
- Tokens com expiração configurável
- Validação de entrada em todos os endpoints
- CORS configurado
- Proteção contra SQL Injection (JPA)

## 🛠️ Desenvolvimento

```powershell
# Compilar
./mvnw.cmd clean package

# Executar testes
./mvnw.cmd test

# Build da imagem Docker
docker build -t linkshortener:latest .
```

## 🧩 Troubleshooting

- Banco não sobe no Docker: verifique variáveis `DATABASE_*` no `.env` e portas em uso (`5432`).
- Falha ao gerar token JWT: garanta que `JWT_SECRET` tenha pelo menos 32 caracteres (HS256 requer ≥ 256 bits).
- 403/401 ao acessar `/links` ou `/my-links`: envie `Authorization: Bearer {token}` retornado no login.

## 📚 Documentação adicional

- `DOCKER.md` – Guia Docker/Docker Compose
- `ENVIRONMENT.md` – Variáveis de ambiente

## 🤝 Contribuindo

1. Faça um fork
2. Crie uma branch (`git checkout -b feature/nova-funcionalidade`)
3. Commit (`git commit -m "feat: adiciona nova funcionalidade"`)
4. Push (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

## 📝 Licença

Projeto sob licença MIT.

## 👤 Autor

Felipe Santos — GitHub: [@FelipeSantos211](https://github.com/FelipeSantos211)

---

⭐ Se este projeto foi útil, considere dar uma estrela!