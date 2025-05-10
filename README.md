# Pizzeria Demo Project

This is a demo Spring Boot application simulating a pizzeria order pipeline with Kafka for asynchronous stage transitions. You can place orders via REST, and the application will process them through four stages (received, in oven, ready, out for delivery) using Kafka topics and configurable worker pools.

## Table of Contents

* [Prerequisites](#prerequisites)
* [Project Structure](#project-structure)
* [Setup](#setup)

  * [Database](#database)
  * [Kafka & Zookeeper](#kafka--zookeeper)
  * [Application Properties](#application-properties)
* [Running the Application](#running-the-application)
* [API Endpoints](#api-endpoints)
* [Configuration](#configuration)
* [Contributing](#contributing)
* [License](#license)

---

## Prerequisites

* Java 24 (OpenJDK 24)
* Maven 3.8+
* MySQL 8+ running locally
* Apache Kafka 3.9.0 + ZooKeeper (or KRaft if configured)
* Optional: Postman or `curl` for API testing

---

## Project Structure

```
src/main/java/org/example/pizzeria
├── config               # Kafka and executor pool configuration
├── controller           # REST controllers
├── assembler            # Entity-to-DTO mappers & response builders
├── dto                  # Request/response records
├── model                # JPA entities and enums
├── repository           # Spring Data JPA repositories
└── service              # Business logic, Kafka listeners
```

---

## Setup

### Database

1. Create a MySQL database:

   ```sql
   CREATE DATABASE pizzeria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
2. Configure credentials in `src/main/resources/application.properties`.

### Kafka & Zookeeper

Unzip Kafka (3.9.0) to `C:\kafka\kafka_2.13-3.9.0`.

1. **Start ZooKeeper** (in one console):

   ```bat
   cd /d C:\kafka\kafka_2.13-3.9.0
   bin\windows\zookeeper-server-start.bat config\zookeeper.properties
   ```

2. **Start Kafka Broker** (in another console):

   ```bat
   cd /d C:\kafka\kafka_2.13-3.9.0
   bin\windows\kafka-server-start.bat config\server.properties
   ```

3. **Create Topics** (after broker is up):

   ```bat
   cd /d C:\kafka\kafka_2.13-3.9.0
   bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic orders-received
   bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic orders-in-oven
   bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic orders-ready
   bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic orders-out-for-delivery
   ```

### Application Properties

Configure `src/main/resources/application.properties`:

```properties
spring.application.name=pizzeria
spring.datasource.url=jdbc:mysql://localhost:3306
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false

spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*
spring.kafka.consumer.group-id=pizzeria-group
```

---

## Running the Application

1. Build and run:

   ```bash
   mvn clean spring-boot:run
   ```
2. Verify on startup you see Tomcat on port 8080.

---

## API Endpoints

### Pizzas (Menu)

* **GET** `/pizzas` — List all menu items
* **GET** `/pizzas/{id}` — Get a pizza by ID
* **POST** `/pizzas` — Add a new pizza

  ```json
  {
    "name": "Classic",
    "description": "Tomato, mozzarella, basil",
    "price": 8.0,
    "toppings": []
  }
  ```

### Orders

* **POST** `/orders` — Place a new order

  ```json
  { "pizzaId": 1 }
  ```

  Returns `201 Created` with `Location: /orders/{id}` and body:

  ```json
  { "id":1, "pizzaId":1, "status":"RECEIVED", "createdAt":"..." }
  ```

* **GET** `/orders/{id}` — Get current status and timestamps

---

## Configuration

Delays and worker pools for each stage are in `StageConfig.java`. You can tweak:

```java
exec.setCorePoolSize(2);  // number of workers at that station
sleepSeconds(7);           // delay per order at that stage
```

---

## Contributing

1. Fork the repo
2. Create a feature branch (`git checkout -b feature/new`)
3. Commit your changes (`git commit -m 'Add feature'`)
4. Push to the branch (`git push origin feature/new`)
5. Open a Pull Request

---
