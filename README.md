# OpportunityHub

 OpportunityHub is a Spring Boot platform to manage jobs, scholarships, and other opportunities.

## Features

    - CRUD for opportunities
    - Filter by type/category
    - Search by title
    - Pagination with sorting
    - Spring Security authentication
    - MockMvc tests included
    - OpenAPI/Swagger support

## Tech Stack

    - Java 21, Spring Boot 3.3.x
    - MySQL, Hibernate (JPA)
    - Spring Security
    - JUnit 5, MockMvc
    - Maven

## API Endpoints

    - POST   /api/opportunities       → create
    - GET    /api/opportunities       → list all
    - GET    /api/opportunities/{id}  → get by id
    - PUT    /api/opportunities/{id}  → update
    - DELETE /api/opportunities/{id}  → delete
    - GET    /api/opportunities/filter?type=&category=
    - GET    /api/opportunities/search?title=
    - GET    /api/opportunities/paged?page=&size=&sortBy=&sortDir=&type=&category=&title=

## Testing
    - Tests with MockMvc, CSRF, and @WithMockUser
    - Run: `mvn test`

## Run

    - Build: `mvn clean install`
    - Start: `mvn spring-boot:run`

## Notes

    - Configure MySQL in `application.properties`
    - Java 21 and Maven 3.8+ required
