# Railway Deployment Guide for Microlend

This guide will help you deploy the Microlend Spring Boot application to Railway.

## Prerequisites

1. Railway account (sign up at https://railway.app/)
2. Railway CLI (optional but recommended): `npm i -g @railway/cli`

## Deployment Steps

### 1. Create a New Project on Railway

1. Go to [Railway Dashboard](https://railway.app/dashboard)
2. Click "New Project"
3. Select "Deploy from GitHub repo" (or "Empty Project" if deploying manually)

### 2. Add PostgreSQL Database

1. In your Railway project, click "New"
2. Select "Database" → "PostgreSQL"
3. Railway will automatically provision a PostgreSQL database
4. Note: Railway will automatically create these environment variables:
   - `DATABASE_URL` (in format: postgresql://user:password@host:port/database)
   - `PGHOST`, `PGPORT`, `PGUSER`, `PGPASSWORD`, `PGDATABASE`

### 3. Configure Environment Variables

In your Railway service settings, add the following environment variables:

#### Required Variables:
```bash
# Spring Profile
SPRING_PROFILES_ACTIVE=prod

# Database (Railway automatically sets these when you add PostgreSQL)
# No manual configuration needed - Railway provides:
# PGHOST, PGPORT, PGDATABASE, PGUSER, PGPASSWORD

# Google OAuth
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret

# JWT Configuration
JWT_SECRET=your-secure-jwt-secret-minimum-256-bits
JWT_EXPIRATION_MS=3600000

# DAML Configuration
DAML_JSON_API_URL=http://your-daml-json-api-url:7575
DAML_ADMIN_TOKEN=your-daml-admin-token
```

#### Optional Variables:
```bash
# Server Port (Railway auto-detects, but you can specify)
PORT=8080

# Database Pool Configuration
DB_POOL_SIZE=10
DB_MIN_IDLE=2

# Application Logging
APP_LOG_LEVEL=INFO
```

### 4. Deploy the Application

#### Option A: Deploy from GitHub (Recommended)

1. Connect your GitHub repository to Railway
2. Railway will automatically:
   - Detect the Dockerfile
   - Build the Docker image
   - Deploy the application
3. Railway will provide a public URL for your application

#### Option B: Deploy using Railway CLI

```bash
# Login to Railway
railway login

# Link to your project
railway link

# Deploy
railway up
```

### 5. Set Up DAML Services

Since your application depends on DAML services, you have two options:

#### Option A: Deploy DAML Services on Railway

1. Create separate services for:
   - DAML Sandbox
   - DAML JSON API

2. Use the following Dockerfile for DAML services:

**Dockerfile.daml-sandbox:**
```dockerfile
FROM digitalasset/daml-sdk:2.10.2
COPY canton-sandbox.conf /canton-sandbox.conf
EXPOSE 6865
CMD ["daml", "sandbox", "--wall-clock-time", "--port", "6865", "-c", "/canton-sandbox.conf"]
```

**Dockerfile.daml-json-api:**
```dockerfile
FROM digitalasset/daml-sdk:2.10.2
EXPOSE 7575
CMD ["daml", "json-api", "--ledger-host", "${DAML_SANDBOX_HOST}", "--ledger-port", "6865", "--address", "0.0.0.0", "--http-port", "7575", "--allow-insecure-tokens"]
```

#### Option B: Use External DAML Services

Host your DAML services elsewhere and update the `DAML_JSON_API_URL` environment variable accordingly.

### 6. Health Check Configuration

Railway will automatically detect the health check endpoint. The Dockerfile includes a health check that calls:
```
http://localhost:8080/actuator/health
```

Make sure Spring Boot Actuator is properly configured (already set up in application-prod.yaml).

### 7. Verify Deployment

1. Check the deployment logs in Railway dashboard
2. Access your application URL
3. Test the health endpoint: `https://your-app.railway.app/actuator/health`

## Important Notes

### Google OAuth Redirect URIs

Update your Google Cloud Console OAuth 2.0 credentials to include your Railway URLs:
- Authorized JavaScript origins: `https://your-app.railway.app`
- Authorized redirect URIs: `https://your-app.railway.app/login/oauth2/code/google`

### Database Migrations

Flyway migrations will run automatically on application startup. Ensure your migration scripts are in `src/main/resources/db/migration`.

### Railway Networking

- Services within the same Railway project can communicate using private networking
- Use service names as hostnames (e.g., `postgres`, `daml-sandbox`)
- Railway provides automatic service discovery

### Monitoring and Logs

- View real-time logs in Railway dashboard
- Set up log drains for external logging services if needed
- Use the `/actuator/metrics` endpoint for application metrics

## Troubleshooting

### Application won't start
- Check environment variables are set correctly
- Verify database connection string
- Review logs for specific error messages

### Database connection issues
- Ensure DATABASE_URL format is correct
- Check database service is running
- Verify network connectivity between services

### DAML connection issues
- Verify DAML_JSON_API_URL is accessible
- Check DAML services are running
- Validate DAML_ADMIN_TOKEN

## Cost Optimization

1. Use Railway's hobby plan for development ($5/month)
2. Scale resources based on actual usage
3. Consider using Railway's sleep mode for non-production environments

## Additional Resources

- [Railway Documentation](https://docs.railway.app/)
- [Spring Boot on Railway](https://docs.railway.app/guides/java)
- [Railway CLI Reference](https://docs.railway.app/develop/cli)
