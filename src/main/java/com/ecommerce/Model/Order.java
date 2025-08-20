package com.ecommerce.Model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Order {
    public String orderId;
    public String customerId;
    public List<Item> items;
    public double totalAmount;
    public double paidAmount;
    public OrderStatus status;
    public List<Event> eventHistory;

    public void addEvent(Event event) {
        this.eventHistory.add(event);
    }
}
