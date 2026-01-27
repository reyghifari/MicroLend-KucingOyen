# Railway Deployment Files Summary

This document summarizes the files created for deploying your Microlend application to Railway.

## Files Created

### 1. **Dockerfile**
A production-ready multi-stage Dockerfile that:
- ✅ Uses Maven 3.9.9 with Java 21 for building
- ✅ Creates a minimal runtime image with Eclipse Temurin JRE
- ✅ Implements security best practices (non-root user)
- ✅ Optimizes JVM settings for containerized environments
- ✅ Includes health check endpoint
- ✅ Uses layer caching for faster builds

**Key Features:**
- Multi-stage build reduces final image size
- Runs as non-root `spring` user for security
- Container-optimized JVM settings (UseContainerSupport, MaxRAMPercentage)
- Health check on `/actuator/health` endpoint
- Port 8080 exposed

### 2. **.dockerignore**
Optimizes Docker build by excluding:
- Git files and IDE configurations
- Build artifacts and test files
- Documentation files
- Local Docker and Canton configuration files

**Benefits:**
- Faster builds (smaller context)
- Smaller Docker images
- Better security (no sensitive files)

### 3. **application-prod.yaml**
Production configuration profile with:
- ✅ Environment variable-based configuration
- ✅ Database connection pooling (HikariCP)
- ✅ Production logging levels
- ✅ Spring Boot Actuator endpoints enabled
- ✅ OAuth2 and JWT configuration
- ✅ DAML integration settings

**Environment Variables Required:**
```
SPRING_PROFILES_ACTIVE=prod
PORT=8080 (Railway auto-detects)
# Database variables auto-set by Railway: PGHOST, PGPORT, PGDATABASE, PGUSER, PGPASSWORD
GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET
JWT_SECRET, JWT_EXPIRATION_MS
DAML_JSON_API_URL, DAML_ADMIN_TOKEN
```

### 4. **RAILWAY_DEPLOYMENT.md**
Complete deployment guide including:
- Step-by-step Railway setup
- Environment variable configuration
- DAML service deployment options
- Google OAuth setup instructions
- Troubleshooting tips
- Cost optimization strategies

### 5. **pom.xml** (Updated)
Added Spring Boot Actuator dependency for:
- Health check endpoints (`/actuator/health`)
- Metrics monitoring (`/actuator/metrics`)
- Application info endpoint (`/actuator/info`)

## Next Steps

### 1. Test Locally with Docker
```bash
# Build the image
docker build -t microlend:latest .

# Test run (need to set environment variables)
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DATABASE_URL=jdbc:postgresql://host.docker.internal:5437/microlend \
  -e DB_USERNAME=admin \
  -e DB_PASSWORD=admin \
  -e GOOGLE_CLIENT_ID=your-id \
  -e GOOGLE_CLIENT_SECRET=your-secret \
  -e JWT_SECRET=your-secret \
  -e DAML_JSON_API_URL=http://host.docker.internal:7575 \
  -e DAML_ADMIN_TOKEN=your-token \
  microlend:latest
```

### 2. Deploy to Railway

#### Quick Start:
1. Install Railway CLI: `npm i -g @railway/cli`
2. Login: `railway login`
3. Create project: `railway init`
4. Add PostgreSQL: In Railway dashboard, click "New" → "Database" → "PostgreSQL"
5. Set environment variables (see RAILWAY_DEPLOYMENT.md)
6. Deploy: `railway up`

#### Via GitHub:
1. Push your code to GitHub
2. In Railway dashboard, click "New Project" → "Deploy from GitHub repo"
3. Select your repository
4. Add PostgreSQL database
5. Configure environment variables
6. Railway will auto-deploy

### 3. Configure External Services

You need to set up:

**Google OAuth:**
- Add Railway URL to authorized redirect URIs
- Format: `https://your-app.railway.app/login/oauth2/code/google`

**DAML Services:**
- Deploy DAML Sandbox and JSON API (see deployment guide)
- Or use external DAML services

### 4. Verify Deployment

Check these endpoints after deployment:
```
https://your-app.railway.app/actuator/health
https://your-app.railway.app/actuator/info
```

## Important Notes

### Security Considerations
- ✅ **Never commit secrets** to Git
- ✅ Use Railway environment variables for all sensitive data
- ✅ Regularly rotate JWT secrets and OAuth credentials
- ✅ Review application logs for security issues

### Database Migrations
- Flyway migrations run automatically on startup
- Ensure your migration scripts are tested
- Railway PostgreSQL includes automatic backups

### Monitoring
- Use Railway's built-in logging
- Monitor `/actuator/health` for application health
- Set up alerts for deployment failures

### Cost Management
- Railway charges based on resource usage
- Use sleep mode for development environments
- Monitor your usage in Railway dashboard
- Consider using Railway's Hobby plan ($5/month) for testing

## Troubleshooting

**Build Failures:**
- Check Dockerfile syntax
- Verify Maven dependencies resolve
- Review build logs in Railway

**Runtime Errors:**
- Verify all environment variables are set
- Check database connectivity
- Review application logs in Railway dashboard

**Health Check Failures:**
- Ensure Spring Boot Actuator is working
- Verify port 8080 is accessible
- Check application startup logs

## Support Resources

- [Railway Documentation](https://docs.railway.app/)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
- Railway Support: support@railway.app

---

**Ready to deploy!** Follow the RAILWAY_DEPLOYMENT.md guide for detailed instructions.
