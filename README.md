# E-Commerce Backend

A backend-only e-commerce system built with Spring Boot, made mainly to actually understand how real backend systems work — order management, inventory handling, payment gateway integration, and JWT auth — instead of just another CRUD tutorial project.  

There's no frontend here on purpose. This is meant to be tested via Postman / any REST client, and the focus is entirely on the API and the logic behind it.  

### Tech Stack  
- Java 17 + Spring Boot 4.1  
- Spring Data JPA (Hibernate) for persistence  
- PostgreSQL as the database  
- Spring Security + JWT for authentication  
- PayPal Checkout SDK (sandbox) for payments  
- Lombok to cut down on boilerplate  
- Maven  

## What it actually does  
- Users can register and log in, and get a JWT token back  
- Products can be created and browsed (public, no login needed)  
- Logged-in users can place orders with multiple items  
- Stock is validated and decremented server-side — never trusted from the frontend  
- The whole order-placement process is wrapped in a database transaction, so if anything fails midway (like insufficient stock on one item), nothing gets half-saved — it all rolls back cleanly  
- Orders go through a real payment flow with PayPal: an order is created on PayPal's side, the user approves it, and the backend independently verifies the payment succeeded before marking the order as paid (the frontend redirect is never trusted on its own)  
- Protected routes require a valid JWT; public routes (like browsing products) don't  

## Database Schema  
> User (1) ──── (many) Order  
> Order (1) ──── (many) OrderItem ──── (1) Product

**OrderItem** exists as its own table instead of Order linking straight to Product, mainly because an order needs to remember how many of something was bought and at what price — and that price needs to stay frozen even if the product's price changes later. Copying the price at the time of purchase into **OrderItem.priceAtPurchase** is what makes that possible.  

## API Overview  
### Auth  
| Method | Endpoint | Auth required | Description |
|----------|-----------|-----------|-----------|
| POST   | /api/auth/register | 	No  | Create a new user   |
| POST   | 	/api/auth/login   | 	No  |	Log in, returns a JWT token  |  

### Products
| Method | Endpoint | Auth required | Description |
|---|---|---|---|
| POST | `/api/products` | No | Add a new product |
| GET | `/api/products` | No | List all products |
| GET | `/api/products/{id}` | No | Get one product |
 
### Orders
| Method | Endpoint | Auth required | Description |
|---|---|---|---|
| POST | `/api/orders` | **Yes** | Place an order (checks stock, calculates total, saves everything transactionally) |
 
### Payments
| Method | Endpoint | Auth required | Description |
|---|---|---|---|
| POST | `/api/payments/create/{orderId}` | **Yes** | Creates a PayPal order, returns an approval link |
| POST | `/api/payments/capture/{orderId}` | **Yes** | Confirms the payment with PayPal and marks the order paid |  

### Sample request — placing an order
 
```json
POST /api/orders
Authorization: Bearer <your-jwt-token>
 
{
  "userId": 1,
  "items": [
    { "productId": 1, "quantity": 2 },
    { "productId": 2, "quantity": 1 }
  ]
}
```  

## Running it locally
 
1. Create a Postgres database:
```sql
   CREATE DATABASE ecommerce_db;
```
2. Update `src/main/resources/application.properties` with your own DB credentials and a JWT secret.
3. Add your own PayPal sandbox `client id` / `secret` if you want to test the payment flow.
4. Run it:
```bash
   mvn spring-boot:run
```
5. Test with Postman — register a user, log in, grab the token, and go from there.
