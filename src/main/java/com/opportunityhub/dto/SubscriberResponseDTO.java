package com.opportunityhub.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriberResponseDTO {
    private Long id;
    private String email;
    private String phone;
    private boolean active;
}