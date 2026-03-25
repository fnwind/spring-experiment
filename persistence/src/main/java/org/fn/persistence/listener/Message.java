package org.fn.persistence.listener;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Message<T> {
    private String destination;
    private T body;
    private long timestamp;
    private Map<String, Object> headers = new HashMap<>();
}