
![Banner](banner.jpg)

### Introduction

BookStore application provides an easy and effective way to purchase books from online book store.

### Technologies 

- SpringBoot
- SpringSecurity
- SpringDataJpa
- Hibernate
- MySQL
- Maven
- Git
- Liquibase
- Swagger
- Docker
- Postman

### Functionalities

As an unauthorized user you can:
 - Register
 - Login

As an authorized user you can:
- View all books available in store
- Search for a specific book by book ID
- View all categories available in store
- Search for a specific category by category ID
- Search books by category ID
- Add books to your shopping cart
- Update/delete books in your shopping cart
- View your shopping cart
- Create new order
- View all your order's items
- Search for specific order item in your order
- View your order history

As an authorized administrator you can:
- Create a new book
- Update/Delete existing book
- Create a new category
- Update/Delete category
- Change order status


### Installation

1. Download this repository by executing this command 
        ```
        git clone https://github.com/IvanFadieiev/BookStore
        ```
2. Use Maven to build project
        ```
        mvn build install
        ```
3. Finally, use Docker to run this app in container
        ```
        docker-compose build
        ```
        ```
        docker-compose up
        ```
        
### Postman requests collection

[Link](https://github.com/IvanFadieiev/BookStore/blob/9d3d73f928782a6ee2ab07aab3c8cd1d830b7607/BookStore.postman_collection.json)
