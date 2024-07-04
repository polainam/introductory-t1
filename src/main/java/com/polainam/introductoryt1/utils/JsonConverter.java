package com.polainam.introductoryt1.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polainam.introductoryt1.models.Status;
import org.springframework.stereotype.Component;

@Component
public class JsonConverter {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String serialize(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public String createStatusJson(Status status, String token) throws JsonProcessingException {
        status.setToken(token);
        status.setStatus("increased");
        return serialize(status);
    }
}
