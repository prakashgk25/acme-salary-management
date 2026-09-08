# ACME Employee Salary Management

End-to-end salary management for 10,000 employees using Java 21, Spring Boot, Spring Data JPA, PostgreSQL/H2, and Angular.

## Requirements document

**Goal:** Replace Excel-based salary administration with a secure, searchable web application that lets an HR Manager maintain employee salary records and answer questions about organizational pay across countries and currencies.

**In scope**
- Employee salary CRUD with employee, country, department, currency, annual salary and effective date.
- Multi-currency normalization into a configurable base currency using a seeded FX-rate table.
- Server-side pagination, sorting, filtering and free-text search for 10,000+ records.
- Analytics: headcount, average/base-currency salary, total/base-currency payroll, and department/country summaries.
- Optimistic concurrency using JPA `@Version` so stale edits are rejected rather than silently overwriting newer changes.
- Deterministic seed data: 10,000 employees plus FX rates.
- Validation, consistent REST errors, unit tests, and a responsive Angular UI.

**Deliberately out of scope**
- Payroll processing, tax calculation, payslips and statutory compliance: these are separate domains with country-specific complexity.
- Authentication/SSO/RBAC implementation: the MVP assumes an authenticated HR Manager boundary; production deployment should integrate the organization's IdP.
- Historical salary versioning/audit trail: useful but intentionally deferred to keep the first release focused on current salary administration; the data model can be extended later.
- Employee self-service and compensation approvals: not required for the stated HR-manager workflow.
- Live FX provider integration: seeded rates make tests and demos deterministic; a provider can be added behind an FX service later.

**Success criteria:** HR can locate and edit a salary in seconds, reliably report payroll in one base currency, and analyze 10,000 records without loading the whole dataset into the browser.

## Run

### Backend
Requires Java 21 and Maven 3.9+.

```bash
cd backend
mvn spring-boot:run
```

PostgreSQL (Neon) is used as the production database and seeds 10,000 employees + FX rates. API: `https://acme-salary-management-vltn.onrender.com/api`.

For PostgreSQL, set `SPRING_PROFILES_ACTIVE=postgres` and the datasource environment variables in `application-postgres.yml`.

### Frontend
Requires Node.js 20+.

```bash
cd frontend
npm install
npm start
```

Open `http://localhost:4200`.

### Tests
```bash
cd backend
mvn test
```

## API
- `GET /api/employees?page=0&size=25&search=alice&country=India&department=Engineering&currency=INR&sort=lastName,asc`
- `GET /api/employees/{id}`
- `POST /api/employees`
- `PUT /api/employees/{id}` with `version` from the latest GET
- `DELETE /api/employees/{id}` with `If-Match-Version` header
- `GET /api/analytics/summary`
- `GET /api/analytics/by-department`
- `GET /api/analytics/by-country`
- `GET /api/fx-rates`

The backend normalizes salary to the base currency (`USD` by default) using the effective FX rate for the employee's currency.

## HR Login

The UI now starts with an HR login screen.

Demo credentials:
- User ID: `HRadmin`
- Password: `HRadmin`

The frontend keeps the authenticated state in browser `sessionStorage` until Logout or the browser session ends. The backend exposes `POST /api/auth/login` for credential validation.

> Note: This is assignment/demo authentication. The credentials are intentionally configured in the backend source and the employee APIs are not protected by Spring Security/JWT. For production, replace this with Spring Security, password hashing, sessions or JWT, and role-based authorization.

## Employee Form Selections

Department, country, and currency are dropdowns in the Add/Edit employee form.

Available departments:
Engineering, Developement, Finance, HR, Sales, Operations, Product

Available countries:
India, USA, UK, Germany, Singapore, Australia

Available currencies:
USD, EUR, GBP, INR, SGD, AUD

