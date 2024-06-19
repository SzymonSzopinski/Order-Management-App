# Order Management System

## Overview

The Order Management System is a comprehensive CRUD application designed to manage products, orders, and customers. The application exposes RESTful APIs to interact with the backend, which is implemented using Java, Spring Boot, JPA/Hibernate, and MySQL. The frontend is developed using HTML, CSS, and JavaScript. The frontend was created in an "old-school" manner as a way to learn the basics of frontend development, which is a good starting point before diving into modern frameworks like React.

## Features

- **CRUD Operations**: Manage products, orders, and customers with create, read, update, and delete functionalities.
- **RESTful APIs**: Expose endpoints for frontend integration.
- **Validation**: Ensures data integrity and proper input validation.
- **Global Exception Handling**: Robust error management for a seamless user experience.
- **Comprehensive Testing**: Unit and integration tests using JUnit and Mockito to ensure application reliability.

## Technologies Used

- Java
- Spring Boot
- JPA/Hibernate
- MySQL
- JUnit
- Mockito
- HTML
- CSS
- JavaScript
- RESTful APIs

## Getting Started

### Prerequisites

- Java 11 or higher
- MySQL
- Maven

### Installation

1. **Clone the repository**:

   ```bash
   git clone https://github.com/SzymonSzopinski/Order-Management-App.git
   cd order-management-system
   ```

2. **Configure the database**:

   - Create a database named `order_management` in MySQL.
   - Update the `application.properties` file in the `src/main/resources` directory with your MySQL database username and password.

3. **Build the project**:

   ```bash
   mvn clean install
   ```

4. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

### Running Tests

To run the unit and integration tests, use the following command:

```bash
mvn test

```

API Endpoints

Products
GET /products: Retrieve all products
GET /products/{id}: Retrieve a product by ID
POST /products: Create a new product
PUT /products/{id}: Update an existing product
DELETE /products/{id}: Delete a product by ID

Orders
GET /orders: Retrieve all orders
GET /orders/order/{id}: Retrieve an order by ID
POST /orders: Create a new order
PUT /orders/update/{id}: Update an existing order
DELETE /orders/delete/{id}: Delete an order by ID

Customers
GET /customers: Retrieve all customers
GET /customers/{id}: Retrieve a customer by ID
POST /customers: Create a new customer
PUT /customers/{id}: Update an existing customer
DELETE /customers/{id}: Delete a customer by ID

Demonstration
To see a video demonstration of the application in action, please visit: https://www.youtube.com/watch?v=CDCWKie-Ppc

Learning Frontend Development
As a backend developer, I created the frontend in an "old-school" manner using HTML, CSS, and JavaScript.
This approach allowed me to learn the basics of frontend development, which is a good starting point before moving on to modern frameworks like React.
Having a solid understanding of these fundamentals will help me build better and more intuitive user interfaces in future projects.

Contributing
Contributions are welcome! Please open an issue or submit a pull request for any improvements or suggestions.

Contact
For any questions or support, please contact: szymon.szopinski.kontakt@gmail.com
