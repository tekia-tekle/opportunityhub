package com.opportunityhub.service;

import com.opportunityhub.dto.OpportunityRequestDTO;
import com.opportunityhub.dto.OpportunityResponseDTO;
import com.opportunityhub.exception.ResourceNotFoundException;
import com.opportunityhub.model.Opportunity;
import com.opportunityhub.repository.OpportunityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityRepository repository;

    public OpportunityServiceImpl(OpportunityRepository repository) {
        this.repository = repository;
    }

    // ================== CREATE ==================
    @Override
    public OpportunityResponseDTO createOpportunity(OpportunityRequestDTO dto) {
        Opportunity opp = new Opportunity();
        opp.setTitle(dto.getTitle());
        opp.setDescription(dto.getDescription());
        opp.setType(dto.getType());
        opp.setOrganizationName(dto.getOrganizationName());
        opp.setLocation(dto.getLocation());
        opp.setDeadline(dto.getDeadline());
        opp.setCategory(dto.getCategory());
        opp.setExternalLink(dto.getExternalLink());
        opp.setCreatedAt(LocalDateTime.now());
        opp.setExpired(false);

        repository.save(opp);
        return mapToResponse(opp);
    }

    // ================== GET ALL ==================
    @Override
    public List<OpportunityResponseDTO> getAllOpportunities() {
        markExpiredOpportunities();
        return repository.findByExpiredFalse()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================== GET BY ID ==================
    @Override
    public OpportunityResponseDTO getOpportunityById(Long id) {
        Opportunity opp = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity", "id", id));
        return mapToResponse(opp);
    }

    // ================== UPDATE ==================
    @Override
    public OpportunityResponseDTO updateOpportunity(Long id, OpportunityRequestDTO dto) {
        Opportunity opp = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity", "id", id));

        opp.setTitle(dto.getTitle());
        opp.setDescription(dto.getDescription());
        opp.setType(dto.getType());
        opp.setOrganizationName(dto.getOrganizationName());
        opp.setLocation(dto.getLocation());
        opp.setDeadline(dto.getDeadline());
        opp.setCategory(dto.getCategory());
        opp.setExternalLink(dto.getExternalLink());

        repository.save(opp);
        return mapToResponse(opp);
    }

    // ================== DELETE ==================
    @Override
    public void deleteOpportunity(Long id) {
        repository.deleteById(id);
    }

    // ================== FILTER BY TYPE ==================
    @Override
    public List<OpportunityResponseDTO> findByType(String type) {
        if (type == null || type.isEmpty()) return getAllOpportunities();
        return repository.findByType(type)
                .stream()
                .filter(opp -> !opp.isExpired())
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================== FILTER BY CATEGORY ==================
    @Override
    public List<OpportunityResponseDTO> findByCategory(String category) {
        if (category == null || category.isEmpty()) return getAllOpportunities();
        return repository.findByCategory(category)
                .stream()
                .filter(opp -> !opp.isExpired())
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================== SEARCH BY TITLE ==================
    @Override
    public List<OpportunityResponseDTO>findByTitle(String title) {
        if (title == null || title.isEmpty()) return getAllOpportunities();
        return repository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================== PAGINATION + SORTING ==================
    public List<OpportunityResponseDTO> getOpportunities(int page, int size, String sortBy, String sortDir) {
        markExpiredOpportunities();
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Page<Opportunity> pagedResult = repository.findByExpiredFalse(PageRequest.of(page, size, sort));
        return pagedResult.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================== EXPIRED HANDLING ==================
    public void markExpiredOpportunities() {
        List<Opportunity> all = repository.findByExpiredFalse();
        LocalDateTime now = LocalDateTime.now();
        all.forEach(opp -> {
            if (opp.getDeadline() != null && opp.getDeadline().isBefore(now)) {
                opp.setExpired(true);
                repository.save(opp);
            }
        });
    }

    // ================== HELPER ==================
    private OpportunityResponseDTO mapToResponse(Opportunity opp) {
        OpportunityResponseDTO dto = new OpportunityResponseDTO();
        dto.setId(opp.getId());
        dto.setTitle(opp.getTitle());
        dto.setDescription(opp.getDescription());
        dto.setType(opp.getType());
        dto.setOrganizationName(opp.getOrganizationName());
        dto.setLocation(opp.getLocation());
        dto.setDeadline(opp.getDeadline());
        dto.setCategory(opp.getCategory());
        dto.setExternalLink(opp.getExternalLink());
        dto.setExpired(opp.isExpired());
        return dto;
    }
}