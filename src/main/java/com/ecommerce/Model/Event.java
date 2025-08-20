package com.ecommerce.Model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,       // use logical name for subtype
    include = JsonTypeInfo.As.PROPERTY,
    property = "eventType",           // look at "eventType" field in JSON
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = OrderCreatedEvent.class, name = "OrderCreated"),
    @JsonSubTypes.Type(value = PaymentReceivedEvent.class, name = "PaymentReceived"),
    @JsonSubTypes.Type(value = ShippingScheduledEvent.class, name = "ShippingScheduled"),
    @JsonSubTypes.Type(value = OrderCancelledEvent.class, name = "OrderCancelled")
})
public abstract class Event {
    public String eventId;
    public String timestamp;
    public String eventType;
}
