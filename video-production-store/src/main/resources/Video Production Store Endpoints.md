# Video Production Store — REST API Endpoints

**Base URL:** `http://localhost:8082/video-production-store`

---

## Video Editing Cards

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/video-editing-cards` | Get all video editing cards |
| GET | `/api/video-editing-cards/{id}` | Get a video editing card by ID |
| GET | `/api/video-editing-cards/name?name=` | Get a video editing card by name |
| GET | `/api/video-editing-cards/price?minPrice=&maxPrice=` | Get video editing cards by price range |
| POST | `/api/video-editing-cards` | Create a new video editing card |
| PUT | `/api/video-editing-cards/{id}` | Update a video editing card by ID |
| DELETE | `/api/video-editing-cards/{id}` | Delete a video editing card by ID |

---

## Customers

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/customers` | Get all customers |
| GET | `/api/customers/{id}` | Get a customer by ID |
| GET | `/api/customers/name?name=` | Get customers by name |
| GET | `/api/customers/video-card/{videoCardId}` | Get customers who ordered a specific video editing card |
| POST | `/api/customers` | Create a new customer |
| PUT | `/api/customers/{id}` | Update a customer by ID |
| DELETE | `/api/customers/{id}` | Delete a customer by ID |

---

## Stock Availabilities

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/stock-availabilities` | Get all stock availabilities |
| GET | `/api/stock-availabilities/{id}` | Get stock availability by ID |
| GET | `/api/stock-availabilities/video-card/{videoCardId}` | Get stock availability for a specific video editing card |
| GET | `/api/stock-availabilities/min-quantity?minQuantity=` | Get stock availabilities with at least the specified quantity |
| POST | `/api/stock-availabilities` | Create a new stock availability entry |
| PUT | `/api/stock-availabilities/{id}` | Update a stock availability entry by ID |
| DELETE | `/api/stock-availabilities/{id}` | Delete a stock availability entry by ID |

---

## Orders

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/orders` | Get all orders |
| GET | `/api/orders/{id}` | Get an order by ID |
| GET | `/api/orders/customer/{customerId}` | Get all orders for a specific customer |
| POST | `/api/orders` | Create a new order |
| PUT | `/api/orders/{id}` | Update an order by ID |
| DELETE | `/api/orders/{id}` | Delete an order by ID |

---

## Documentation

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/swagger-ui.html` | Swagger UI |
| GET | `/api-docs` | OpenAPI JSON |
