# skills-getting-started-with-github-copilot
Exercise: Get started using GitHub Copilot

## Spring Boot Project

This is a Spring Boot project initialized with the following specifications:

### Dependencies
- **Spring Web** - For building RESTful web services
- **Spring Data JPA** - For data persistence with JPA
- **H2 Database** - In-memory database for development
- **Spring Boot DevTools** - For hot reload during development (optional)

### Configuration
- **Java Version**: 25 (configured)
- **Spring Boot Version**: 3.4.0
- **Build Tool**: Maven

### Getting Started

To build and run the project:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

**Note**: Java version 25 is configured in `pom.xml` as requested. However, Java 25 is not yet available. To build and run this project, you'll need to either:
1. Wait for Java 25 to be released, or
2. Modify the `java.version` property in `pom.xml` to a currently available version (e.g., 17, 21, or 23)
