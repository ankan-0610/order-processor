package com.ecommerce;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.Model.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EventReader {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Event> readEvents(String filePath) throws IOException{
        List<Event> events = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Event event = objectMapper.readValue(line, Event.class);
                    events.add(event);
                } catch (JsonProcessingException e) {
                    System.err.println("⚠️ Failed to parse event: " + line);
                }
            }
        }
        
        return events;
    }
}
