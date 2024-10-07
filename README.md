<div align="center"><h1><img src=src/main/resources/images/logo.png width="300" align="center" alt="Book store"/></h1></div>
<div align="center"><h1> Book Store </h1></div>

### ___

### Welcome to the Book Store project. 
 This application is a platform for buying books in the Internet.
 It can manage this collection of books, categories, and user orders.


### Technologies Used

> **Spring Boot**   
>
> **Spring Web** 
> 
> **Spring Security** 
> 
> **Spring Data JPA** 
> 
> **Spring Boot Testing** 
> 
> **Hibernate** 
> 
> **MySQL** 
> 
> **Liquibase** 
> 
> **MapStruct** 
> 
> **Docker** 
> 
> **Swagger UI** 
> 


### The specific functionalities of project controllers

 Controllers                Endpoint                                      Description                                     

- AuthenticationController /auth       -   handles user registration and authorization                     
- BookController           /books      -   manages book operations, such as search, creation, updating and deleting       
- CategoryController       /categories -   manages categories, allows to create, update, retrieve and delete          
- OrderController          /orders  -      handles order management, creating, updating, retrieving order history and deleting 
- ShoppingCartController    /cart    -     manages shopping cart state, allows to add, update, retrieve and delete cart items  

### In this app we will have the following models (entities):

User: Contains information about the registered user including their authentication details and personal information.
Role: Represents the role of a user in the system, for example, admin or user.
Book: Represents a book available in the store.
Category: Represents a category that a book can belong to.
ShoppingCart: Represents a user's shopping cart.
CartItem: Represents an item in a user's shopping cart.
Order: Represents an order placed by a user.
OrderItem: Represents an item in a user's order.

### People are acted:

User: Someone who looks at books, puts them in a shopping cart, and buys them.
Admin: Someone who checked the books on the shelf and watches what gets bought.

### PROJECT FEATURES
Language: Java 22. Build System: Maven.
The application has Controller - Service - Repository architecture.
To ensure security, Spring Boot Security and Data Transfer Objects (DTOs) are used.
All the endpoints were documented using Swagger.
Books can be filtered by author and title. For this purpose it is used Criteria Query.
Liquibase was used to create tables and add some data to them in the database.
Only one user has role ADMIN (email:alice@com.net, password:1234). All new users will have role USER by default.
CustomGlobalExceptionHandler is used to handle exceptions. It provides more descriptive exception messages.
Tests were written using Testcontainers for repository-level, Mockito for service-level and MockMvc for controller-level.
For application deployment Docker was used.

### TO START LOCALLY THIS APPLICATION YOU MUST:
*) Clone this project to you local IDE.
*) Open this project in your IDE.
*) Create in the root directory .env file.
*) Run Docker Desktop.
*) To run the application via docker, run the commands "docker compose up --build" in the IDE terminal.
*) If you want to try application you can use this [link](http://localhost:8087/swagger-ui/index.html#/)

You can watch video to see how this application works.
https://www.loom.com/share/df7edc897a414d1d9d16c9661e2b6a4a