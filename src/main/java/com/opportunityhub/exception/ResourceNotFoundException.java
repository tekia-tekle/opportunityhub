package com.opportunityhub.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

    // Getters
    private final String resourceName; // e.g., "Opportunity"
    private final String fieldName;    // e.g., "id"
    private final Object fieldValue;   // e.g., 5L

    // Constructor
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

}