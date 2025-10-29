# Employee Management REST API

A Spring Boot REST API for managing employee records with CRUD operations, built with the latest Spring Boot framework and H2 in-memory database.

## Features

- **Create** new employee records
- **Read** all employees or get a specific employee by ID
- **Update** existing employee information
- **Delete** employee records
- In-memory H2 database for data persistence
- Input validation with meaningful error messages
- Global exception handling
- RESTful API design following best practices

## Technologies

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **Maven** for dependency management
- **JUnit 5** for testing

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Build the Project

```bash
mvn clean package
```

### Run the Application

```bash
mvn spring-boot:run
```

Or run the JAR file directly:

```bash
java -jar target/employee-api-1.0.0.jar
```

The application will start on `http://localhost:8080`

### Run Tests

```bash
mvn test
```

## API Endpoints

### Base URL
```
http://localhost:8080/api/employees
```

### 1. Get All Employees
**GET** `/api/employees`

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "department": "IT",
    "salary": 75000.0
  }
]
```

### 2. Get Employee by ID
**GET** `/api/employees/{id}`

**Response:** `200 OK`
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "department": "IT",
  "salary": 75000.0
}
```

**Error Response:** `404 Not Found`
```json
{
  "status": 404,
  "message": "Employee not found with id: 999",
  "timestamp": "2025-10-29T06:01:56.851612296"
}
```

### 3. Create Employee
**POST** `/api/employees`

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "department": "IT",
  "salary": 75000.0
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "department": "IT",
  "salary": 75000.0
}
```

**Validation Error Response:** `400 Bad Request`
```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2025-10-29T06:01:56.868250902",
  "errors": {
    "lastName": "Last name is required",
    "firstName": "First name is required",
    "department": "Department is required",
    "salary": "Salary is required",
    "email": "Email should be valid"
  }
}
```

**Duplicate Email Error:** `409 Conflict`
```json
{
  "status": 409,
  "message": "Employee already exists with email: john.doe@example.com",
  "timestamp": "2025-10-29T06:01:56.883686454"
}
```

### 4. Update Employee
**PUT** `/api/employees/{id}`

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.updated@example.com",
  "department": "Finance",
  "salary": 85000.0
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.updated@example.com",
  "department": "Finance",
  "salary": 85000.0
}
```

### 5. Delete Employee
**DELETE** `/api/employees/{id}`

**Response:** `204 No Content`

## Validation Rules

- **firstName**: Required, cannot be blank
- **lastName**: Required, cannot be blank
- **email**: Required, must be a valid email format, must be unique
- **department**: Required, cannot be blank
- **salary**: Required, cannot be null

## H2 Console

The H2 database console is available at: `http://localhost:8080/h2-console`

**Connection details:**
- JDBC URL: `jdbc:h2:mem:employeedb`
- Username: `sa`
- Password: (leave blank)

## Testing the API

### Using cURL

#### Create an employee:
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","email":"john.doe@example.com","department":"IT","salary":75000}'
```

#### Get all employees:
```bash
curl http://localhost:8080/api/employees
```

#### Get employee by ID:
```bash
curl http://localhost:8080/api/employees/1
```

#### Update an employee:
```bash
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","email":"john.updated@example.com","department":"Finance","salary":85000}'
```

#### Delete an employee:
```bash
curl -X DELETE http://localhost:8080/api/employees/1
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/employeeapi/
│   │   ├── EmployeeApiApplication.java      # Main application class
│   │   ├── controller/
│   │   │   └── EmployeeController.java       # REST controller
│   │   ├── exception/
│   │   │   ├── DuplicateResourceException.java
│   │   │   ├── ResourceNotFoundException.java
│   │   │   └── GlobalExceptionHandler.java   # Global error handling
│   │   ├── model/
│   │   │   └── Employee.java                 # Entity class
│   │   ├── repository/
│   │   │   └── EmployeeRepository.java       # JPA repository
│   │   └── service/
│   │       └── EmployeeService.java          # Business logic
│   └── resources/
│       └── application.properties            # Application configuration
└── test/
    └── java/com/example/employeeapi/
        └── EmployeeControllerTest.java       # Integration tests
```

## License

This project is created as an exercise for getting started with GitHub Copilot.
