package com.ecommerce;

import com.ecommerce.Model.Event;
import com.ecommerce.Model.Order;
import com.ecommerce.Model.OrderStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerObserver implements OrderObserver {
    @Override
    public void onEventProcessed(Event event, Order order) {
        log.info("📝 Logger: Processed event " + event.eventType + " for Order " + order.orderId);
    }

    @Override
    public void onStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        log.info("📝 Logger: Order " + order.orderId + " status changed from " + oldStatus + " to " + newStatus);
    }
}