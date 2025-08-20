package com.ecommerce.Model;

import lombok.Getter;

@Getter
public class PaymentReceivedEvent extends Event {
    public String orderId;
    public double amountPaid;
}
