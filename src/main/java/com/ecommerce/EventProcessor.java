package com.ecommerce;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecommerce.Model.Event;
import com.ecommerce.Model.Order;
import com.ecommerce.Model.OrderCancelledEvent;
import com.ecommerce.Model.OrderCreatedEvent;
import com.ecommerce.Model.OrderStatus;
import com.ecommerce.Model.PaymentReceivedEvent;
import com.ecommerce.Model.ShippingScheduledEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventProcessor {

    private final Map<String, Order> orderStore = new HashMap<>();

    public void processEvent(Event event) {
        if (event instanceof OrderCreatedEvent) {
            handleOrderCreated((OrderCreatedEvent) event);
        } else if (event instanceof PaymentReceivedEvent) {
            handlePaymentReceived((PaymentReceivedEvent) event);
        } else if (event instanceof ShippingScheduledEvent) {
            handleShippingScheduled((ShippingScheduledEvent) event);
        } else if (event instanceof OrderCancelledEvent) {
            handleOrderCancelled((OrderCancelledEvent) event);
        } else {
            log.error("⚠️ Unknown event type: {}", event.eventType);
        }
    }

    private void handleOrderCreated(OrderCreatedEvent event) {
        // Logic to handle order created event
        Order order = new Order(
                event.orderId,
                event.customerId,
                event.items,
                event.totalAmount,
                OrderStatus.CREATED,
                List.of(event) // Initialize event history with the created event
        );
        order.addEvent(event);
        orderStore.put(event.orderId, order);
        log.info("Order created: {}", order.orderId);
    }
    
    private void handlePaymentReceived(PaymentReceivedEvent event) {
        // Logic to handle payment received event
        Order order = orderStore.get(event.orderId);
        if (order == null || order.status != OrderStatus.CREATED) {
            log.error("No order found for orderId: {}", event.orderId);
            return;
        }
        if(event.amountPaid >= order.totalAmount) {
            order.status = OrderStatus.PAID;
            order.addEvent(event);
            log.info("Payment received for order: {}", order.orderId);
        } else if(event.amountPaid > 0){
            order.status = OrderStatus.PARTIALLY_PAID;
            log.warn("Payment amount {} is less than total amount {} for order: {}",
                    event.amountPaid, order.totalAmount, order.orderId);
        } else {
            log.error("Payment amount is zero or negative for order: {}", order.orderId);
            return;
        }
        order.addEvent(event);
        log.info("Order status updated to: {}", order.status);
        log.info("Payment processed for order: {}", order.orderId);
    }

    private void handleShippingScheduled(ShippingScheduledEvent event) {
        // Logic to handle shipping scheduled event
        Order order = orderStore.get(event.orderId);
        if (order == null || order.status != OrderStatus.PAID) return;
        order.status = OrderStatus.SHIPPED;
        order.addEvent(event);
        log.info("📦 Order " + order.orderId + " shipped on " + event.shippingDate);
    }

    private void handleOrderCancelled(OrderCancelledEvent event) {
        // Logic to handle order cancelled event
        Order order = orderStore.get(event.orderId);
        if(order == null) return;
        order.status = OrderStatus.CANCELLED;
        log.info("Order {} has been cancelled", order.orderId);
        order.addEvent(event);
    }
}
