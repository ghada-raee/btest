package com.example.banktest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class GenericResponse {

    public static final String GENERIC_MESSAGE = "Error, please try again later";

    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object resp) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", message);
        map.put("status", status.value());
        map.put("data", resp);

        return new ResponseEntity<Object>(map, status);
    }
}
