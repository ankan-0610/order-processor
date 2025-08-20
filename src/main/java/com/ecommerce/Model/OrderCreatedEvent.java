package com.ecommerce.Model;

import java.util.List;

public class OrderCreatedEvent extends Event {
    public String orderId;
    public String customerId;
    public List<Item> items;
    public double totalAmount;
}
