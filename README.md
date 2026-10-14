# Kipps Billing System

## Overview

Kipps Billing System is a Java-based billing application developed using Spring Boot for a confectionery and dry-fruit shop.

The application provides REST APIs for managing products and creating bills. It handles GST calculation, inventory/stock updates, receipt generation, and PDF bill generation.

Camunda 8 has also been integrated into the project as the foundation for future workflow and business-process automation.

## Key Features

- Product management
- Create and retrieve products
- Create bills through REST APIs
- GST calculation
- Automatic stock quantity update when a bill is created
- Insufficient stock validation
- Product-not-found handling
- Receipt generation
- PDF bill generation
- Thermal printing service/API preparation
- Camunda 8 integration
- MySQL database persistence
- Input validation and exception handling

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Backend programming language |
| Spring Boot | Application framework |
| Spring Web | REST API development |
| Spring Data JPA | Database access |
| Hibernate | ORM |
| MySQL 8 | Relational database |
| Maven | Build and dependency management |
| Camunda 8 | Workflow and business-process automation |
| Git | Version control |
| GitHub | Source code hosting |
| IntelliJ IDEA | Development environment |

## REST API Documentation

The application exposes REST APIs for product management and billing operations.

### Product APIs

#### 1. Create Product

**Endpoint**

`POST /products`

**Request**

```json
{
  "name": "Kaju Premium",
  "category": "Dry Fruits",
  "price": 10000,
  "gstPercentage": 5,
  "stockQuantity": 15,
  "unit": "kg"
}
```

**Response**

```json
{
  "category": "Dry Fruits",
  "gstPercentage": 5.00,
  "id": 13,
  "name": "Kaju Premium",
  "price": 10000.00,
  "stockQuantity": 15.00,
  "unit": "kg"
}
```

---

#### 2. Get All Products

**Endpoint**

`GET /products`

**Response**

```json
[
  {
    "category": "Chocolates",
    "gstPercentage": 5.00,
    "id": 7,
    "name": "Test Chocolate",
    "price": 120.00,
    "stockQuantity": 18.00,
    "unit": "PCS"
  },
  {
    "category": "Dry Fruits",
    "gstPercentage": 5.00,
    "id": 13,
    "name": "Kaju Premium",
    "price": 10000.00,
    "stockQuantity": 15.00,
    "unit": "kg"
  }
]
```

---

#### 3. Get Product by ID

**Endpoint**

`GET /products/{id}`

**Example**

`GET /products/13`

**Response**

```json
{
  "category": "Dry Fruits",
  "gstPercentage": 5.00,
  "id": 13,
  "name": "Kaju Premium",
  "price": 10000.00,
  "stockQuantity": 15.00,
  "unit": "kg"
}
```

---

#### 4. Update Product

**Endpoint**

`PUT /products/{id}`

**Example**

`PUT /products/13`

**Request**

```json
{
  "name": "Kaju Premium",
  "category": "Dry Fruits",
  "price": 10000,
  "gstPercentage": 5,
  "stockQuantity": 15,
  "unit": "kg"
}
```

---

### Bill APIs

#### 5. Create Bill

**Endpoint**

`POST /bills`

**Request**

```json
{
  "items": [
    {
      "product": {
        "id": 13
      },
      "quantity": 2
    }
  ]
}
```

**Response**

```json
{
  "billDate": "2026-10-14T10:52:00.9790135",
  "grandTotal": 21000.00,
  "gstAmount": 1000.00,
  "id": 19,
  "items": [
    {
      "gstPercentage": 5.00,
      "id": 15,
      "price": 10000.00,
      "product": {
        "category": "Dry Fruits",
        "gstPercentage": 5.00,
        "id": 13,
        "name": "Kaju Premium",
        "price": 10000.00,
        "stockQuantity": 11.00,
        "unit": "kg"
      },
      "quantity": 2,
      "total": 20000.00
    }
  ],
  "subtotal": 20000.00
}
```

**What happens when the bill is created:**

1. Product is retrieved using its ID.
2. Product price and GST are used for calculation.
3. Bill subtotal is calculated.
4. GST amount is calculated.
5. Grand total is calculated.
6. Purchased quantity is deducted from product stock.
7. Bill and bill items are persisted in MySQL.

---

## 8. Get Bill by ID

**Endpoint**

`GET /bills/{id}`

**Example**

`GET /bills/19`

**Response**

