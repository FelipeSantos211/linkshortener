# Link Shortener - Docker Compose

Este arquivo explica como executar o projeto usando Docker Compose.

## 🐳 Pré-requisitos

- Docker instalado (versão 20.10+)
- Docker Compose instalado (versão 2.0+)

## 🚀 Como executar

### **1. Configurar variáveis de ambiente (opcional)**

As variáveis já têm valores padrão, mas você pode personalizá-las:

```bash
# Copiar arquivo de exemplo
cp .env.example .env

# Editar valores (opcional)
# As variáveis já têm valores padrão funcionais
```

### **2. Construir e iniciar os containers**

```bash
# Construir e iniciar em modo detached (background)
docker-compose up -d --build

# OU apenas iniciar (se já estiver construído)
docker-compose up -d
```

### **3. Verificar status**

```bash
# Ver logs
docker-compose logs -f

# Ver logs apenas da aplicação
docker-compose logs -f app

# Ver logs apenas do banco
docker-compose logs -f db

# Status dos containers
docker-compose ps
```

### **4. Acessar a aplicação**

- **API:** http://localhost:8080
- **Health Check:** http://localhost:8080/api/auth/health
- **PostgreSQL:** localhost:5432

## 📋 Comandos úteis

### **Parar os containers**
```bash
docker-compose stop
```

### **Parar e remover containers**
```bash
docker-compose down
```

### **Parar e remover containers + volumes**
```bash
# ATENÇÃO: Isto apaga os dados do banco!
docker-compose down -v
```

### **Reconstruir após mudanças no código**
```bash
docker-compose up -d --build
```

### **Acessar o container da aplicação**
```bash
docker-compose exec app sh
```

### **Acessar o PostgreSQL**
```bash
docker-compose exec db psql -U postgres -d linkshortener
```

### **Ver uso de recursos**
```bash
docker-compose stats
```

## 🗂️ Estrutura dos Services

### **db** - PostgreSQL 16
- **Porta:** 5432
- **Volume:** `postgres_data` (persistência de dados)
- **Health Check:** Verifica se o PostgreSQL está pronto

### **app** - Spring Boot Application
- **Porta:** 8080
- **Depende de:** db (aguarda health check)
- **Restart Policy:** unless-stopped

## 🔧 Variáveis de Ambiente

Todas as variáveis são configuráveis via arquivo `.env`:

| Variável | Padrão | Descrição |
|----------|--------|-----------|
| `DATABASE_NAME` | `linkshortener` | Nome do banco de dados |
| `DATABASE_USERNAME` | `postgres` | Usuário do PostgreSQL |
| `DATABASE_PASSWORD` | `maneger` | Senha do PostgreSQL |
| `DATABASE_PORT` | `5432` | Porta exposta do PostgreSQL |
| `JWT_SECRET` | (veja .env.example) | Chave secreta JWT |
| `JWT_EXPIRATION_HOURS` | `1` | Expiração do token em horas |
| `APP_BASE_URL` | `http://localhost:8080` | URL base da aplicação |
| `SERVER_PORT` | `8080` | Porta exposta da aplicação |
| `JPA_SHOW_SQL` | `false` | Mostrar SQL no console |
| `JPA_DDL_AUTO` | `update` | Estratégia DDL do Hibernate |

## 🧪 Testando a API

### **1. Registrar usuário**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "senha123",
    "email": "test@example.com"
  }'
```

### **2. Fazer login**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "senha123"
  }'
```

### **3. Criar link curto**
```bash
# Substituir SEU_TOKEN pelo token recebido no login
curl -X POST http://localhost:8080/links \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN" \
  -d '{
    "urlOriginal": "https://google.com",
    "urlCurta": "google"
  }'
```

### **4. Listar meus links**
```bash
curl -X GET http://localhost:8080/my-links \
  -H "Authorization: Bearer SEU_TOKEN"
```

### **5. Acessar link curto**
```bash
# Redireciona para a URL original
curl -L http://localhost:8080/google
```

## 🛑 Troubleshooting

### **Container não inicia**
```bash
# Ver logs detalhados
docker-compose logs app

# Verificar se a porta 8080 já está em uso
netstat -an | grep 8080  # Linux/Mac
netstat -ano | findstr 8080  # Windows
```

### **Erro de conexão com banco**
```bash
# Verificar se o PostgreSQL está saudável
docker-compose ps

# Reiniciar o banco
docker-compose restart db
```

### **Limpar e recomeçar do zero**
```bash
# Parar tudo e remover volumes
docker-compose down -v

# Limpar imagens antigas
docker system prune -a

# Reconstruir
docker-compose up -d --build
```

## 📦 Volumes

O Docker Compose cria um volume persistente para o PostgreSQL:

- **Nome:** `linkshortener-master_postgres_data`
- **Conteúdo:** Dados do banco de dados
- **Persistência:** Mantém dados entre reinicializações

Para backup do volume:
```bash
docker run --rm -v linkshortener-master_postgres_data:/data -v $(pwd):/backup alpine tar czf /backup/postgres-backup.tar.gz -C /data .
```

## 🌐 Network

Os containers se comunicam via rede bridge:

- **Nome:** `linkshortener-network`
- **Driver:** bridge
- **Isolamento:** Containers podem se comunicar entre si

## 🔒 Segurança em Produção

⚠️ **Antes de usar em produção:**

1. Mude `DATABASE_PASSWORD` para uma senha forte
2. Gere uma chave `JWT_SECRET` aleatória e segura
3. Configure `APP_BASE_URL` com seu domínio real
4. Use `JPA_SHOW_SQL=false`
5. Use `JPA_DDL_AUTO=validate` ou `none`
6. Configure HTTPS/SSL
7. Use secrets do Docker para credenciais

## 📚 Recursos

- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [PostgreSQL Docker Image](https://hub.docker.com/_/postgres)
- [Spring Boot with Docker](https://spring.io/guides/gs/spring-boot-docker/)
