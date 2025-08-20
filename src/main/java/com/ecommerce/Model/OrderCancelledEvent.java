package com.ecommerce.Model;

import lombok.Getter;

@Getter
public class OrderCancelledEvent extends Event{
    public String orderId;
    public String reason;
}
