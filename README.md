
````markdown
# 🚗 RapidRide

RapidRide is a **microservices-based ride-sharing platform** built using **Java, Spring Boot, Redis, Kafka, MySQL, Docker, and GitHub Actions**.

The project is designed to manage driver locations, find nearby drivers, match riders with drivers, and manage the complete ride lifecycle.

---

## 🚀 Project Overview

RapidRide follows a **Microservices Architecture** where each service is responsible for a specific business capability.

### Microservices

- 📍 **Location Service** – Manages real-time driver locations and nearby-driver searches using Redis Geospatial operations.
- 🔍 **Matching Service** – Finds nearby available drivers and matches them with riders.
- 🚕 **Ride Service** – Creates and manages rides and maintains the ride lifecycle.

### Supporting Components

- 🔴 **Redis** – Real-time driver location storage and geospatial search.
- 📨 **Apache Kafka** – Event-driven communication between microservices.
- 🗄️ **MySQL** – Persistent storage for riders, drivers, rides, and other business data.
- 🐳 **Docker** – Containerization.
- 🐳 **Docker Compose** – Local development and infrastructure management.
- ⚙️ **GitHub Actions** – Continuous Integration and CI/CD automation.
- 📮 **Postman** – API testing.

---

# 🏗️ System Architecture

```text
                         ┌──────────────────┐
                         │    Rider App     │
                         │     Client       │
                         └────────┬─────────┘
                                  │
                                  │ Ride Request
                                  ▼
                         ┌──────────────────┐
                         │   Ride Service   │
                         │                  │
                         │ • Create Ride    │
                         │ • Ride Lifecycle │
                         │ • Ride Status    │
                         └────────┬─────────┘
                                  │
                                  │ RideRequested Event
                                  ▼
                         ┌──────────────────┐
                         │ Matching Service │
                         │                  │
                         │ • Find Drivers   │
                         │ • Filter Drivers │
                         │ • Match Rider    │
                         └────────┬─────────┘
                                  │
                                  │ Find Nearby Drivers
                                  ▼
                         ┌──────────────────┐
                         │ Location Service │
                         │                  │
                         │ • Driver Location│
                         │ • Geo Search     │
                         │ • Location APIs  │
                         └────────┬─────────┘
                                  │
                                  ▼
                         ┌──────────────────┐
                         │      Redis       │
                         │                  │
                         │ Driver Locations │
                         │ Geospatial Data  │
                         └──────────────────┘


       ┌─────────────────────────────────────────────────┐
       │                     Kafka                         │
       │           Event-Driven Communication             │
       │                                                   │
       │ • RideRequested                                  │
       │ • DriverAssigned                                 │
       │ • RideStatusUpdated                              │
       │ • DriverLocationUpdated                          │
       └─────────────────────────────────────────────────┘


       ┌─────────────────────────────────────────────────┐
       │                     MySQL                         │
       │                Persistent Data                    │
       │                                                   │
       │ • Riders                                          │
       │ • Drivers                                         │
       │ • Rides                                           │
       │ • Ride History                                    │
       │ • Other Business Data                             │
       └─────────────────────────────────────────────────┘
````

---

# 🔄 Ride Request Flow

The basic ride flow works as follows:

```text
Rider
  │
  │ 1. Request Ride
  ▼
Ride Service
  │
  │ 2. RideRequested Event
  ▼
Kafka
  │
  ▼
Matching Service
  │
  │ 3. Find Nearby Drivers
  ▼
Location Service
  │
  │ 4. Geospatial Search
  ▼
Redis
  │
  │ 5. Nearby Drivers
  ▼
Matching Service
  │
  │ 6. Select Available Driver
  ▼
Kafka
  │
  │ 7. DriverAssigned
  ▼
Ride Service
  │
  │ 8. Update Ride Status
  ▼
MySQL
```

---

# 📍 Driver Location Flow

Driver location is handled separately from the ride lifecycle.

```text
Driver App
    │
    │ latitude + longitude
    ▼
Location Service
    │
    │ Store / Update Location
    ▼
Redis
    │
    │ Geospatial Search
    ▼
Location Service
    │
    │ Nearby Drivers
    ▼
