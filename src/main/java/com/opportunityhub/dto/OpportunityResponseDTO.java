package com.opportunityhub.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OpportunityResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String type;
    private String organizationName;
    private String location;
    private LocalDateTime deadline;
    private String category;
    private String externalLink;
    private boolean expired;

    // Getters & Setters
}