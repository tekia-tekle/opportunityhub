package com.opportunityhub.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriberRequestDTO {
    private String email;
    private String phone;
}