# 🎵 Toy Music App

A containerized demo web application that allows users to browse songs, view their musical components, and visualize/play MIDI files using a piano roll interface.

The project is designed to demonstrate modern full-stack deployment with Docker and Kubernetes.

---

## Overview

- **Frontend**: Vue 3 + Vite application
- **Backend**: Spring Boot REST API
- **Database**: PostgreSQL
- **Storage**: Persistent Volume Claim
- **Orchestration**: Kubernetes Local Cluster

---

## Architecture

```
Browser
   ↓
Frontend (Vue + Nginx)
   ↓ REST API
Backend (Spring Boot)
   ↳ PVC (MIDI Files Storage)
   ↓
PostgreSQL
   ↳ PVC (Persistent Database Storage)

```

---

## Technologies

| Layer         | Stack                |
| ------------- | -------------------- |
| Frontend      | Vue 3, Vite, Tone.js |
| Backend       | Spring Boot, MyBatis |
| Database      | PostgreSQL           |
| Containers    | Docker               |
| Orchestration | Kubernetes           |

---

## Running Locally (Development Mode)

### 1. Backend

```bash
cd backend
./mvnw spring-boot:run
```

Backend will run on:

```
http://localhost:8081
```

### 2. Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend available at:

```
http://localhost:5173
```

## Docker Setup

### Build Images

```bash
# Backend
docker build -t toy-music-backend backend/

# Frontend
docker build -t toy-music-frontend frontend/
```

---

## Kubernetes Deployment

### Create Namespace

```bash
kubectl create namespace toy-music
kubectl config set-context --current --namespace=toy-music
```

### Apply Manifests

```bash
kubectl apply -f k8s/
```

This deploys:

- Persistent Volume Claim
- PostgreSQL Deployment + Service
- Backend Deployment + Service
- Frontend Deployment + Service

Frontend accessible via:

```
http://localhost:<nodePort>
```

---

## MIDI File Storage

Dummy MIDI files must be placed in the mounted volume:

```bash
kubectl cp localfile.mid <backend-pod>:/data/midi/file.mid -n toy-music
```

Verify:

```bash
kubectl exec -it <backend-pod> -- ls /data/midi
```

---

## Database Seed Data

The database includes:

Songs:

- Pirates → Components: C1, C2
- Queen → Components: C1, C2, C3

Components are linked via `song_component` join table.

---

## Environment Configuration

### Backend (Spring Boot)

The backend uses Spring Boot profiles to manage different runtime environments.

Profiles:

- application-local1.yaml → Local development

- application-local9.yaml → Kubernetes environment

Profile selection can be controlled via:

```
SPRING_PROFILES_ACTIVE=local1
```

### Frontend (Vue)

The frontend uses environment variables to control API endpoints.

Create a .env file in the frontend root:

```
VITE_APP_API=http://localhost:8081/api
```

For Kubernetes:

```
VITE_APP_API=http://<node-ip>:<nodePort>/api
```

### Database

Create a postgres-secret.yaml file in the k8s root:

Apply the real credentials:

```
kubectl apply -f postgres-secret.yaml
```
