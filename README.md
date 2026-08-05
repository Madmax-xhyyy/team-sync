# 🚀 TeamSync API

A production-ready Team Collaboration REST API built with Spring Boot 4 and MongoDB.

TeamSync provides organizations with projects, task management, member management, authentication, and role-based authorization.

---

## Table of Contents

- Features
- Tech Stack
- Overview
- Architecture
- Project Structure
- Core Features
- API Documentation
- Running Locally
- Running with Docker
- Testing
- CI/CD
- Roadmap
- License

## Project Status

🚧 Currently under active development.

Completed:

- Authentication
- Organizations
- Member Management

In Progress:

- Projects
- Tasks
- Comments

Planned:

- Notifications
- File Attachments

## 🛠 Tech Stack

### Backend

- Java 21
- Spring Boot 4
- Spring Security
- Spring Data MongoDB
- MongoDB
- JWT Authentication

### Documentation

- Swagger / OpenAPI

### Testing

- JUnit 5
- Mockito
- JaCoCo

### DevOps

- Docker
- Docker Compose
- GitHub Actions

### Build Tool

- Maven

## 📖 Overview

TeamSync API is a RESTful backend application designed for collaborative team and project management. It enables organizations to manage members, projects, tasks, and comments while enforcing secure authentication and role-based authorization.

The project follows a feature-based architecture and modern Spring Boot best practices, making it maintainable, scalable, and suitable as a production-ready foundation for team collaboration software.

## 🏗️ Architecture

The application follows a layered architecture with feature-based organization.

```
Client
   │
   ▼
Controllers
   │
   ▼
Services
   │
   ▼
Repositories
   │
   ▼
MongoDB
```

Each feature is isolated into its own package containing:

- Controller
- Service
- Repository
- Entity
- DTOs
- Mapper

## 📁 Project Structure

```
src
└── main
    ├── java
    │   └── com.teamsync.api
    │       ├── common
    │       │   ├── config
    │       │   ├── exception
    │       │   ├── response
    │       │   └── security
    │       │
    │       └── features
    │           ├── auth
    │           ├── organization
    │           ├── organizationmember
    │           ├── project
    │           ├── task
    │           ├── taskcomment
    │           └── user
    │
    └── resources
```

## 🚀 Core Features

### Authentication

- User registration
- Secure login
- JWT Access Tokens
- JWT Refresh Tokens
- Password encryption using BCrypt

### Organization Management

- Create organizations
- View organizations
- Role-based organization access

### Member Management

- Invite members
- Update member roles
- Remove members
- Permission validation

### Project Management

- Create projects
- Update project information
- Archive projects

### Task Management

- Create tasks
- Assign members
- Due dates
- Status management
- Priority levels

### Task Comments

- Create comments
- Edit comments
- Delete comments

### Security

- Spring Security
- JWT Authentication
- Role-Based Access Control (RBAC)
- Authorization Guards

## 📚 API Documentation

Once the application is running, Swagger UI is available at:

```
http://localhost:8080/swagger-ui/index.html
```

The OpenAPI specification is available at:

```
http://localhost:8080/v3/api-docs
```

## ▶️ Running Locally

### Clone the repository

```bash
git clone https://github.com/<your-username>/team-sync.git
```

### Navigate to the project

```bash
cd team-sync
```

### Configure environment variables

Create an `env.properties` file:

```properties
MONGODB_URI=mongodb://localhost:27017
MONGODB_DB=teamsync

JWT_SECRET=your-secret-key
```

### Start the application

```bash
./mvnw spring-boot:run
```

The API will start at:

```
http://localhost:8080
```

## 🐳 Running with Docker

Build the application:

```bash
docker compose build
```

Start all services:

```bash
docker compose up
```

The following containers will be started:

- TeamSync API
- MongoDB

Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

## Continuous Integration

Every push and pull request automatically runs:

- Build
- Unit Tests
- JaCoCo
- Checkstyle

## Roadmap

- Email Verification
- Password Reset
- File Uploads
- Activity Logs
- Notifications
- WebSocket Support

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
