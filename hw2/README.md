# cs504-hw-2

### TODO

1. add more test case
1. add frontend

### steps to test

1. WORKSPACE=spring-cloud-food-delivery
1. go to $WORKSPACE/
1. run: mvn clean install
1. run: docker-compose up
1. run: docker exec -i springcloudfooddelivery_mysql_1 mysql --password=mypass < ./setup.sql
1. go to $WORKSPACE/platform/eureka/target/
1. start eureka server: java -jar food-delivery-eureka-server-0.0.1-SNAPSHOT.jar
1. go to $WORKSPACE/dashboard/target/
1. start dashboard: java -jar dashboard-1.0.0.BUILD-SNAPSHOT.jar
1. go to $WORKSPACE/order-service/target/
1. start order-service: java -jar order-service-1.0.0.BUILD-SNAPSHOT.jar
1. go to $WORKSPACE/restaurant-service/target/
1. start restaurant-service: java -jar restaurant-service-1.0.0.BUILD-SNAPSHOT.jar
1. go to $WORKSPACE/payment-service/target/
1. start payment-service: java -jar payment-service-1.0.0.BUILD-SNAPSHOT.jar
1. upload restaurants: POST http://localhost:8080/restaurant-service/upload;

   use $WORKSPACE/restaurant-service/src/main/resources/sample.json
1. upload orders: POST http://localhost:8080/order-service/upload;
   
   use $WORKSPACE/order-service/src/main/resources/sample.json
1. check status of order using orderId return by upload:
   
   GET localhost:8080/order-service/orders/<orderId>
   * the status should be PENDING
   * createdAt and updatedAt should be same
   * deliveryEstimatedTime should be 0
1. check status of payment using orderId:
   
   GET localhost:8080/payment-service/payments/<orderId>
1. after 10 seconds, check order again
   
   GET GET localhost:8080/order-service/orders/<orderId> 

### architecture design

Five services:
1. eureka-server
2. restaurant-service + mysql
3. order-service + mongodb
4. payment-service + mysql
5. dashboard (use zuul as edging service)

Main workflow:
1. create restaurants using REST API /upload of restaurant-service
2. create orders using REST API /upload of order-service
3. the creation of order will publish payment object to payment-service via MQ
4. payment-service will create payment from MQ and use REST API /complete of order-service to complete order

### API design

#### restaurant-service

* create list of restaurants

```
POST http://localhost:8080/restaurant-service/upload

[
  {
    "name": "In-N-Out Burger",
    "description": "In-N-Out Burger",
    "items": [
      {
        "name": "DOUBLE-DOUBLE",
        "description": "FRESHLY BAKED USING OLD-FASHIONED, SLOW-RISING SPONGE DOUGH",
        "price": 5.0
      },
      {
        "name": "CHEESEBURGER",
        "description": "FRESHLY BAKED USING OLD-FASHIONED, SLOW-RISING SPONGE DOUGH",
        "price": 4.0
      }
    ],
    "address": {
      "address": "604 E El Camino Real",
      "city": "Sunnyvale",
      "state": "CA",
      "zip": "94087"
    }
  },
  {
    "name": "Jack in the Box",
    "description": "Jack in the Box",
    "items": [
      {
        "name": "CLASSIC BUTTERY JACK",
        "description": "CLASSIC BUTTERY JACK",
        "price": 3.0
      },
      {
        "name": "BACON & SWISS BUTTERY JACK",
        "description": "BACON & SWISS BUTTERY JACK",
        "price": 3.5
      }
    ],
    "address": {
      "address": "3395 Stevens Creek Blvd",
      "city": "San Jose",
      "state": "CA",
      "zip": "95117"
    }
  }
]
```

* delete all restaurants

```
DELETE http://localhost:8080/restaurant-service/purge
```

* find restaurant by name

```
GET http://localhost:8080/restaurant-service/restaurants/{name}?page=0&size=1

E.g.
GET http://localhost:8080/restaurants/Jack%20in%20the%20Box?page=0&size=1
```

#### order-service

* create list of orders and publish payments to MQ

```
POST http://localhost:8080/order-service/upload

[
  {
    "note": "xxx",
    "restaurantId": 1,
    "deliveryCustomer": "John",
    "items": [
      {
        "name": "DOUBLE-DOUBLE",
        "description": "FRESHLY BAKED USING OLD-FASHIONED, SLOW-RISING SPONGE DOUGH",
        "price": 5.0,
        "quantity": 2
      }
    ],
    "creditCard": {
      "name": "John",
      "number": "1234-1234-1234-1234",
      "year": 2020,
      "month": 9,
      "securityCode": 666
    },
    "deliveryAddress": {
      "address": "1911 Landings Dr",
      "city": "Mountain View",
      "state": "CA",
      "zip": "94043"
    }
  }
]
```

* delete all orders
```
DELETE http://localhost:8080/order-service/purge
```

* mark order as success or failure
* fill in paymentId and deliveryEstimatedTime
```
POST http://localhost:8080/order-service/complete

{
  "id": "...",
  "status": "SUCCESS",
  "paymentId": 1
}
```

#### payment-service

* create list of payments and start async tasks to complete orders (with 1/10 probability to fail)
* used for debug only.

```
POST http://localhost:8080/payment-service/upload

[
  {
    "orderId": "123",
    "price": "20",
    "creditCard": {
      "year": 2020,
      "month": 9,
      "number": "1234-1234-1234-1234",
      "securityCode": 666
    }
  }
]
```

* stop all running tasks and delete all payments
* used for debug only

```
DELETE http://localhost:8080/payment-service/purge
```

* get all running tasks with pagination
* used for debug only

```
GET http://localhost:8080/payment-service/tasks?page=0&size=1
```

* get first payment by orderId
* used for debug only

```
GET http://localhost:8080/payment-service/payments/{orderId}
```

* MQ consumer: create payment and start async task to complete order
* message format: JSON of Payment
