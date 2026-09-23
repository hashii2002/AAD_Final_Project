# 🚗 Vehicle Rental & Fleet Management System — Backend

A modern **Vehicle Rental & Fleet Management System** backend developed using **Spring Boot** and **MySQL**. The system provides RESTful APIs for managing vehicles, customers, drivers, rentals, payments, invoices, maintenance, inspections, reviews, users, and fleet operations.

The backend is designed with a layered architecture, secure JWT-based authentication, role-based authorization, DTO-based API communication, validation, centralized exception handling, scheduled rental reminders, email functionality, and a customer-facing AI vehicle information assistant.

---

## 📌 Project Overview

The Vehicle Rental & Fleet Management System is designed to digitize and streamline the operations of a vehicle rental company.

The backend manages the complete rental lifecycle, from user and vehicle management to rental processing, payments, invoicing, vehicle inspections, maintenance, and customer reviews.

It provides secure REST APIs that can be consumed by the **DriveGo frontend application** or other API clients such as Postman.

### Main Objectives

* Manage users and system roles
* Manage customers and drivers
* Manage vehicle categories, brands, models, and vehicles
* Manage vehicle documents
* Configure rental rates
* Process vehicle rentals
* Assign drivers to rentals
* Manage payments and invoices
* Record vehicle inspections
* Manage vehicle maintenance
* Collect customer reviews
* Provide JWT-based authentication and authorization
* Send rental-related email notifications
* Provide automated rental-expiry reminders
* Provide a customer-facing AI assistant for vehicle information

---

# ✨ Key Features

## 🔐 Authentication & Authorization

The system uses **JWT-based authentication** with role-based access control.

### Supported Roles

* `ADMIN`
* `FLEET_MANAGER`
* `CUSTOMER`
* `DRIVER`

Security features include:

* JWT authentication
* BCrypt password encryption
* Stateless authentication
* Role-based endpoint authorization
* Custom `UserDetailsService`
* JWT authentication filter
* CORS configuration
* Protected REST endpoints

---

## 👤 User & Role Management

Administrators can manage system users and their assigned roles.

### Capabilities

* User registration
* User authentication
* User management
* Role management
* Password encryption
* User status management
* Role-based authorization

---

## 👥 Customer Management

The customer module manages customer-specific information and rental-related data.

### Customer Information

* NIC
* Address
* Driving license number
* User account association

The system also supports customer-specific operations through authenticated APIs.

---

## 🚘 Fleet Management

The system provides structured vehicle management through:

**Category → Brand → Model → Vehicle**

### Vehicle Categories

Examples can include:

* Sedan
* SUV
* Van
* Luxury
* Economy

### Vehicle Brands

Vehicle brands are maintained independently and can contain multiple vehicle models.

### Vehicle Models

Each model contains information such as:

* Model name
* Fuel type
* Seating capacity
* Transmission type
* Brand relationship

### Vehicles

Vehicle records contain information such as:

* Vehicle registration number
* Color
* Manufacturing year
* Vehicle status
* Vehicle model
* Vehicle category

This structure allows the fleet to be managed efficiently while maintaining proper entity relationships.

---

## 📄 Vehicle Document Management

The system supports vehicle-related document management.

Documents include information such as:

* Document type
* Document number
* Issue date
* Expiry date
* Document status
* Associated vehicle

This allows important vehicle documentation to be tracked within the system.

---

## 💰 Rental Rate Management

Rental rates can be configured according to vehicle categories.

The rental rate module supports:

* Daily rental rate
* Monthly rental rate
* Free kilometers per day
* Extra kilometer charges

This provides a flexible pricing structure for vehicle rentals.

---

# 📝 Rental Management

The rental module manages the main vehicle rental lifecycle.

### Rental Information

* Start date
* End date
* Rental duration
* Pickup mileage
* Return mileage
* Deposit amount
* Rental status
* Total rental amount
* Customer
* Vehicle
* Rental rate

The system can therefore maintain the complete relationship between:

