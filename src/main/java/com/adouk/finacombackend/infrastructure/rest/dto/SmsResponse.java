package com.adouk.finacombackend.infrastructure.rest.dto;

import lombok.Data;

@Data
public class SmsResponse {
    private boolean success;
    private String message;
    private String bulk_id;
    private Integer cost;
    private String currency;

    public SmsResponse(boolean b, String s, Object o, Object o1, Object o2, Object o3) {
        this.success = b;
        this.message = s;
        this.bulk_id = (String) o; // Example casting, adjust as necessary
        this.cost = (Integer) o1; // Example casting, adjust as necessary
        this.currency = (String) o2; // Example casting, adjust as necessary
    }

    // Getters et Setters
}

