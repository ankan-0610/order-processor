package com.ecommerce;

import java.util.ArrayList;
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
    private final List<OrderObserver> observers = new ArrayList<>();

    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }

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

        // Notify observers after processing
        Order order = (event instanceof OrderCreatedEvent)
                ? orderStore.get(((OrderCreatedEvent) event).orderId)
                : orderStore.get(getOrderIdFromEvent(event));

        if (order != null) {
            notifyEventProcessed(event, order);
        }
    }

    private void updateStatus(Order order, OrderStatus newStatus) {
        OrderStatus oldStatus = order.status;
        if (oldStatus != newStatus) {
            order.setStatus(newStatus);
            orderStore.put(order.orderId, order);
            notifyStatusChanged(order, oldStatus, newStatus);
        }
    }

    private String getOrderIdFromEvent(Event e) {
        if (e instanceof PaymentReceivedEvent) return ((PaymentReceivedEvent) e).orderId;
        if (e instanceof ShippingScheduledEvent) return ((ShippingScheduledEvent) e).orderId;
        if (e instanceof OrderCancelledEvent) return ((OrderCancelledEvent) e).orderId;
        return null;
    }

    private void handleOrderCreated(OrderCreatedEvent event) {
        // Logic to handle order created event
        Order order = new Order(
                event.getOrderId(),
                event.getCustomerId(),
                event.getItems(),
                event.getTotalAmount(),
                0,
                OrderStatus.CREATED,
                new ArrayList<>()
        );
        order.addEvent(event);
        orderStore.put(event.getOrderId(), order);
        log.info("Order created: {}", order.orderId);
    }
    
    private void handlePaymentReceived(PaymentReceivedEvent event) {
        // Logic to handle payment received event
        Order order = orderStore.get(event.orderId);
        if (order == null) {
            log.error("No order found for orderId: {}", event.orderId);
            return;
        }
        if (order.getStatus() != OrderStatus.CREATED &&
        order.getStatus() != OrderStatus.PARTIALLY_PAID) {
            log.error("Payment received for order {} in status {}. Expected CREATED or PARTIALLY_PAID.",
                    order.getOrderId(), order.getStatus());
            return;
        }
        order.setPaidAmount(order.getPaidAmount()+event.getAmountPaid());
        if (order.getPaidAmount() >= order.getTotalAmount()) {
            updateStatus(order, OrderStatus.PAID);
            log.info("Payment received for order: {}", order.getOrderId());
        } else if(order.getPaidAmount() > 0){
            updateStatus(order, OrderStatus.PARTIALLY_PAID);
            log.warn("Payment amount {} is less than total amount {} for order: {}",
                    event.getAmountPaid(), order.getTotalAmount(), order.getOrderId());
        } else {
            log.error("Invalid payment amount for order: {}", order.getOrderId());
            return;
        }
        orderStore.put(event.getOrderId(), order);
        order.addEvent(event);
        log.info("Payment processed for order: {}", order.orderId);
    }

    private void handleShippingScheduled(ShippingScheduledEvent event) {
        // Logic to handle shipping scheduled event
        Order order = orderStore.get(event.orderId);
        if (order == null || order.getStatus() != OrderStatus.PAID) return;
        updateStatus(order, OrderStatus.SHIPPED);
        order.addEvent(event);
        orderStore.put(event.getOrderId(), order);
        log.info("📦 Order " + order.getOrderId() + " shipped on " + event.getShippingDate());
    }

    private void handleOrderCancelled(OrderCancelledEvent event) {
        // Logic to handle order cancelled event
        Order order = orderStore.get(event.getOrderId());
        if (order == null)
            return;
        updateStatus(order, OrderStatus.CANCELLED);
        order.addEvent(event);
        orderStore.put(event.getOrderId(), order);
        log.info("Order {} has been cancelled", order.getOrderId());
    }
    
    private void notifyEventProcessed(Event event, Order order) {
        for (OrderObserver obs : observers) {
            obs.onEventProcessed(event, order);
        }
    }

    private void notifyStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        for (OrderObserver obs : observers) {
            obs.onStatusChanged(order, oldStatus, newStatus);
        }
    }
}
