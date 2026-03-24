package com.opportunityhub.service;

import com.opportunityhub.dto.SubscriberRequestDTO;
import com.opportunityhub.dto.SubscriberResponseDTO;
import com.opportunityhub.model.Subscriber;
import com.opportunityhub.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriberService {

    private final SubscriberRepository repository;

    public SubscriberResponseDTO subscribe(SubscriberRequestDTO dto) {
        Subscriber sub = Subscriber.builder()
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .active(true)
                .build();
        repository.save(sub);
        return mapToDTO(sub);
    }

    public List<SubscriberResponseDTO> getAllSubscribers() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private SubscriberResponseDTO mapToDTO(Subscriber subscriber) {
        return new SubscriberResponseDTO(
                subscriber.getId(),
                subscriber.getEmail(),
                subscriber.getPhone(),
                subscriber.isActive()
        );
    }
}