**Customer → Rental → Vehicle → Rental Rate**

---

## 👨‍✈️ Driver Management

The driver module manages drivers and their driving credentials.

Driver information includes:

* Driving license number
* Driver status
* Associated user account

The system also supports assigning drivers to rentals through the `RentalDriver` entity.

This allows a rental to be associated with the relevant driver information.

---

# 💳 Payment Management

The payment module manages rental-related financial transactions.

Payment information includes:

* Payment reference
* Amount
* Discount
* Balance
* Payment date
* Payment method
* Payment status
* Related rental

This provides a structured way to track payments made against rentals.

---

# 🧾 Invoice Management

The invoice module provides rental invoice management.

Invoices are associated with rental transactions and can be used to maintain formal billing records for completed rental operations.

---

# 🔍 Vehicle Inspection

Vehicle inspections can be recorded as part of the rental and fleet management process.

The inspection module supports maintaining vehicle condition information during fleet operations.

This helps the company keep track of vehicle condition and identify issues that may require attention.

---

# 🔧 Maintenance Management

The maintenance module manages vehicle maintenance records.

Maintenance records support information such as:

* Vehicle
* Service date
* Service details
* Maintenance status
* Next service date

The backend also contains validation and duplicate-prevention logic for maintenance records.

For example, duplicate active maintenance records for the same vehicle and service date are prevented, while cancelled records are handled separately.

---

# ⭐ Customer Reviews

Customers can submit reviews related to their rental experience.

The review module provides functionality for storing customer feedback associated with the rental/customer context.

This can help the rental company collect and manage customer feedback through the application.

---

# 🤖 Customer Vehicle Information Assistant

The backend includes an **AI-powered customer-facing vehicle information assistant**.

The AI functionality is provided as a separate customer feature and is intended to help customers obtain vehicle-related information through conversational interaction.

> **Note:** The AI assistant is a customer-facing feature and is **not part of the Admin Dashboard**.

---

# 📧 Email Service

The backend contains email functionality for sending system-related notifications.

Email services can be integrated with rental-related events and customer communication workflows.

---

# ⏰ Automated Rental Reminder

The backend includes a scheduled rental reminder component.

The `RentalReminderScheduler` is responsible for checking relevant rental records and triggering reminder functionality for rentals approaching their expiry/end date.

This reduces the need for manual monitoring of upcoming rental deadlines.

---

# 🏗️ Architecture

The project follows a layered architecture:

```text
┌─────────────────────────────┐
│        REST Controller      │
├─────────────────────────────┤
│        Service Layer        │
├─────────────────────────────┤
│      ServiceImpl Layer      │
├─────────────────────────────┤
│       Repository Layer      │
├─────────────────────────────┤
│       JPA Entity Layer      │
├─────────────────────────────┤
│          MySQL              │
└─────────────────────────────┘
```

Additional layers/components include:

```text
DTO
Security
Exception Handling
Validation
Constants
Enums
Scheduler
Email Service
AI Service
```

### Request Flow

```text
Client
   ↓
Controller
   ↓
Service
   ↓
ServiceImpl
   ↓
Repository
   ↓
Entity
   ↓
MySQL
```

DTOs are used for API data transfer rather than exposing entity objects directly where appropriate.

---

# 🗂️ Main Domain Entities

The current backend contains the following main entities:

| Entity              | Purpose                          |
| ------------------- | -------------------------------- |
| `Role`              | System role management           |
| `User`              | Authentication and user accounts |
| `Customer`          | Customer information             |
| `Driver`            | Driver information               |
| `VehicleCategory`   | Vehicle category management      |
| `VehicleBrand`      | Vehicle brand management         |
| `VehicleModel`      | Vehicle model management         |
| `Vehicle`           | Fleet vehicle management         |
| `VehicleDocument`   | Vehicle document tracking        |
| `RentalRate`        | Rental pricing                   |
| `Rental`            | Vehicle rental transactions      |
| `RentalDriver`      | Rental-driver association        |
| `Payment`           | Rental payment records           |
| `Invoice`           | Rental billing records           |
| `VehicleInspection` | Vehicle inspection records       |
| `MaintenanceRecord` | Vehicle maintenance history      |
| `Review`            | Customer feedback                |

