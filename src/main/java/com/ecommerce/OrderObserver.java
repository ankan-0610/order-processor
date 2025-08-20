package com.ecommerce;

import com.ecommerce.Model.Event;
import com.ecommerce.Model.Order;
import com.ecommerce.Model.OrderStatus;

public interface OrderObserver {
    void onEventProcessed(Event event, Order order);
    void onStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus);
}