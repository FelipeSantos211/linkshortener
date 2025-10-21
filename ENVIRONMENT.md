# Configuração de Variáveis de Ambiente

Este documento explica como configurar e usar variáveis de ambiente no projeto Link Shortener.

## 📋 Variáveis Disponíveis

### **Banco de Dados**

| Variável | Padrão | Descrição |
|----------|---------|-----------|
| `DATABASE_URL` | `jdbc:postgresql://localhost:5432/linkshortener` | URL de conexão com PostgreSQL |
| `DATABASE_USERNAME` | `postgres` | Usuário do banco de dados |
| `DATABASE_PASSWORD` | `maneger` | Senha do banco de dados |

### **JWT (Autenticação)**

| Variável | Padrão | Descrição |
|----------|---------|-----------|
| `JWT_SECRET` | (veja `.env.example`) | Chave secreta para assinatura de tokens |
| `JWT_EXPIRATION_HOURS` | `1` | Tempo de expiração do token em horas |

### **Aplicação**

| Variável | Padrão | Descrição |
|----------|---------|-----------|
| `APP_BASE_URL` | `http://localhost:8080` | URL base para geração de links completos |
| `SERVER_PORT` | `8080` | Porta do servidor HTTP |

### **JPA/Hibernate (Opcional)**

| Variável | Padrão | Descrição |
|----------|---------|-----------|
| `JPA_SHOW_SQL` | `true` | Mostrar SQL no console |
| `JPA_DDL_AUTO` | `update` | Estratégia de criação do schema |

---

## 🐳 Docker Compose

Para usar com Docker Compose, crie um arquivo `.env` na raiz do projeto:

```bash
# .env
DATABASE_URL=jdbc:postgresql://db:5432/linkshortener
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=senha_segura_aqui
JWT_SECRET=sua_chave_jwt_muito_segura_e_aleatoria
APP_BASE_URL=http://localhost:8080
```

Exemplo de `docker-compose.yaml`:

```yaml
version: '3.8'

services:
  db:
    image: postgres:15
    environment:
      POSTGRES_DB: linkshortener
      POSTGRES_USER: ${DATABASE_USERNAME}
      POSTGRES_PASSWORD: ${DATABASE_PASSWORD}
    ports:
      - "5432:5432"

  app:
    build: .
    environment:
      DATABASE_URL: ${DATABASE_URL}
      DATABASE_USERNAME: ${DATABASE_USERNAME}
      DATABASE_PASSWORD: ${DATABASE_PASSWORD}
      JWT_SECRET: ${JWT_SECRET}
      APP_BASE_URL: ${APP_BASE_URL}
    ports:
      - "8080:8080"
    depends_on:
      - db
```

---

## ☁️ Deploy em Cloud (Heroku, AWS, Azure)

### **Heroku**
```bash
heroku config:set DATABASE_URL="jdbc:postgresql://..."
heroku config:set JWT_SECRET="sua_chave_secreta"
heroku config:set APP_BASE_URL="https://seu-app.herokuapp.com"
```

### **AWS Elastic Beanstalk**
Configure no arquivo `.ebextensions/environment.config`:
```yaml
option_settings:
  - option_name: DATABASE_URL
    value: jdbc:postgresql://...
  - option_name: JWT_SECRET
    value: sua_chave_secreta
```

### **Azure App Service**
Configure nas "Application Settings" no portal Azure ou via CLI:
```bash
az webapp config appsettings set --name seu-app --resource-group seu-grupo \
  --settings DATABASE_URL="jdbc:postgresql://..." \
             JWT_SECRET="sua_chave" \
             APP_BASE_URL="https://seu-app.azurewebsites.net"
```

---

## 🔒 Segurança

### **⚠️ IMPORTANTE:**

1. **Nunca commite o arquivo `.env`** com credenciais reais
2. Use o `.env.example` como template
3. **Gere uma chave JWT forte** em produção:
   ```bash
   # Linux/Mac
   openssl rand -base64 64
   
   # Windows PowerShell
   [Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Minimum 0 -Maximum 256 }))
   ```

4. Em produção:
   - Use `JPA_SHOW_SQL=false`
   - Use `JPA_DDL_AUTO=validate` ou `none`
   - Configure `DATABASE_PASSWORD` forte
   - Use HTTPS para `APP_BASE_URL`

---

## 🛠️ Desenvolvimento Local

1. Copie o arquivo de exemplo:
   ```bash
   cp .env.example .env
   ```

2. Ajuste os valores no `.env`

3. Execute a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```

As variáveis de ambiente sobrescrevem os valores padrão do `application.properties`.

---

## 📝 Notas

- Valores entre `${}` no `application.properties` suportam fallback (valor padrão após `:`)
- Formato: `${VARIAVEL:valor_padrao}`
- Se a variável de ambiente não existir, o valor padrão é usado
