# Internship-Assignment

Spring Boot Basic with PostgreSQL
=================================

A **Spring Boot REST API** with **role-based authentication**, **user management**, and **exception handling**, integrated with **PostgreSQL**.

* * * * *

**Table of Contents**
---------------------

-   [Features](#features)

-   [Technologies Used](#technologies-used)

-   [Getting Started](#getting-started)

    -   [Prerequisites](#prerequisites)

    -   [Running Locally](#running-locally)


-   [API Endpoints](#api-endpoints)

-   [Exception Handling](#exception-handling)

-   [Security](#security)

-   [Project Structure](#project-structure)


* * * * *

**Features**
------------

-   REST endpoints:

    -   Public endpoint `/public` (accessible by anyone)

    -   User endpoint `/user` (accessible by `USER` and `ADMIN` roles)

    -   Admin endpoint `/admin` (accessible only by `ADMIN`)

    -   Create user endpoint `/users` (secured, accessible by `ADMIN`)

-   Role-based access control using **Spring Security**

-   Custom `UserDetailsService` for authentication

-   Password hashing with **BCrypt**

-   Validation for username and password

-   Custom exceptions:

    -   `UserAlreadyExistsException`

    -   `RoleNotFoundException`

-   Global exception handler with JSON error responses

-   Database initialization with default users (`intern` and `admin`)


* * * * *

**Technologies Used**
---------------------

-   Java 17

-   Spring Boot 3.x

-   Spring Security

-   Spring Data JPA / Hibernate

-   PostgreSQL

-   Maven

-   Lombok

* * * * *

**Getting Started**
-------------------

### **Prerequisites**

-   JDK 17+

-   Maven 3.8+


* * * * *

### **Running Locally**

1.  Clone the repository:

bash


`git clone <https://github.com/nay-eem-01/Internship-Assignment.git>

    cd springbootbasicwithpostgresql` 

1.  Configure PostgreSQL database:

-   Create a database `intern_db`

-   Update `src/main/resources/application.properties`:

properties



`spring.datasource.url=jdbc:postgresql://localhost:5432/intern_db`    

`spring.datasource.username=postgres`

`spring.datasource.password= use your password here` 

`spring.jpa.hibernate.ddl-auto=update`

1.  Build the project:

bash

`mvn clean package`

1.  Run the Spring Boot app:

bash

`mvn spring-boot:run`

* * * * *

**API Endpoints**
-----------------

| Method | Endpoint | Role Access | Description |
| --- | --- | --- | --- |
| GET | /public | Anyone | Public endpoint |
| GET | /user | USER, ADMIN | User-only endpoint |
| GET | /admin | ADMIN | Admin-only endpoint |
| POST | /users | ADMIN | Create a new user |

**Example JSON to create a user:**

json


`{
  "userName": "newuser",
  "password": "password123",
  "role": "ROLE_USER"
}`

* * * * *

**Exception Handling**
----------------------

| Exception | Status Code | Description |
| --- | --- | --- |
| `UserAlreadyExistsException` | 409 CONFLICT | Username already exists |
| `RoleNotFoundException` | 400 BAD REQUEST | Invalid role (only `ROLE_USER` or `ROLE_ADMIN`) |

**Example JSON response:**

json


`{
  "timestamp": "2025-08-14T15:00:00",
  "status": 409,
  "error": "User Already Exists",
  "message": "User with username 'newuser' already exists"
}`

* * * * *

**Security**
------------

-   **Authentication**: Basic Auth

-   **Roles**:

    -   `ROLE_USER` → can access `/user`

    -   `ROLE_ADMIN` → can access `/user`, `/admin`, `/users`

-   **Password hashing**: BCrypt

-   **CustomUserDetailsService** to load users from DB
