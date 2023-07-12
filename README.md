# Smart Meter Reading Application

## Description

This is a Smart Meter Reading Application built using Spring Boot. It provides APIs to submit and retrieve electric and gas meter readings for a specified account.

## Requirements

- Java 17 or higher
- Maven
- H2 Database Engine

## Installation

1. Clone the repository: 
```
git clone https://github.com/macsoftware/SPTT.git
```
2. Navigate to the project directory:
```
cd SPTT
```
3. Build the project:
```
mvn clean install
```
4. Run the application:
```
mvn spring-boot:run
```

## APIs

The application provides the following endpoints:

1. GET /api/smart/reads/{ACCOUNTNUMBER}: Fetches meter readings by account number.
2. POST /api/smart/reads/{ACCOUNTNUMBER}/meter-readings: Submits new meter readings for a specified account number.

For more details on API endpoints and request/response schemas, navigate to the Swagger UI after starting the application: http://localhost:8080/swagger-ui.html

## Testing

To run tests:
```
mvn clean test
```
