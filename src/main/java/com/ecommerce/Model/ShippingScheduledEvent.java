package com.ecommerce.Model;

import lombok.Getter;

@Getter
public class ShippingScheduledEvent extends Event{
    public String orderId;
    public String shippingDate;
}
