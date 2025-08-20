# order-processor

## Events

1. Order creation

```json
{
    "eventId": "e1",
    "timestamp": "2025-07-29T10:00:00Z",
    "eventType": "OrderCreated",
    "orderId": "ORD001",
    "customerId": "CUST001",
    "items": [
        { "itemId": "P001", "qty": 2 },
        { "itemId": "P002", "qty": 1 }
    ],
    "totalAmount": 150.0
}
```

2. Payment received

```json
{
    "eventId": "e2",
    "timestamp": "2025-07-29T10:05:00Z",
    "eventType": "PaymentReceived",
    "orderId": "ORD001",
    "amountPaid": 100.0
}
```
3. Shipping scheduling

```json
{
    "eventId": "e4",
    "timestamp": "2025-07-29T11:00:00Z",
    "eventType": "ShippingScheduled",
    "orderId": "ORD001",
    "shippingDate": "2025-07-30"
}
```

4. Order cancellation

```json
{
    "eventId": "e6",
    "timestamp": "2025-07-29T12:30:00Z",
    "eventType": "OrderCancelled",
    "orderId": "ORD002",
    "reason": "Customer requested cancellation"
}
```

## Requirements Covered

1. Domain Model Classes
2. Event Ingestion from Text files with JSON into Objects
3. Event Processing - Update Object State
4. Notification/observer system

## How to use

1. Add your event objects in json format, in the following file:
``` 
src/main/resources/events.txt
```

each json event must be covered on a single line, for the EventReader to work

2. Run the application

```bash
mvn clean package
java -jar target/order-processing-system-1.0-SNAPSHOT.jar
```

3. Check Logs