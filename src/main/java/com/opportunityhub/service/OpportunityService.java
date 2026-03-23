package com.opportunityhub.service;

import com.opportunityhub.dto.OpportunityRequestDTO;
import com.opportunityhub.dto.OpportunityResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
public interface OpportunityService {

    OpportunityResponseDTO createOpportunity(OpportunityRequestDTO dto);

    List<OpportunityResponseDTO> getAllOpportunities();
    List<OpportunityResponseDTO> findByType(String type);
    List<OpportunityResponseDTO> findByCategory(String category);

    OpportunityResponseDTO getOpportunityById(Long id);
    List<OpportunityResponseDTO> findByTitle(String title);

    OpportunityResponseDTO updateOpportunity(Long id, OpportunityRequestDTO dto);

    void deleteOpportunity(Long id);
    List<OpportunityResponseDTO> getOpportunities(int page, int size, String sortBy, String sortDir);

}