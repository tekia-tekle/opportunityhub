package com.opportunityhub.controller;

import com.opportunityhub.dto.*;
import com.opportunityhub.service.OpportunityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {

    private final OpportunityService service;

    public OpportunityController(OpportunityService service) {
        this.service = service;
    }

    @PostMapping
    public OpportunityResponseDTO create(@RequestBody OpportunityRequestDTO dto) {
        return service.createOpportunity(dto);
    }

    @GetMapping
    public List<OpportunityResponseDTO> getAll() {
        return service.getAllOpportunities();
    }
    @GetMapping("/filter")
    public List<OpportunityResponseDTO> filter(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category) {

        return service.findByType(type).stream()
                .filter(opp -> category == null || category.equals(opp.getCategory()))
                .collect(Collectors.toList());
    }
    @GetMapping("/search")
    public List<OpportunityResponseDTO> search(
            @RequestParam(required = false) String title) {
        if (title == null) {
            return service.getAllOpportunities();
        }
        return service.findByTitle(title);
    }

    @GetMapping("/{id}")
    public OpportunityResponseDTO getById(@PathVariable Long id) {
        return service.getOpportunityById(id);
    }

    @PutMapping("/{id}")
    public OpportunityResponseDTO update(@PathVariable Long id,
                                         @RequestBody OpportunityRequestDTO dto) {
        return service.updateOpportunity(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteOpportunity(id);
    }
    @GetMapping("/paged")
    public List<OpportunityResponseDTO> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "deadline") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String title
    ) {
        // 1️⃣ If searching by title
        if (title != null && !title.isEmpty()) {
            return service.findByTitle(title)
                    .stream()
                    .skip((long) page * size)
                    .limit(size)
                    .toList();
        }

        // 2️⃣ If filtering by type or category
        if ((type != null && !type.isEmpty()) || (category != null && !category.isEmpty())) {
            List<OpportunityResponseDTO> filtered = service.getAllOpportunities();

            if (type != null && !type.isEmpty()) {
                filtered = filtered.stream()
                        .filter(o -> o.getType().equalsIgnoreCase(type))
                        .toList();
            }
            if (category != null && !category.isEmpty()) {
                filtered = filtered.stream()
                        .filter(o -> o.getCategory().equalsIgnoreCase(category))
                        .toList();
            }

            return filtered.stream()
                    .sorted((o1, o2) -> {
                        if (sortBy.equalsIgnoreCase("deadline")) {
                            return sortDir.equalsIgnoreCase("asc") ?
                                    o1.getDeadline().compareTo(o2.getDeadline()) :
                                    o2.getDeadline().compareTo(o1.getDeadline());
                        } else { // default: createdAt (not in DTO? add if needed)
                            return 0;
                        }
                    })
                    .skip((long) page * size)
                    .limit(size)
                    .toList();
        }

        // 3️⃣ Default: get all with pagination & sorting
        return service.getOpportunities(page, size, sortBy, sortDir);
    }
}