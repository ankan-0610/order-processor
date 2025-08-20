package com.ecommerce;

import com.ecommerce.Model.Event;
import com.ecommerce.Model.Order;
import com.ecommerce.Model.OrderCancelledEvent;
import com.ecommerce.Model.OrderStatus;

public class AlertObserver implements OrderObserver {
    @Override
    public void onEventProcessed(Event event, Order order) {
        // Alerts only on critical events
        if (event instanceof OrderCancelledEvent) {
            System.out.println("🚨 [ALERT]: Order " + order.orderId + " was cancelled!");
        }
    }

    @Override
    public void onStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        if (newStatus == OrderStatus.CANCELLED || newStatus == OrderStatus.SHIPPED) {
            System.out.println("🚨 [ALERT]: Order " + order.orderId + ": Status changed to " + newStatus);
        }
    }
    
}
