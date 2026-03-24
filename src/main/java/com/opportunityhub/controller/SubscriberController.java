package com.opportunityhub.controller;

import com.opportunityhub.dto.SubscriberRequestDTO;
import com.opportunityhub.dto.SubscriberResponseDTO;
import com.opportunityhub.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscribers")
@RequiredArgsConstructor
public class SubscriberController {

    private final SubscriberService service;

    // Public subscription endpoint
    @PostMapping("/subscribe")
    public ResponseEntity<SubscriberResponseDTO> subscribe(@RequestBody SubscriberRequestDTO dto) {
        return ResponseEntity.ok(service.subscribe(dto));
    }

    // Admin-only: get all subscribers
    @GetMapping
    public ResponseEntity<List<SubscriberResponseDTO>> getAllSubscribers() {
        return ResponseEntity.ok(service.getAllSubscribers());
    }
}