---

# 🔗 Core Relationships

The main domain relationships can be represented as:

```text
Role
 │
 └── User
      ├── Customer
      └── Driver


VehicleCategory
      │
      ├── RentalRate
      │
      └── Vehicle

VehicleBrand
      │
      └── VehicleModel
             │
             └── Vehicle
                    │
                    ├── VehicleDocument
                    ├── VehicleInspection
                    └── MaintenanceRecord


Customer
   │
   └── Rental
        ├── Vehicle
        ├── RentalRate
        ├── RentalDriver
        │      └── Driver
        ├── Payment
        ├── Invoice
        └── Review
```

---

# 🛡️ Exception Handling

The backend provides centralized exception handling through a global exception handler.

The application handles common API errors such as:

* Resource not found
* Duplicate records
* Validation failures
* Invalid requests
* Authentication/authorization errors
* Business logic exceptions

This allows API responses to remain consistent and easier for frontend applications to process.

---

# ✅ Validation & Data Integrity

The application applies validation rules at the API and service levels.

Examples include:

* Required field validation
* Unique vehicle registration numbers
* Unique customer NIC information
* Unique driving license information
* Rental validation
* Maintenance date validation
* Duplicate maintenance prevention
* Status-based business rules

Database relationships are maintained using **JPA/Hibernate** mappings.

---

# 🧰 Technology Stack

| Technology         | Usage                          |
| ------------------ | ------------------------------ |
| Java 21            | Programming language           |
| Spring Boot 4.1.0  | Backend framework              |
| Spring Web MVC     | REST API development           |
| Spring Data JPA    | Database persistence           |
| Hibernate          | ORM                            |
| MySQL              | Relational database            |
| Spring Security    | Authentication & authorization |
| JWT                | Stateless authentication       |
| BCrypt             | Password encryption            |
| Jakarta Validation | Request validation             |
| Spring Mail        | Email functionality            |
| Jackson            | JSON processing                |
| Maven              | Dependency management          |
| Lombok             | Boilerplate reduction          |
| Apache Tomcat      | Application server             |

---

# 📦 Project Dependencies

The project uses Spring Boot dependencies including:

* Spring Web MVC
* Spring Data JPA
* Spring Security
* Spring Validation
* Spring Mail
* Spring Actuator
* Jackson
* Lombok
* MySQL Driver
* JWT-related security libraries

---

# ⚙️ Getting Started

## 1. Clone the Repository

```bash
git clone <your-backend-repository-url>
```

Navigate into the project:

```bash
cd aad_final_project
```

---

## 2. Configure MySQL

Create a MySQL database for the project.

Example:

```sql
CREATE DATABASE vehicle_rental_db;
```

Update the database configuration in:

```text
src/main/resources/application.properties
```

Configure:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/vehicle_rental_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

> Never commit real database passwords, JWT secrets, email passwords, or API keys to GitHub.

---

# 🔑 Environment Configuration

For production or public repositories, sensitive values should be supplied through environment variables rather than hard-coded configuration.

Typical sensitive configuration includes:

```text
DATABASE_USERNAME
DATABASE_PASSWORD
JWT_SECRET
MAIL_USERNAME
MAIL_PASSWORD
AI_API_KEY
```

---

# ▶️ Running the Application

Make sure Java 21 and Maven are installed.

Run using Maven:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

Or run the main Spring Boot application class directly from IntelliJ IDEA.

The backend will normally be available at:

```text
http://localhost:8080
```

---

# 🔐 Authentication Flow

The authentication process follows this general flow:

