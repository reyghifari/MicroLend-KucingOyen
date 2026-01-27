# Deploy DAML Services to Railway - Quick Guide

This guide explains how to deploy DAML Sandbox and DAML JSON API as separate services on Railway to work with your Microlend application.

## Overview

Railway doesn't support docker-compose, so we deploy each service separately:
1. **DAML Sandbox** - The DAML ledger
2. **DAML JSON API** - HTTP API for ledger operations
3. **Microlend App** - Your Spring Boot application

These services communicate via **Railway's private networking** using `.railway.internal` domains.

---

## Step 1: Deploy DAML Sandbox

### 1.1 Create New Service
1. Go to your Railway project dashboard
2. Click **"New"** → **"Empty Service"**
3. Name it: `daml-sandbox`

### 1.2 Connect GitHub Repository
1. In the service settings, click **"Settings"** tab
2. Under **"Source"**, click **"Connect Repo"**
3. Select your `microlend` repository
4. Under **"Build"**, set:
   - **Dockerfile Path**: `Dockerfile.daml-sandbox`
   - **Docker Build Context**: `/` (root)

### 1.3 Configure Service
1. **Port Settings**:
   - Railway auto-detects port `6865`
   - No public domain needed (internal only)

2. **Health Check** (optional):
   - Path: `/health`
   - Port: `6865`

### 1.4 Deploy
- Click **"Deploy"**
- Wait for deployment to complete
- Check logs for: `"Ledger API server started on port 6865"`

---

## Step 2: Deploy DAML JSON API

### 2.1 Create New Service
1. Click **"New"** → **"Empty Service"**
2. Name it: `daml-json-api`

### 2.2 Connect GitHub Repository
1. Connect same repository
2. Set **Dockerfile Path**: `Dockerfile.daml-json-api`

### 2.3 Configure Environment Variables
Add these variables in the service's **"Variables"** tab:

```bash
DAML_SANDBOX_HOST=daml-sandbox.railway.internal
DAML_SANDBOX_PORT=6865
```

### 2.4 Configure Port
- Railway auto-detects port `7575`
- Enable public domain if you want external access (optional)

### 2.5 Deploy
- Click **"Deploy"**
- Check logs for: `"JSON API server started on port 7575"`
- Should see: `"Connected to ledger at daml-sandbox.railway.internal:6865"`

---

## Step 3: Update Microlend Application

### 3.1 Set Environment Variable
In your **microlend** (Spring Boot) service, update:

```bash
DAML_JSON_API_URL=http://daml-json-api.railway.internal:7575
```

### 3.2 Redeploy
- Save the environment variable
- Railway will automatically redeploy your app

### 3.3 Verify
Check Spring Boot logs for:
```
DamlLedgerService initialized with JSON API URL: http://daml-json-api.railway.internal:7575
```

---

## Verification

### Test Party Allocation

1. **Login with Google OAuth** (new user)
2. **Check Logs** in Railway dashboard:

**Microlend service logs:**
```
Allocating party with hint: john_doe, displayName: John Doe
Successfully allocated DAML party: john_doe::122abc... for user: John Doe
User john@example.com logged in successfully with DAML party: john_doe::122abc...
```

3. **Verify Response** contains:
```json
{
  "token": "eyJ...",
  "fullName": "John Doe",
  "damlPartyId": "john_doe::122abc...",
  "email": "john@example.com"
}
```

---

## Troubleshooting

### Issue: "Connection refused" or "Timeout"

**Check service names:**
```bash
# In Railway dashboard, verify service names match:
daml-sandbox
daml-json-api
```

**Verify private networking:**
- All services must be in the same Railway project
- Railway automatically configures `.railway.internal` DNS

### Issue: DAML JSON API can't connect to sandbox

**Check DAML JSON API logs:**
```
Error: Could not connect to ledger
```

**Solution:**
1. Ensure `DAML_SANDBOX_HOST=daml-sandbox.railway.internal`
2. Ensure DAML Sandbox is deployed and running
3. Check DAML Sandbox logs for errors

### Issue: Spring Boot can't allocate party

**Check environment variable:**
```bash
DAML_JSON_API_URL=http://daml-json-api.railway.internal:7575
```

**Test connection manually:**
- Add temporary route in Spring Boot to test DAML connection
- Or use Railway's built-in shell to curl the endpoint

---

## Railway Service Architecture

```
┌─────────────────────────────────────────┐
│           Railway Project               │
│                                         │
│  ┌──────────────┐   Internal Network   │
│  │   PostgreSQL │◄────────────┐        │
│  └──────────────┘              │        │
│                                │        │
│  ┌──────────────┐              │        │
│  │ DAML Sandbox │◄────┐        │        │
│  │  :6865       │     │        │        │
│  └──────────────┘     │        │        │
│         ▲             │        │        │
│         │             │        │        │
│  ┌──────┴────────┐    │        │        │
│  │ DAML JSON API │    │        │        │
│  │  :7575        │◄───┼────────┤        │
│  └───────────────┘    │        │        │
│         ▲             │        │        │
│         │             │        │        │
│  ┌──────┴────────┐    │        │        │
│  │  Microlend    │────┴────────┘        │
│  │  Spring Boot  │                      │
│  │  :8080        │◄─── Public Access    │
│  └───────────────┘                      │
└─────────────────────────────────────────┘
```

---

## Important Notes

1. **Internal URLs**: Always use `.railway.internal` for service-to-service communication
2. **No Authentication**: Internal network traffic doesn't need authentication
3. **Service Names**: Must match exactly (case-sensitive)
4. **Port Configuration**: Railway auto-detects from Dockerfile EXPOSE statements
5. **Logs**: Always check deployment logs for connection confirmations

---

## Cost Estimation

Railway pricing (as of 2026):
- **Hobby Plan**: $5/month base + usage
- **Each service** consumes resources:
  - DAML Sandbox: ~100MB RAM, minimal CPU
  - DAML JSON API: ~100MB RAM, minimal CPU
  - Estimated cost: ~$5-10/month for all DAML services

---

## Next Steps

After deploying DAML services:
1. ✅ Test user registration with Google OAuth
2. ✅ Verify party allocation in logs
3. ✅ Confirm login response includes `damlPartyId`
4. 🔄 Monitor for any timeout or connection issues
5. 📊 Set up monitoring/alerts for production

**Need help?** Check Railway dashboard logs for each service to diagnose issues.