Matching Service
```

Redis is used because driver locations are **frequently changing real-time data**.

---

# 🛠️ Technology Stack

| Technology        | Purpose                                  |
| ----------------- | ---------------------------------------- |
| Java 17           | Backend development                      |
| Spring Boot       | Microservices framework                  |
| Spring Data Redis | Redis integration                        |
| Redis             | Real-time location and geospatial search |
| Apache Kafka      | Event-driven communication               |
| MySQL             | Persistent application data              |
| Maven             | Build and dependency management          |
| Docker            | Containerization                         |
| Docker Compose    | Local infrastructure                     |
| Git               | Version control                          |
| GitHub            | Source code management                   |
| GitHub Actions    | CI/CD automation                         |
| Postman           | API testing                              |

---

# 📂 Project Structure

```text
RapidRide/
│
├── .github/
│   └── workflows/
│       └── location-service-ci.yml
│
├── location_service/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   │
│   │   └── test/
│   │
│   ├── Dockerfile
│   ├── pom.xml
│   ├── mvnw
│   └── mvnw.cmd
│
├── matching_service/
│
├── ride_service/
│
├── docker-compose.yml
│
└── README.md
```

---

# 📍 Location Service

The Location Service manages real-time driver locations.

Driver coordinates are stored in Redis using **Redis Geospatial operations**.

Redis coordinates follow:

```text
Longitude → X
Latitude  → Y
```

Example:

```text
Driver ID : D101
Latitude  : 12.9716
Longitude : 77.5946
```

Redis can then be used to search for drivers within a specified radius.

---

# 📡 Location Service APIs

## 1. Update Driver Location

### Endpoint

```http
POST /api/location/driver
```

### URL

```text
http://localhost:8080/api/location/driver
```

### Request

```json
{
  "driverId": "D101",
  "latitude": "12.9716",
  "longitude": "77.5946"
}
```

### Response

```text
driver location updated
```

---

## 2. Search Nearby Drivers

### Endpoint

```http
GET /api/location/search
```

### Example

```text
http://localhost:8080/api/location/search?latitude=12.9716&longitude=77.5946&radius=5
```

### Parameters

| Parameter | Description                 |
| --------- | --------------------------- |
| latitude  | Search location latitude    |
| longitude | Search location longitude   |
| radius    | Search radius in kilometers |

Example:

```text
/api/location/search?latitude=12.9716&longitude=77.5946&radius=5
```

This searches for drivers within **5 kilometers** of the specified location.

---

# 🔴 Redis

Redis is responsible for storing driver location data.

### Application Configuration

```properties
spring.data.redis.host=${REDIS_HOST:localhost}
spring.data.redis.port=${REDIS_PORT:6379}
```

### Local Environment

```text
REDIS_HOST=localhost
REDIS_PORT=6379
```

### Docker Environment

```text
REDIS_HOST=redis
REDIS_PORT=6379
```

Docker Compose provides the Redis hostname:

```text
redis
```

---

# 🗄️ MySQL

MySQL is used for persistent business data.

Possible entities include:

* Rider
* Driver
* Ride
* Ride Status
* Ride History
* User information

### Default Port

```text
3306
```

### Example Spring Boot Configuration

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rapidride
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

When running MySQL through Docker Compose, the hostname can be:

```text
mysql
```

instead of:

```text
localhost
```

---

# 📨 Apache Kafka

Kafka is used for **event-driven communication** between microservices.

Example events:

```text
RideRequested
DriverAssigned
RideStatusUpdated
DriverLocationUpdated
```

Example flow:

```text
Ride Service
     │
     │ RideRequested
     ▼
   Kafka
     │
     ▼
Matching Service
```

Another example:

```text
Matching Service
     │
     │ DriverAssigned
     ▼
   Kafka
     │
     ▼
Ride Service
```

Kafka helps decouple microservices and enables asynchronous communication.

---

# 🐳 Docker

RapidRide uses Docker to containerize services and infrastructure.

### Start Services

From the project root:

```bash
docker compose up --build
```

### Start in Background

```bash
docker compose up -d --build
```

### Stop Services

```bash
docker compose down
```

### Check Running Containers

```bash
docker ps
```

---

# 🔌 Service Ports

| Service          | Port |
| ---------------- | ---- |
| Location Service | 8080 |
| Redis            | 6379 |
| Kafka            | 9092 |
| Zookeeper        | 2181 |
| MySQL            | 3306 |

---

# ☕ Run Location Service Locally

Go to the Location Service:

```bash
cd location_service
```

### Build

```bash
./mvnw clean package -DskipTests
```

### Run Tests

```bash
./mvnw clean verify
```

### Run Application

```bash
./mvnw spring-boot:run
```

The application can also be started from Eclipse/STS:

```text
Run As → Spring Boot App
```

---

# 🧪 Testing with Postman

The APIs can be tested using Postman.

## Update Driver Location

```http
POST http://localhost:8080/api/location/driver
```

Request body:

```json
{
  "driverId": "D101",
  "latitude": "12.9716",
  "longitude": "77.5946"
}
```

---

## Search Nearby Drivers

```http
GET http://localhost:8080/api/location/search?latitude=12.9716&longitude=77.5946&radius=5
```

---

# 🔄 Continuous Integration

RapidRide uses **GitHub Actions** for Continuous Integration.

The current Location Service CI pipeline performs:

```text
Developer
    │
    │ git push
    ▼