```text
User
  ↓
Login API
  ↓
Username + Password
  ↓
Spring Security
  ↓
BCrypt Password Verification
  ↓
JWT Token Generated
  ↓
Client Stores Access Token
  ↓
Token Sent With Protected Requests
  ↓
JwtAuthenticationFilter
  ↓
Role Authorization
  ↓
Controller
```

Protected requests use the JWT access token in the authorization header:

```http
Authorization: Bearer <access-token>
```

---

# 🌐 Frontend Integration

This backend provides REST APIs for the **DriveGo** frontend application.

The frontend communicates with the backend through endpoints under:

```text
/v1/*
```

Examples include:

```text
/v1/user/login
/v1/user/save
/v1/user/all
/v1/customer/*
/v1/driver/*
/v1/vehicle/*
/v1/vehicleBrand/*
/v1/vehicleModel/*
/v1/vehicleCategory/*
/v1/rental/*
/v1/payment/*
/v1/invoice/*
/v1/review/*
/v1/maintenance/*
/v1/vehicleDocument/*
/v1/vehicleInspection/*
/v1/rentalDriver/*
/v1/ai/*
```

The APIs can also be tested independently using tools such as **Postman**.

---

# 📊 API Testing

The REST APIs can be tested using:

* Postman
* Browser developer tools
* Frontend application
* Other REST API clients

A typical testing workflow is:

```text
Register / Create User
        ↓
Login
        ↓
Receive JWT
        ↓
Add JWT to Authorization Header
        ↓
Access Protected APIs
```

---

# 🔒 Security Design

The backend applies role-based endpoint protection.

A simplified access structure is:

| Resource            | Access                     |
| ------------------- | -------------------------- |
| Login               | Public                     |
| Registration        | Public                     |
| User Management     | ADMIN                      |
| Role Management     | ADMIN                      |
| Customer Management | ADMIN / FLEET_MANAGER      |
| Driver Management   | Role-dependent             |
| Vehicle Management  | ADMIN / FLEET_MANAGER      |
| Rental Operations   | Authenticated / role-based |
| Payment Operations  | Authenticated / role-based |
| Customer Features   | CUSTOMER                   |
| Driver Features     | DRIVER                     |

Exact authorization is enforced through the application's Spring Security configuration.

---

# 📁 Project Structure

A simplified project structure is:

```text
src/
└── main/
    ├── java/
    │   └── lk/
    │       └── ijse/
    │           └── aad_final_project/
    │               ├── controller/
    │               ├── dto/
    │               ├── entity/
    │               ├── repository/
    │               ├── service/
    │               ├── service/impl/
    │               ├── security/
    │               ├── exception/
    │               ├── enums/
    │               ├── constants/
    │               └── scheduler/
    │
    └── resources/
        └── application.properties
```

---

# 🧪 Development & Testing

The backend was developed and tested using REST API clients during development.

Recommended testing areas include:

* Authentication
* Authorization
* CRUD operations
* Validation
* Duplicate handling
* Rental workflows
* Payment processing
* Invoice generation
* Vehicle maintenance
* Vehicle inspections
* Customer reviews
* AI chat requests
* Scheduled reminder functionality

---

# 🚀 Future Enhancements

Potential future improvements include:

* Advanced fleet analytics
* More detailed reporting
* Advanced notification management
* Online payment gateway integration
* Improved rental availability checking
* Automated document expiry notifications
* Production deployment configuration
* API documentation with OpenAPI/Swagger
* Expanded automated testing
* Advanced dashboard analytics

These are considered future enhancements and are not represented as currently implemented functionality unless already available in the project.

---

# 📌 Project Status

**Status:** Development / Academic Project

The backend currently provides the core REST API and business logic required for the Vehicle Rental & Fleet Management System.

It is designed to work with the **DriveGo frontend application** and can also be independently tested through REST API clients.

---

# 👩‍💻 Author

**Hashini Emalsha**

Higher Diploma in Software Engineering
Institute of Java & Software Engineering (IJSE)

---

# 📄 License

This project was developed as an academic software engineering project.

If you plan to reuse, modify, or distribute the project, please contact the author for permission.
