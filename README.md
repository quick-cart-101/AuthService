# QuickCart AuthService

## Overview

QuickCart AuthService is a Spring Boot-based authentication and authorization service. It provides APIs for user registration, login, and JWT token validation.

## Features

- User registration
- User login with JWT token generation
- JWT token validation
- Password encryption using BCrypt

## Technologies Used

- Java
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Maven

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6.0 or higher

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/MayankShivhare999/quickcart-authservice.git
    cd quickcart-authservice
    ```

2. Build the project:
    ```sh
    mvn clean install
    ```

3. Run the application:
    ```sh
    mvn spring-boot:run
    ```

## API Endpoints

### User Registration

- **URL:** `/api/auth/signup`
- **Method:** `POST`
- **Request Body:**
    ```json
    {
        "email": "user@example.com",
        "password": "password123",
        "fullName": "John Doe",
        "address": "123 Main St",
        "contactNumber": "1234567890"
    }
    ```
- **Response:**
    ```json
    {
        "id": "user-id",
        "email": "user@example.com",
        "fullName": "John Doe",
        "address": "123 Main St",
        "contactNumber": "1234567890"
    }
    ```

### User Login

- **URL:** `/api/auth/login`
- **Method:** `POST`
- **Request Body:**
    ```json
    {
        "email": "user@example.com",
        "password": "password123"
    }
    ```
- **Response:**
    ```json
    {
        "token": "jwt-token",
        "status": "ACTIVE"
    }
    ```

### User Logout

- **URL:** `/api/auth/logout`
- **Method:** `POST`
- **Headers:**
    - `Authorization`: Bearer token
- **Response:**
    ```json
    "Logged out successfully"
    ```

### Get Current User

- **URL:** `/api/auth/current-user`
- **Method:** `GET`
- **Headers:**
    - `Authorization`: Bearer token
- **Response:**
    ```json
    {
        "id": "user-id",
        "email": "user@example.com",
        "fullName": "John Doe",
        "address": "123 Main St",
        "contactNumber": "1234567890"
    }
    ```

### Refresh Token

- **URL:** `/api/auth/refresh`
- **Method:** `POST`
- **Request Body:**
    - `refreshToken`: refresh token
- **Response:**
    ```json
    {
        "token": "new-jwt-token",
        "status": "ACTIVE"
    }
    ```

### Get All Users (Admin only)

- **URL:** `/api/auth/users`
- **Method:** `GET`
- **Response:**
    ```json
    [
        {
            "id": "user-id",
            "email": "user@example.com",
            "fullName": "John Doe",
            "address": "123 Main St",
            "contactNumber": "1234567890"
        }
    ]
    ```

### Delete User (Admin only)

- **URL:** `/api/auth/users/{id}`
- **Method:** `DELETE`
- **Response:**
    ```json
    "User deleted successfully"
    ```

## Configuration

### Security Configuration

The security configuration is defined in `src/main/java/com/quickcart/authservice/config/SecurityConfig.java`. It includes settings for HTTP security, password encoding, and JWT secret key generation.

### Application Properties

Configure your application properties in `src/main/resources/application.properties`.