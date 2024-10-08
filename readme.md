# VALR Book Order API

# Getting Started

## Dependencies
This project makes use of
* Java
* Gradle
* Spring Boot
* Spring Security with JWT
* Swagger for documentation

### Pre-requisite
1. Install docker from - https://docs.docker.com/get-docker/

### Setup
1. Start docker
2. Clone the repo from https://github.com/kelvin-bonni/valr-api.git
3. Navigate to the project directory on command line/terminal : `cd ../valr-api`
4. Run the app using `docker-compose up`
5. The application can be accessed at `http://localhost:8080`
6. The documentation can be accessed at `http://localhost:8080/swagger-ui.html`

```shell script
git clone https://github.com/kelvin-bonni/valr-api.git
cd ../valr-api
docker-compose up
```

### Endpoints
The following cURL commands or the postman collection attached can be used to test the endpoints

1. Authenticate user (with seeded credentials)

>**Note:**
>The user credentials are already seeded (email:john.doe@gmail.com/jane.doe@gmail.com, password: pass123)

```
curl --location 'http://localhost:8080/auth' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email":"john.doe@gmail.com",
    "password":"pass123"
}'
```
2. Place a limit order (with bearer token obtained from the Authenticate user call)
```
curl --location 'http://localhost:8080/api/v1/orders/limit' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer ••••••' \
--data '{
  "side": "BUY",
  "price": 5.0,
  "quantity": 1.0,
  "pair": "BTCUSDC"
}'
```
3. Get book orders by currency (replace ':currencyPair' with the specific currency pair)
```
curl --location 'http://localhost:8080/api/v1/:currencyPair/orderbook'
```
4. Get trades by currency (replace ':currencyPair' with the specific currency pair)
```
curl --location 'http://localhost:8080/api/v1/:currencyPair/tradehistory'
```