```json
{
  "billDate": "2026-10-14T10:52:00.9790135",
  "grandTotal": 21000.00,
  "gstAmount": 1000.00,
  "id": 19,
  "items": [
    {
      "gstPercentage": 5.00,
      "id": 15,
      "price": 10000.00,
      "product": {
        "category": "Dry Fruits",
        "gstPercentage": 5.00,
        "id": 13,
        "name": "Kaju Premium",
        "price": 10000.00,
        "stockQuantity": 11.00,
        "unit": "kg"
      },
      "quantity": 2,
      "total": 20000.00
    }
  ],
  "subtotal": 20000.00
}

9. Generate Receipt
Endpoint
GET /bills/{id}/receipt
Example
GET /bills/19/receipt
Description
Generates a text-based receipt for the specified bill.
Example Output

H G SWEETS (KIPPS)
Philibhit Bypass Road
9454442143

----------------------------------------
Bill No: 19
Date: 2026-10-14T10:52:00.9790135
----------------------------------------
Item         Qty       Rate      Amount
----------------------------------------
Kaju Premi    2.00   10000.00    20000.00
----------------------------------------
Subtotal:                      20000.00
GST:                            1000.00
----------------------------------------
GRAND TOTAL:                   21000.00
----------------------------------------
          Thanks! Visit Again!

10. Generate Bill PDF
Endpoint
GET /bills/{id}/pdf
Example
GET /bills/19/pdf
Description
Generates a PDF version of the bill.
The API returns the generated document with the content type:
application/pdf
The PDF can be opened directly in a browser or PDF viewer.

11. Print Bill
Endpoint
POST /bills/{id}/print
Example
POST /bills/19/print
Description
The endpoint retrieves the bill, generates the receipt, and passes the receipt to the thermal printing service.
The thermal printing service/API has been prepared in the application architecture.
A physical thermal printer is not currently connected to the project.

Application Flow
The current billing flow works approximately as follows:
Client / Postman
       |
       v
Spring Boot REST Controller
       |
       v
BillService
       |
       +------> ProductRepository
       |             |
       |             v
       |           MySQL
       |
       +------> Validate Product
       |
       +------> Validate Stock
       |
       +------> Calculate Subtotal
       |
       +------> Calculate GST
       |
       +------> Calculate Grand Total
       |
       +------> Deduct Stock
       |
       v
BillRepository
       |
       v
MySQL Database
       |
       v
Bill Response

Database
The application uses MySQL 8 for persistent data storage.
Product
Stores product information including:
- Product ID
- Product name
- Category
- Price
- GST percentage
- Stock quantity
- Unit
Bill
Stores:
- Bill ID
- Bill date
- Subtotal
- GST amount
- Grand total
BillItem
Stores:
- Bill item ID
- Product
- Quantity
- Price
- GST percentage
- Total
Transaction Management
Bill creation is handled using Spring's transaction management.
The bill creation process includes:
- Product lookup
- Stock validation
- Stock deduction
- GST calculation
- Bill calculation
- Bill item creation
- Database persistence
These operations are handled within the billing service transaction.
Camunda 8 Integration
Camunda 8 has been integrated into the project as the foundation for future workflow and business-process automation.
The project currently contains:
- Camunda connection testing
- Camunda workflow service
The intended future workflow can be extended to automate processes such as:
Bill Created
     |
     v
Camunda Workflow
     |
     +----> Inventory Processing
     |
     +----> Receipt Generation
     |
     +----> PDF Generation
     |
     +----> Thermal Printing
     |
     v
Workflow Completed

Camunda is currently being used as the foundation for future workflow orchestration rather than being required for the core bill calculation.
Project Structure
billing-system/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── kipps/
│   │   │           └── billingsystem/
│   │   │               │
│   │   │               ├── BillingSystemApplication.java
│   │   │               ├── CamundaConnectionTest.java
│   │   │               │
│   │   │               ├── cotroller/
│   │   │               │   ├── BillController.java
│   │   │               │   └── ProductController.java
│   │   │               │
│   │   │               ├── entity/
│   │   │               │   ├── Product.java
│   │   │               │   └── bill/
│   │   │               │       ├── Bill.java
│   │   │               │       ├── BillItem.java
│   │   │               │       └── Product.java
│   │   │               │
│   │   │               ├── exception/
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   └── ProductNotFoundException.java
│   │   │               │
│   │   │               ├── repository/
│   │   │               │   ├── BillItemRepository.java
│   │   │               │   ├── BillRepository.java
│   │   │               │   └── ProductRepository.java
│   │   │               │
│   │   │               └── service/
│   │   │                   ├── BillNotFoundException.java
│   │   │                   ├── BillPdfService.java
│   │   │                   ├── BillService.java
│   │   │                   ├── CamundaWorkflowService.java
│   │   │                   ├── ProductService.java
│   │   │                   ├── ReceiptService.java
│   │   │                   └── ThermalPrintService.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/
│
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
├── .gitattributes
└── README.md

Running the Application
Prerequisites
Make sure the following are installed:
- Java 17
- MySQL 8
- Maven
- IntelliJ IDEA
Start the Application
From the project directory:
Windows PowerShell
.\mvnw.cmd spring-boot:run

The application runs on:
http://localhost:8080

Testing the APIs
The REST APIs can be tested using Postman or any REST API client.
Example:
POST http://localhost:8080/products

Create a bill:
POST http://localhost:8080/bills

Retrieve a bill:
GET http://localhost:8080/bills/19

Generate receipt:
GET http://localhost:8080/bills/19/receipt

Generate PDF:
GET http://localhost:8080/bills/19/pdf

Current Project Status
The current backend supports:
- Product creation
- Product retrieval
- Product updates
- Bill creation
- GST calculation
- Automatic stock deduction
- Insufficient stock validation
- Product-not-found handling
- Bill retrieval
- Receipt generation
- PDF bill generation
- Thermal printing service/API preparation
- MySQL persistence
- Transaction management
- Exception handling
- Camunda 8 integration foundation
A physical thermal printer is not currently connected.
Future Enhancements
Possible future improvements include:
- Frontend billing interface
- Product search
- Customer management
- Payment method tracking
- Cash / UPI / Card support
- Low-stock alerts
- Daily sales reports
- Monthly sales reports
- Authentication and authorization
- Employee/user management
- Actual thermal printer integration
- Automated invoice printing
- Expanded Camunda workflow automation
- Docker containerization
- Cloud deployment