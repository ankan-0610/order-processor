package com.ecommerce;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.Model.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventReader {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Event> readEvents(String filePath) throws IOException{
        List<Event> events = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    log.warn("Skipping empty line in event file");
                    continue; // skip blank lines
                }

                try {
                    Event event = objectMapper.readValue(line, Event.class);
                    events.add(event);
                    log.info("Successfully parsed event: {}", event);
                    log.info("Event Type: {}", event.eventType);
                } catch (JsonProcessingException e) {
                    log.error("Failed to parse event: " + line);
                    log.error("Error: " + e);
                }
            }
        }
        
        log.info("📦 Total events parsed successfully: {}", events.size());
        return events;
    }
}
