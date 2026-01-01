# Okanefy – Backend (API)

This is the **backend API** for the Okanefy application — a REST server built with **Java**, **Spring Boot**, **Spring Security**, **JPA**, and designed to be containerized with **Docker**.

The API handles authentication, data persistence, business logic, and provides endpoints for the frontend to manage users, transactions, and more.

---

## 🚀 Features

- Secure REST API with JWT authentication
- User registration and login
- CRUD endpoints for financial entities (transactions, categories)
- Configured for local development and Docker
- Structured for easy extension and integration

---

## 🧰 Technologies

**Core**
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Flyway (migrations)
- JUnit + Mockito (tests)

**Database**
- MySQL (or MariaDB)

**Infrastructure**
- Docker
- Docker Compose

---

## 📦 Running the Backend

### Prerequisites
Before running the backend, make sure you have:

- Docker
- Docker Compose

### Environment Variables

Before running, configure your environment variables. You can copy from the example:

```bash
cp application.properties.example application.properties
```