GitHub
    │
    ▼
GitHub Actions
    │
    ├── Checkout Code
    │
    ├── Setup Java 17
    │
    ├── Start Redis
    │
    ├── Maven Build & Test
    │
    └── Build Docker Image
    │
    ▼
CI Success
```

### Workflow

```text
.github/workflows/location-service-ci.yml
```

The workflow runs on:

* Push to `main`
* Pull Requests targeting `main`

---

# ✅ CI Pipeline

The Location Service CI pipeline has been successfully configured and tested with GitHub Actions.

Current pipeline:

```text
Git Push
   ↓
GitHub Actions
   ↓
Checkout Code
   ↓
Java 17
   ↓
Redis Service
   ↓
Maven Verify
   ↓
Docker Build
   ↓
✅ Success
```

---

# 🔐 Configuration & Secrets

Sensitive information should not be committed to GitHub.

Examples:

```text
Database Passwords
API Keys
JWT Secrets
Cloud Credentials
Kafka Credentials
```

Use:

* Environment variables
* `.env` files locally
* GitHub Actions Secrets
* Cloud secret-management services

---

# 🚧 Current Development Status

## Location Service

* [x] Spring Boot microservice
* [x] Redis integration
* [x] Driver location storage
* [x] Redis Geospatial operations
* [x] Nearby driver search
* [x] REST APIs
* [x] Dockerfile
* [x] Docker Compose integration
* [x] Maven build
* [x] Unit testing
* [x] GitHub Actions CI
* [x] Docker image build in CI

## Matching Service

* [ ] Nearby driver matching
* [ ] Driver availability management
* [ ] Driver filtering
* [ ] Rider-driver matching
* [ ] Kafka integration

## Ride Service

* [ ] Ride creation
* [ ] Ride request management
* [ ] Driver assignment
* [ ] Ride status management
* [ ] Ride completion
* [ ] Kafka integration

---

# 🔮 Future Enhancements

* [ ] Complete Matching Service
* [ ] Complete Ride Service
* [ ] MySQL integration
* [ ] Kafka event communication
* [ ] JWT authentication
* [ ] Role-based access control
* [ ] Rider authentication
* [ ] Driver authentication
* [ ] Driver availability management
* [ ] Real-time driver tracking
* [ ] API Gateway
* [ ] Service Discovery
* [ ] Centralized configuration
* [ ] Distributed logging
* [ ] Monitoring and health checks
* [ ] Docker image publishing
* [ ] Continuous Deployment
* [ ] Cloud deployment
* [ ] Automated integration testing

---

# 🔄 Planned CI/CD Pipeline

The planned deployment pipeline is:

```text
Developer
    │
    │ git push
    ▼
GitHub Repository
    │
    ▼
GitHub Actions
    │
    ├── Build
    ├── Test
    └── Docker Build
            │
            ▼
      Docker Registry
            │
            ▼
      Deployment Server
            │
            ▼
    RapidRide Services
```

---

# 🎯 Project Goals

RapidRide is designed to demonstrate practical implementation of:

* Microservices Architecture
* RESTful APIs
* Spring Boot
* Redis Geospatial Operations
* Event-Driven Architecture
* Apache Kafka
* MySQL
* Docker
* Docker Compose
* GitHub Actions
* Continuous Integration
* Automated Testing
* Scalable Backend Architecture

---

# 👨‍💻 Author

## Vivek Gupta

**Java Backend Developer**

### Technologies

```text
Java
Spring Boot
Microservices
REST APIs
Redis
Kafka
MySQL
Docker
Docker Compose
GitHub Actions
Git
Maven
Postman
```

---

# 📄 License

This project is currently intended for learning, development, and demonstration purposes.

```
```
