# DD Financial Investment Management System

A full-stack financial investment management system designed for secure interaction between administrators, employees, clients, and guests. This application provides role-based access to financial services and investment management tools.

## Project Structure

The application is built using a multi-module architecture:

- **Core**: Contains domain entities, repositories, and shared business logic
- **Backend**: Spring Boot REST API that handles authentication, business logic, and database operations
- **Frontend**: React TypeScript application with role-specific dashboards and responsive UI

## Features

### User Management
- Role-based access control with four distinct roles (Admin, Employee, Client, Guest)
- Secure account creation and management
- Password encryption using BCrypt
- Profile management for all user types

### Client Account Management
- Client account request and approval workflow
- Employee-client assignment system
- Client portfolio overview

### Investment Management
- Investment tracking and visualization
- Investment status monitoring
- Portfolio performance analysis

### Administration
- User account administration
- System statistics dashboard
- Upgrade request management
- Role and permission management

## Technology Stack

### Backend
- Java 17
- Spring Boot 3.2.2
- Spring Security with JWT Authentication
- JPA/Hibernate for data persistence
- MariaDB/MySQL database

### Frontend
- React 18
- TypeScript
- TailwindCSS for styling
- Axios for API communication
- React Router for navigation
- JWT for authentication storage

## Prerequisites

- Docker and Docker Compose (for containerized deployment)
- Java 17 (for development)
- Node.js 20+ (for frontend development)
- PNPM package manager (recommended)
- MariaDB or MySQL (for database)

## Running the Application

### Using Docker Compose (Recommended)

The easiest way to run the application will be using Docker Compose once fully implemented:

```bash
# Start all services
docker-compose up -d

# To view logs
docker-compose logs -f

# To stop services
docker-compose down
```

Once running, the application is available at:
- Frontend: http://localhost:5173
- Backend API: http://localhost:8080/api

### Manual Setup

#### Database Setup

1. Install MariaDB or MySQL
2. Create a database named `ddfinv`
3. Run the following SQL script to prepare the database for the application launch:
   - `db-schema-init.sql` (Creates the database structure)

#### Backend Setup

```bash
# Navigate to the project root
cd DDFInvestment

# Build the project
./gradlew build -x test

# Run the application
./gradlew bootRun
```

#### Frontend Setup

```bash
# Navigate to the frontend folder
cd frontend

# Install dependencies
pnpm install

# Run the development server
pnpm run dev
```

## Authentication and Authorization

The system uses JWT (JSON Web Token) for authentication with the following security features:

- Token-based authentication
- Role-based access control
- Permission-based feature access
- Password hashing using BCrypt
- Token blacklisting for logout

### User Roles and Permissions

The system implements a hierarchical permission system with four roles:

1. **Admin**: Full system access including user management, statistics, and system configuration
2. **Employee**: Can manage clients, view client information, create and manage investments
3. **Client**: Can view their own account, manage personal information, and view investments
4. **Guest**: Can register, login, and request an upgrade to client status

### Default Credentials

For testing purposes, the system initializes with the following default accounts:

| Email                 | Password | Role     |
|-----------------------|----------|----------|
| admin@example.com     | password | Admin    |
| employee@example.com  | password | Employee |
| client@example.com    | password | Client   |
| guest@example.com     | password | Guest    |

## Development Setup

### Backend Development

```bash
# Navigate to the project root
cd DDFInvestment

# Run the application with auto-reload
./gradlew bootRun --args='--spring.profiles.active=dev'

# Run tests
./gradlew test
```

### Frontend Development

```bash
# Navigate to the frontend folder
cd frontend

# Install dependencies
pnpm install

# Run the development server with hot reload
pnpm run dev

# Build for production
pnpm run build

# Preview production build
pnpm run preview
```

### Configuration

#### Backend Configuration

Main configuration files:
- `application.properties`: Default configuration
- `application-dev.properties`: Development environment settings
- `application-test.properties`: Test environment settings

#### Frontend Configuration

- Update the API endpoint in `src/services/api.service.ts` if needed

## Project Organization

### Core Module

Contains shared domain models and repositories:
- Domain entities (`UserAccount`, `Client`, `Employee`, etc.)
- Base repositories
- Common exceptions
- Shared enums and constants

### Backend Module

Implements the API and business logic:
- REST Controllers for all entities
- Service layer for business logic
- Data Transfer Objects (DTOs)
- Security configuration and JWT handling
- Exception handling

### Frontend Module

Implements the user interface:
- Role-specific dashboards (Admin, Employee, Client, Guest)
- Authentication flows
- Form validation and submission
- API service integration
- Responsive UI components

## API Structure

The REST API is organized into logical controllers:

- `/api/auth`: Authentication endpoints
- `/api/admin`: Administration functionalities
- `/api/employees`: Employee management
- `/api/client`: Client account management
- `/api/users`: User account operations
- `/api/guests`: Guest user functionalities
- `/api/investments`: Investment operations

## Next Steps and Roadmap

1. ✅ ~~**Database Schema Initialization**: Database tables are automatically created during the first run~~
2. ✅ ~~**Data Seeding**: Initial test data is provided through the seed class, while the application is in active dev mode. This can be set in the application.settings of the backend.~~
3. 🔄 **Dockerization**: Finish setting up Dockerfiles for each service in the application
4. 🔄 **Code Documentation**: Complete API and method documentation for both frontend and backend
5. 🔄 **Comprehensive Testing**: Implement unit and integration tests
6. 📋 **Advanced Investment Features**: Add more sophisticated investment tracking and analysis tools
7. 📋 **Reporting**: Implement financial reports and analytics
8. 📋 **Mobile Responsiveness**: Enhance mobile UI/UX
9. 📋 **Notification System**: Add email and in-app notifications



