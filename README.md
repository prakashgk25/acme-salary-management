ACME Employee Salary Management

A full-stack employee salary management application designed to help HR teams manage salary information for 10,000+ employees across multiple countries and currencies.

The application replaces spreadsheet-based salary administration with a searchable, scalable web application that provides salary management, multi-currency normalization, and analytics.

🚀 Live Application
Frontend / UI

https://acme-salary-management-ui.onrender.com/login

Backend API

https://acme-salary-management-vltn.onrender.com

Database

PostgreSQL hosted on Neon

Note about the backend:
The Spring Boot backend is hosted on Render and may be automatically shut down after a period of inactivity.

If the application is not immediately available, open the backend URL first:

https://acme-salary-management-vltn.onrender.com

Wait approximately 1–2 minutes for the backend service to start, then open the frontend login page.

🔐 Demo Login

Use the following credentials to access the application:

User ID:  HRadmin
Password: HRadmin

After login, the HR Manager can access the employee management workspace and analytics.

✨ Key Features
Employee Management
Add new employees
Edit existing employee salary information
View employee details
Delete employee records
Manage employee country, department, currency and salary
Effective date support
Search & Filtering
Server-side pagination
Free-text employee search
Filter by country
Filter by department
Filter by currency
Server-side sorting

The application is designed to handle 10,000+ employee records without loading the complete dataset into the browser.

💰 Multi-Currency Salary Management

Employees can have salaries in different currencies:

USD
EUR
GBP
INR
SGD
AUD

Salary values are normalized to a configurable base currency (USD by default) using seeded FX rates.

📊 Analytics

The application provides:

Total headcount
Average salary
Total payroll
Base-currency salary calculations
Department-wise salary summaries
Country-wise salary summaries
🔒 Concurrent Updates

Employee salary records use optimistic locking with JPA @Version.

This prevents one HR user from accidentally overwriting changes made by another user using an outdated record.

🛠️ Technology Stack
Component	Technology
Frontend	Angular
Backend	Java 21
Framework	Spring Boot
Data Access	Spring Data JPA / Hibernate
Production Database	PostgreSQL
Database Hosting	Neon
Local Database	H2
Deployment	Render
Seed Data	10,000 Employees + FX Rates
🏗️ Application Architecture
Angular UI
    │
    │ REST API
    ▼
Spring Boot Backend
    │
    ├── Employee Management
    ├── Authentication
    ├── Analytics
    ├── Validation
    └── FX Rate / Salary Normalization
    │
    ▼
PostgreSQL (Neon)
💻 Run Locally
1. Start the Backend

Requirements:

Java 21
Maven 3.9+
cd backend
mvn spring-boot:run

Backend:

http://localhost:8080
2. Start the Angular Frontend

Requirements:

Node.js 20+
cd frontend
npm install
npm start

Frontend:

http://localhost:4200
3. Run Tests
cd backend
mvn test
🔗 API Endpoints
Authentication
POST /api/auth/login
Employees
GET    /api/employees
GET    /api/employees/{id}
POST   /api/employees
PUT    /api/employees/{id}
DELETE /api/employees/{id}

Example:

GET /api/employees?page=0&size=25&search=alice&country=India&department=Engineering&currency=INR&sort=lastName,asc
Analytics
GET /api/analytics/summary
GET /api/analytics/by-department
GET /api/analytics/by-country
🌱 Seed Data

The application includes deterministic seed data for:

10,000 employees
Multiple countries
Multiple departments
Multiple currencies
FX conversion rates

This makes the application suitable for demonstrating pagination, filtering, searching and analytics against a realistic dataset.

📹 Application Demo

The attached video demonstrates the main features of the application, including:

HR Login
Employee dashboard
Employee search and filtering
Add employee
Edit employee
Delete employee
Salary and currency management
Analytics
Local application
Deployed application on Render
🔒 Production Considerations

The current authentication implementation is intended for assignment/demo purposes.

For a production environment, authentication should be replaced with:

Spring Security
Secure password hashing
JWT or server-side sessions
Role-based access control
Organization SSO / Identity Provider integration

Historical salary tracking, audit trails, payroll processing, tax calculation and live FX provider integration are intentionally outside the scope of this MVP.

📌 Deployment

The application is deployed using:

Angular UI
     ↓
Render
     ↓
Spring Boot REST API
     ↓
Render
     ↓
PostgreSQL
     ↓
Neon

The frontend communicates with the deployed Spring Boot REST API, while employee and salary data is persisted in the Neon PostgreSQL database.

First-load delay: Because the Render backend can sleep when inactive, the first request after inactivity may take approximately 1–2 minutes while the service starts. Subsequent requests should respond normally.
