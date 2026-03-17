# 🏦 Banking API – Spring Boot (RESTful Service)

## 📖 Overview
This project is a RESTful API developed using Spring Boot for managing customers and accounts in a banking system.

It supports full CRUD operations, implements clean architecture principles, and demonstrates strong backend development practices including validation, exception handling, and API documentation.

---

## 🚀 Features

### 👤 Customer Management API
- Create a new customer (Person or Company)  
- Retrieve customer by ID  
- Update customer details (name, city, postal code, province)  
- Delete a customer  
- Retrieve all customers  

---

### 💳 Account Management
- Create:
  - Savings Account  
  - Checking Account  
- A customer can have multiple accounts  
- Retrieve accounts based on customer location (e.g. city = Toronto)  

---

### 🧱 Object-Oriented Design
- Abstract `Customer` class  
- Inheritance:
  - `Person`  
  - `Company`  
- Account types:
  - `SavingsAccount`  
  - `CheckingAccount`  
- Separation of concerns (Customer & Address split)

---

### 🔐 Validation & Exception Handling
- Input validation using Jakarta Validation 
- Custom exception handling for API errors  

---

### 📄 API Documentation
- Integrated Swagger/OpenAPI for API documentation and testing  

---

### 🧪 Testing
- Unit and integration tests  
- API endpoint testing  
- Achieves high / full test coverage

---

## 🛠️ Technologies Used
- Java  
- Spring Boot  
- Spring Data JPA  
- REST APIs  
- Swagger / OpenAPI  
- Jakarta Validation  
- JUnit / Mockito  

---

## 📂 Project Structure
```
src/
├── controller/
├── service/
├── repository/
├── model/
├── exception/
└── test/
```


---

## ▶️ How to Run

1. Clone the repository:
   git clone https://github.com/your-username/your-repo.git
   
2. Open in IDE (IntelliJ / Eclipse)

3. Run the application:
   mvn spring-boot:run

4. Access Swagger UI:
   http://localhost:8080/swagger-ui/index.html

---

## 🧠 Key Concepts Demonstrated
- RESTful API design  
- Spring Boot architecture  
- Object-Oriented Programming (OOP)  
- SOLID Principles  
- Entity relationships & inheritance  
- Input validation & exception handling  
- Test-driven development practices  

---

## 📌 Notes
- All entities follow the relational model provided  
- Primary keys are auto-generated  
- API tested using Postman  

---

## 👩‍💻 Author
**Megi Belba**
