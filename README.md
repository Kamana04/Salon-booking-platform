# 💇 Salon Appointment Booking System - Full Stack Microservices Project

A real-world **Salon Appointment Booking Platform** built using **Microservices Architecture** with secure authentication, online payments, real-time notifications, and scalable backend services.

This project demonstrates enterprise-level software engineering concepts including:

- Microservices Architecture
- API Gateway
- Service Discovery
- JWT Authentication
- Keycloak Security
- Event-Driven Communication
- Real-Time Notifications
- Payment Integration
- Dockerized Deployment

---

# 🚀 Tech Stack

## 🏗️ Microservices Architecture
Develop scalable and independently deployable services.

---

# ⚙️ Backend Technologies

- **Spring Boot** — Robust backend framework
- **Spring Cloud Gateway** — API Gateway routing
- **Eureka Server** — Service discovery
- **Keycloak** — Authentication & Authorization
- **JWT** — Token-based security
- **MySQL** — Relational database
- **RabbitMQ** — Event-driven communication
- **WebSocket** — Real-time notifications
- **Feign Client** — Inter-service communication

---

# 🎨 Frontend Technologies

- **React.js** — Dynamic UI development
- **TailwindCSS** — Modern responsive styling
- **Redux Toolkit** — State management
- **Material UI (MUI)** — UI components
- **Formik** — Form handling & validation

---

# 💳 Payment Gateway

- Razorpay Integration

---

# 🐳 DevOps & Deployment

- Docker
- Docker Compose

---

# 🛠️ Tools Required

| Tool | Purpose |
|------|----------|
| IntelliJ IDEA | Backend Development |
| VS Code | Frontend Development |
| MySQL | Database |
| Docker | Containerization |
| Postman | API Testing |

---

# ✨ Features

## 👥 Customer Features

- User Registration & Login
- Browse Salons
- Search Salons By City
- Book Appointments
- Online Payments
- Booking History
- Real-Time Notifications

---

## 💼 Salon Owner Features

- Create & Manage Salon
- Manage Categories
- Add/Update Services
- View Bookings
- Booking Reports
- Update Booking Status

---

# 🧩 Microservices

| Service | Responsibility |
|---------|----------------|
| User Service | Authentication & User Management |
| Salon Service | Salon Management |
| Category Service | Salon Categories |
| Service Offering Service | Salon Services |
| Booking Service | Appointment Booking |
| Payment Service | Payment Processing |
| API Gateway | Centralized Routing |
| Eureka Server | Service Discovery |

---

# 🔐 Authentication & Authorization

- Keycloak Integration
- JWT Token Authentication
- Role-Based Access Control
- Secure API Gateway

---

# 🔔 Real-Time Communication

- WebSocket Notifications
- Live Booking Updates
- Payment Status Updates

---

# 📡 Communication Pattern

## Synchronous Communication
- OpenFeign Client

## Asynchronous Communication
- RabbitMQ Event Messaging

---



# 🏗️ System Architecture

```text
Frontend (React)
       |
API Gateway
       |
-------------------------------------------------
|       |         |         |        |          |
User   Salon   Category   Booking   Payment   Service
Service Service Service   Service   Service   Offering
       |
    MySQL
```

---

# 🐳 Running Project Locally

# 1️⃣ Clone Repository

```bash
git clone <repository-url>
```

---

# 2️⃣ Backend Setup

Run all microservices:

- Eureka Server
- API Gateway
- User Service
- Salon Service
- Category Service
- Service Offering Service
- Booking Service
- Payment Service

---

# 3️⃣ Start Keycloak

Install Docker and run Keycloak container.

```bash
docker-compose up
```

---

# 4️⃣ Frontend Setup

```bash
npm install
npm start
```

Frontend runs on:

```text
http://localhost:3000
```

---

# 🔒 Security Features

- JWT Authentication
- Role-Based Authorization
- Keycloak Integration
- Secure API Gateway
- Protected REST APIs

---

# 📊 Key Learning Outcomes

This project helps you understand:

- Microservices Architecture
- API Gateway Pattern
- JWT Authentication
- OAuth2 & Keycloak
- Inter-Service Communication
- Event-Driven Systems
- Payment Gateway Integration
- Docker Deployment
- Scalable Backend Design
- React Frontend Architecture

---

# 🚀 Future Enhancements

- Kubernetes Deployment
- CI/CD Pipeline
- Redis Caching
- SMS & Email Notifications
- Elasticsearch Integration
- AI-based Salon Recommendations
- Reviews & Ratings Analytics
