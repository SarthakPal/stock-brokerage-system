Problem Statement
Design a RESTful API for managing a stock trading platform using Java Spring Boot with H2 as the database.
Requirements:
Core Functionality:

● CRUD Operations:
The API should support Create, Read, Update, and Delete operations for managing users, stocks, and orders.

● Real-time Price Fetching:
Integrate with a third-party API to fetch real-time stock prices from an external exchange.

● Order Management:
Users should be able to place buy/sell orders based on the fetched prices. The system should ensure that the price is locked at the time of placing the order.

● Order History:
Maintain a history of all orders placed by users, including the prices at which the trades were executed.

Attributes:
Users:

● Decide the attributes
Stocks:
● Decide the attributes
Orders:
● Decide the attributes
Endpoints:
Orders:
● POST /orders: Place a new buy/sell order.
● GET /orders: Retrieve all order records with pagination and sorting options. ● GET /orders/{id}: Retrieve a specific order record by ID.
● PUT /orders/{id}: Update an existing order record (only if the order is pending).
● DELETE /orders/{id}: Cancel an order by its ID (only if the order is pending).
● PATCH /orders/{id}/status: Update the status of the order (e.g., mark it as completed or cancelled).
Users:
● POST /users: Register a new user.
● GET /users: Retrieve all user records with pagination and sorting options. ● GET /users/{id}: Retrieve a specific user record by ID.
● PUT /users/{id}: Update an existing user record.
● DELETE /users/{id}: Delete a user record by its ID.
Stocks:
● GET /stocks: Retrieve all stock records with pagination and sorting options.
● GET /stocks/{symbol}: Retrieve real-time price and details of a specific stock by its symbol. (This fetches the price from a third-party API.)
● PATCH /stocks/{symbol}/update: Update stock prices (internal cache for quick access).
