# Invoice Management System

Full-stack starter project using Java 17, Spring Boot, Spring Data JPA, PostgreSQL, HTML, CSS and vanilla JavaScript.

## Features
- Dashboard summary (invoice count, paid/pending/overdue counts and paid revenue)
- Customer CRUD (create, list, update, delete API)
- Invoice creation with multiple line items and percentage tax
- Invoice listing, status updates, deletion and search/filter
- Responsive HTML/CSS frontend served by Spring Boot

## Requirements
- JDK 17+
- Maven 3.9+
- PostgreSQL 14+

## Run locally
1. Create the database:
   ```sql
   CREATE DATABASE invoice_db;
   ```
2. Set database credentials in `src/main/resources/application.properties`, or environment variables:
   - `DB_URL` (default `jdbc:postgresql://localhost:5432/invoice_db`)
   - `DB_USERNAME` (default `postgres`)
   - `DB_PASSWORD` (default `postgres`)
3. From this folder run:
   ```bash
   mvn spring-boot:run
   ```
4. Open http://localhost:8080

## API endpoints
| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/customers` | List customers |
| POST | `/api/customers` | Create customer |
| PUT | `/api/customers/{id}` | Update customer |
| DELETE | `/api/customers/{id}` | Delete customer |
| GET | `/api/invoices` | List invoices |
| GET | `/api/invoices/{id}` | Invoice details |
| POST | `/api/invoices` | Create invoice |
| PATCH | `/api/invoices/{id}/status` | Change status |
| DELETE | `/api/invoices/{id}` | Delete invoice |
| GET | `/api/invoices/dashboard` | Dashboard metrics |

### Example create-invoice request
```json
{
  "customerId": 1,
  "issueDate": "2026-11-19",
  "dueDate": "2026-12-03",
  "taxRate": 18,
  "items": [
    {"description": "Website development", "quantity": 1, "unitPrice": 25000},
    {"description": "Maintenance", "quantity": 2, "unitPrice": 3000}
  ]
}
```

## Important next steps before production
This is a learning/resume project starter, not a production-hardened billing platform. Add authentication/authorization, pagination, database migrations, invoice-number sequencing, currency/tax jurisdiction rules, audit logs, tests, and PDF export before real business use. Restrict CORS and validate all request fields on the server.
