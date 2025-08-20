package com.ecommerce.Model;

public class PaymentReceivedEvent extends Event {
    public String orderId;
    public double amountPaid;
}
