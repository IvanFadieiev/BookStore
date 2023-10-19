
## Introduction

BookStore application provides an easy and effective way to purchase books from online book store.

[Here](https://www.loom.com/share/86db70c4dc494c15bbf9b766d754756c?sid=4a3e1de1-3acc-45a0-bbca-cf0f5caf2075) is a short video presentation of application.

## Technologies 

- **SpringBoot**: This project is build on Spring Boot framework.
- **SpringSecurity**: Authorization and authentification functionality.
- **SpringDataJpa**: Database access and management functionality.
- **Hibernate**: Object-Relation mapping functionality.
- **MySQL**: Database management system for this project.
- **Maven**: Dependencies management functionality.
- **Git**: Version control tool.
- **Liquibase**: Database version control tool.
- **Swagger**: API documentation provider tool.
- **Postman**: Endpoints testing tool.
- **Docker**: Containerization  for easy deployment.

## Functionalities

### Authentication Controller: Register and login users.
As an unauthorized user you can:
1. Register - POST /auth/register
2. Login - POST /auth/login

### Book Controller: Manage books data.
As an authorized user you can:
1. View all books available in store - GET /books
2. Search for a specific book by book ID - GET /books/{id}

As an authorized administrator you can:
1. Create a new book - POST /books
2. Update existing book by ID - PUT /books/{id}
3. Delete existing book by ID - DELETE /books/{id}

### Category Controller: Manage book categories data.
As an authorized user you can:
1. View all categories available in store - GET /categories
2. Search for a specific category by category ID - GET /categories/{id}
3. Search all books by category ID - /categories/{id}/books

As an authorized administrator you can:
1. View all categories available in store - GET /categories
2. Search for a specific category by category ID - GET /categories/{id}
3. Search all books by category ID - GET /categories/{id}/books
4. Create a new category - POST /categories
5. Update existing category by ID - PUT /categories/{id}
6. Delete existing category by ID - DELETE /categories/{id}

### Shopping Cart Controller: Manage user's shopping cart information.
As an authorized user you can:
1. Add books to your shopping cart - POST /cart
2. View your shopping cart - GET /cart
3. Update books in your shopping cart - PUT /cart/cart-item/{id}
4. Delete books in your shopping cart - DELETE /cart/cart-item/{id}

### Order Controller: Manage user's orders information.
As an authorized user you can:
1. Create new order - POST /orders
2. View your order history - GET /orders
3. View all your order's items - GET /orders/{orderID}/items
4. Search for specific order item in your order - GET /orders/{orderId}/items/{itemId}

As an authorized administrator you can:
1. Update user's order status - PATCH /orders/{id}

## Installation

1. To run this app you need : <br />
        a. Java 17. You can download it from official [website](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) <br />
        b. MySQL. You can download it from official [website](https://www.mysql.com/downloads/) <br />
        c. Docker. You can download it from official [website](https://www.docker.com/get-started/) <br />
        d. Maven. You can download if from official [website](https://maven.apache.org/download.cgi) <br />

2. Download this repository by executing this command 
        ```
   git clone https://github.com/IvanFadieiev/BookStore
        ```
3. Use Maven to build project
        ```
   mvn build install
        ```
4. Finally, use Docker to run this app in container
        ```
   docker-compose build
        ```
        ```
   docker-compose up
        ```

Other way to run this app without Docker :
1. Download this repository by executing this command
        ```
   git clone https://github.com/IvanFadieiev/BookStore
        ```
2. Use Maven to build project :
        ```
   mvn build install
        ```
3. Set up connection for your local database in application.properties file
4. Run this command after :
        ```
   mvn spring-boot:run
        ```
        
## Postman requests collection

[Link](https://github.com/IvanFadieiev/BookStore/blob/9d3d73f928782a6ee2ab07aab3c8cd1d830b7607/BookStore.postman_collection.json)

## Contributing

If you have any ideas for contributing to this project, I am ready for conversation on [GitHub](https://github.com/IvanFadieiev) or [Email](ivanfadieiev@gmail.com) 
