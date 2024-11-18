Loan Management API

Backend services for bank employees to manage loans.
Employees can create loans, list loans and installments 
and process payments.

Introduction

Loan management API is designed for both bank employees 
and customers. Employees can create loans for customer with 
some constraints, retrieve information about
customer loans and installments and pay loans with respective to
some rules and validations. On the other hand Customers can only
do listing and paying operations to their own loans.

Features

Loan creation: Predefined interest rates and installment counts are allowed.
Installment amounts and due dates are calculated. Customer's credit limit is validated.

Loan payment: Installments within 3 calendar months is allowed. Installments are paid in order. Multiple installments can be paid.

Security: All endpoints are authorized with username&password authentication. 
In addition CreateLoan service is auhorized with ADMIN role.

Database Persistence: H2 database is used for data storage.

Prerequisites: Java 21 , Maven 3.9 or higher

Getting Started

Clone repository: git clone https://github.com/onat-kutlu/ing-hub-case.git

Build: mvn clean install

Run: mvn spring-boot:run alternatively you can go to project directory and "docker-compose up"

H2 Console : http://localhost:8080/h2-console

Swagger : http://localhost:8080/swagger-ui/index.html

Endpoints:

Register:

Endpoint: POST /api/v0/auth/register

Request Body:

{
"username":"onatk",
"password":"112233",
"name":"onat",
"surname":"kutlu"
}

Response:

{
"serviceResult": {
"success": true,
"message": "OK",
"code": "0"
},
"customerDto": {
"id": 1,
"name": "onat",
"surname": "kutlu",
"username": "onatk",
"creditLimit": 100000,
"usedCreditLimit": 0,
"status": "CUSTOMER"
}
}

Login:

Endpoint: POST /api/v0/auth/login

Request Body:

{
"username":"onatk",
"password":"112233"
}

Response:

{
"serviceResult": {
"success": true,
"message": "OK",
"code": "0"
},
"token": "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MzE4NjQzMjQsImV4cCI6MTczMTg2NzkyNCwic3ViIjoib25hdGsifQ.7RAgMUhZT4DC7aU5Fmo-FuWtD6tDQD0wnS5KF1aZoRU",
"tokenExpirationDate": "2024-11-17 21:25:24 +03:00"
}

Create Loan:

Endpoint: POST /api/v0/loans/create

Request Body:

{
"customerId":1,
"installmentCount":6 ,
"interestRate":0.2,
"loanAmount":1350
}

Response:

{
"serviceResult": {
"success": true,
"message": "OK",
"code": "0"
},
"loan": {
"id": 1,
"loanAmount": 1350,
"totalAmount": 1620,
"customerId": 1,
"singleInstallmentAmount": 270,
"installmentCount": 6,
"interestRate": 0.2,
"createdAt": "2024-11-17 20:25:14 +03:00",
"nextInstallmentDueDate": "2024-12-01 00:00:00 +03:00",
"paid": false
}
}

List Loans By Customer Id:

Endpoint: GET /api/v0/loans/installmentsByLoanId/{id}?page={pageNumber}&size={pageSize}

Path variable {id} stands for customer id

Query parameters {pageNumber} & {pageSize} are used for pagination

Response:

{
"serviceResult": {
"success": true,
"message": "OK",
"code": "0"
},
"loans": [
{
"id": 1,
"loanAmount": 1350,
"totalAmount": 1620,
"customerId": 1,
"singleInstallmentAmount": 270,
"installmentCount": 6,
"interestRate": 0.2,
"createdAt": "2024-11-17 20:25:14 +03:00",
"nextInstallmentDueDate": "2025-03-01 00:00:00 +03:00",
"paid": false
}
]
}

List Installments By Loan Id


Endpoint: GET /api/v0/loans/installmentsByLoanId/{loanId}?page={pageNumber}&size={pageSize}

Path variable {loanId} stands for loan id to be queried

Query parameters {pageNumber} & {pageSize} are used for pagination

Response:

{
"serviceResult": {
"success": true,
"message": "OK",
"code": "0"
},
"installments": [
{
"id": 1,
"amount": 270,
"paidAmount": 270,
"dueDate": "2024-12-01 00:00:00 +03:00",
"paymentDate": "2024-11-17 20:25:21 +03:00",
"paid": true,
"order": 1
},
{
"id": 2,
"amount": 270,
"paidAmount": null,
"dueDate": "2025-01-01 00:00:00 +03:00",
"paymentDate": null,
"paid": false,
"order": 2
},
{
"id": 3,
"amount": 270,
"paidAmount": null,
"dueDate": "2025-02-01 00:00:00 +03:00",
"paymentDate": null,
"paid": false,
"order": 3
},
{
"id": 4,
"amount": 270,
"paidAmount": null,
"dueDate": "2025-03-01 00:00:00 +03:00",
"paymentDate": null,
"paid": false,
"order": 4
},
{
"id": 5,
"amount": 270,
"paidAmount": null,
"dueDate": "2025-04-01 00:00:00 +03:00",
"paymentDate": null,
"paid": false,
"order": 5
},
{
"id": 6,
"amount": 270,
"paidAmount": null,
"dueDate": "2025-05-01 00:00:00 +03:00",
"paymentDate": null,
"paid": false,
"order": 6
}
]
}

Payment:

Endpoint: POST /api/v0/loans/payment

Request Body:

{
"loanId":1,
"paymentAmount":280
}

Response:

{
"serviceResult": {
"success": true,
"message": "OK",
"code": "0"
},
"paymentDto": {
"loanId": 1,
"paidInstallmentCount": 0,
"amountSpent": 0,
"remainingInstallmentCount": 3,
"fullyPaid": false
}
}

Authorization

All endpoints are secured with username & password authentication. 
Admin user is created by default and can not be registered via register service.

Username:admin

Password:admin

After successful login, jwt token is returned and that token must be given as Bearer token to the following requests.

Final Notes:

Register service is for customer creation. Admin account is already created.
Loan creation service is restricted to ADMIN user. Customers can list and pay loans that belong to themselves.
ADMIN account can list and pay loans for any customer. Endpoints must be called with Bearer token which will be
available after login.


Contact:

Onat Kutlu

onatkutlu97@gmail.com

https://github.com/onat-kutlu
