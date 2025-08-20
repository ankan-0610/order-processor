package com.ecommerce;

import java.util.List;

import com.ecommerce.Model.Event;

public class OrderProcessingApp {
    public static void main(String[] args) throws Exception {
        EventReader reader = new EventReader();
        EventProcessor processor = new EventProcessor();
        // Attach observers
        processor.addObserver(new LoggerObserver());
        processor.addObserver(new AlertObserver());

        // Read and process events
        List<Event> events = reader.readEvents("src/main/resources/events.txt");


        for (Event event : events) {
            processor.processEvent(event);
        }
    }
}
