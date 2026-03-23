package com.opportunityhub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Entity
@Table(name = "opportunities")
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    private String type; // JOB, SCHOLARSHIP, INTERNSHIP, etc.

    private String organizationName;

    private String location;

    private LocalDateTime deadline;

    private String category;

    private String externalLink;

    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;

    private boolean expired;

    public Opportunity(){}

    // Getters & Setters

    //toString


}