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
