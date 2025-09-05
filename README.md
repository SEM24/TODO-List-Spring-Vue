# Todo List Backend

Spring Boot REST API for a Todo List application with JWT authentication and OAuth2 integration.

## Features

- **Authentication & Authorization**
    - JWT Token-based authentication
    - OAuth2 integration (Google, GitHub)
    - Role-based access control (USER, ADMIN)
    - Session management with refresh tokens cookie

- **Todo Management**
    - CRUD operations for tasks
    - Priority levels (LOW, MEDIUM, HIGH, URGENT)
    - Due date management
    - Task completion tracking
    - Search and filtering capabilities

## Tech Stack

- **Framework**: Spring Boot 3.x
- **Security**: Spring Security 6.x
- **Database**: PostgreSQL
- **Authentication**: JWT + OAuth2
- **Build Tool**: Maven
- **Java Version**: 21

## ⚙️ Installation & Setup

### 1. Clone the repository
```bash
git clone `https://github.com/SEM24/TODO-List-Spring-Vue.git`
cd todo-backend
```

### 2. Configuration
Create `application-dev.properties` or update `application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/todoapp
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# JWT Configuration
jwt.secret=your-jwt-secret-key-here-minimum-256-bits
jwt.expiration=3600000

# OAuth2 Configuration (optional)
spring.security.oauth2.client.registration.google.client-id=your-google-client-id
spring.security.oauth2.client.registration.google.client-secret=your-google-client-secret
spring.security.oauth2.client.registration.github.client-id=your-github-client-id
spring.security.oauth2.client.registration.github.client-secret=your-github-client-secret
```

### 3. Run the application
```bash
mvn clean install
mvn spring-boot:run
```

The API will be available at `http://localhost:8082/api`

## API Documentation

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/auth/register` | Register new user |
| POST | `/v1/auth/login` | User login |
| POST | `/v1/auth/refresh` | Refresh JWT token |
| POST | `/v1/auth/logout` | User logout |
| GET | `/v1/auth/user/me` | Get current user info |

### Task Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/v1/tasks` | Get all user tasks |
| GET | `/v1/tasks/{id}` | Get specific task |
| POST | `/v1/tasks` | Create new task |
| PUT | `/v1/tasks/{id}` | Update task |
| PATCH | `/v1/tasks/{id}/toggle` | Toggle task completion |
| DELETE | `/v1/tasks/{id}` | Delete task |

### Query Parameters for GET `/v1/tasks`

- `completed` - Filter by completion status (true/false)
- `priority` - Filter by priority (LOW, MEDIUM, HIGH, URGENT)
- `search` - Search in task titles
- `overdue` - Show overdue tasks (true)
- `today` - Show today's tasks (true)

### Request/Response Examples

#### Register User
```json
POST /api/v1/auth/register
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "Password1231!"
}
```

#### Create Task
```json
POST /api/v1/tasks
{
  "title": "Complete project documentation",
  "description": "Write comprehensive README and API docs",
  "priority": "HIGH",
  "dueDate": "2024-12-31T23:59:59"
}
```

#### Task Response
```json
{
  "id": 1,
  "title": "Complete project documentation",
  "description": "Write comprehensive README and API docs",
  "completed": false,
  "priority": "HIGH",
  "dueDate": "2024-12-31T23:59:59",
  "createdAt": "2024-09-05T10:00:00",
  "updatedAt": "2024-09-05T10:00:00"
}
```
## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.