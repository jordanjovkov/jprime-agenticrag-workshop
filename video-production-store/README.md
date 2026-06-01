# video-production-store

Pre-built Spring Boot REST API serving as a store/warehouse for video editing products.
Used as an external data source by `rag-retriever` in Steps 6 and 7 of the workshop.

> **Note:** This module is pre-built and complete. It is not modified during the workshop.

---

## Responsibilities

Manages video editing card catalog, stock availability, customer data, and purchase orders.

---

## Technology Stack

- **Java 25**, **Spring Boot 4**
- **Database**: MySQL via Docker
- **API Documentation**: Swagger / SpringDoc

---

## Port & Context Path

```
http://localhost:8080/video-production-store
```

---

## Swagger UI

```
http://localhost:8080/video-production-store/swagger-ui.html
```

---

## Domain Objects

- `VideoEditingCard` — id, name, manufacturer, description, price
- `StockAvailability` — id, videoEditingCard, availability
- `Customer` — id, name, email, phone, address, notes
- `Order` — id, customer, videoEditingCard, orderDate, orderNote

---

## Endpoints

### Video Editing Cards
```
GET    /api/video-editing-cards
GET    /api/video-editing-cards/{id}
GET    /api/video-editing-cards/name?name=
GET    /api/video-editing-cards/price?minPrice=&maxPrice=
POST   /api/video-editing-cards
PUT    /api/video-editing-cards/{id}
DELETE /api/video-editing-cards/{id}
```

### Customers
```
GET    /api/customers
GET    /api/customers/{id}
GET    /api/customers/name?name=
GET    /api/customers/video-card/{videoCardId}
POST   /api/customers
PUT    /api/customers/{id}
DELETE /api/customers/{id}
```

### Stock Availabilities
```
GET    /api/stock-availabilities
GET    /api/stock-availabilities/{id}
GET    /api/stock-availabilities/video-card/{videoCardId}
GET    /api/stock-availabilities/min-quantity?minQuantity=
POST   /api/stock-availabilities
PUT    /api/stock-availabilities/{id}
DELETE /api/stock-availabilities/{id}
```

### Orders
```
GET    /api/orders
GET    /api/orders/{id}
GET    /api/orders/customer/{customerId}
POST   /api/orders
PUT    /api/orders/{id}
DELETE /api/orders/{id}
```

---

## Infrastructure (Docker)

```
docker/
└── docker-compose.yml    # MySQL
```

Started automatically by Spring Boot Docker Compose integration.
