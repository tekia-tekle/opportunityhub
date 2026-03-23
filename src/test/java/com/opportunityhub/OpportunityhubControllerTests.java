package com.opportunityhub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opportunityhub.dto.OpportunityRequestDTO;
import com.opportunityhub.dto.OpportunityResponseDTO;
import com.opportunityhub.repository.OpportunityRepository;
import com.opportunityhub.service.OpportunityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OpportunityhubControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OpportunityService service;

    @Autowired
    private OpportunityRepository repository;

    private OpportunityRequestDTO dto1;
    private OpportunityRequestDTO dto2;

    @BeforeEach
    void setup() {
        repository.deleteAll();

        dto1 = new OpportunityRequestDTO();
        dto1.setTitle("Java Developer");
        dto1.setType("JOB");
        dto1.setCategory("IT");
        dto1.setOrganizationName("TechOrg");
        dto1.setLocation("Remote");
        dto1.setDeadline(LocalDateTime.now().plusDays(10));
        dto1.setExternalLink("https://techorg.com/apply");

        dto2 = new OpportunityRequestDTO();
        dto2.setTitle("AI Scholarship");
        dto2.setType("SCHOLARSHIP");
        dto2.setCategory("AI");
        dto2.setOrganizationName("AI Institute");
        dto2.setLocation("Online");
        dto2.setDeadline(LocalDateTime.now().plusDays(15));
        dto2.setExternalLink("https://aiinstitute.com/apply");
    }

    // === Service-level tests ===
    @Test
    void testCreateAndGetOpportunity() {
        OpportunityResponseDTO created = service.createOpportunity(dto1);
        assertNotNull(created.getId());

        OpportunityResponseDTO fetched = service.getOpportunityById(created.getId());
        assertEquals("TechOrg", fetched.getOrganizationName());
    }

    @Test
    void testUpdateOpportunity() {
        OpportunityResponseDTO created = service.createOpportunity(dto1);
        OpportunityRequestDTO updateDto = new OpportunityRequestDTO();
        updateDto.setTitle("Java Backend Developer");
        updateDto.setType("JOB");
        updateDto.setCategory("IT");
        updateDto.setOrganizationName("TechOrg");
        updateDto.setLocation("Remote");
        updateDto.setDeadline(LocalDateTime.now().plusDays(10));
        updateDto.setExternalLink("https://techorg.com/apply");

        OpportunityResponseDTO updated = service.updateOpportunity(created.getId(), updateDto);
        assertEquals("Java Backend Developer", updated.getTitle());
    }

    @Test
    void testDeleteOpportunity() {
        OpportunityResponseDTO created = service.createOpportunity(dto1);
        service.deleteOpportunity(created.getId());
        assertThrows(RuntimeException.class, () -> service.getOpportunityById(created.getId()));
    }

    @Test
    void testFilterAndSearch() {
        service.createOpportunity(dto1);
        service.createOpportunity(dto2);

        List<OpportunityResponseDTO> jobs = service.findByType("JOB");
        assertEquals(1, jobs.size());

        List<OpportunityResponseDTO> ai = service.findByCategory("AI");
        assertEquals(1, ai.size());

        List<OpportunityResponseDTO> search = service.findByTitle("AI");
        assertEquals(1, search.size());
    }

    @Test
    void testPagination() {
        for (int i = 1; i <= 12; i++) {
            OpportunityRequestDTO dto = new OpportunityRequestDTO();
            dto.setTitle("Job " + i);
            dto.setType("JOB");
            dto.setCategory("IT");
            dto.setOrganizationName("Org " + i);
            dto.setDeadline(LocalDateTime.now().plusDays(i));
            service.createOpportunity(dto);
        }

        List<OpportunityResponseDTO> page0 = service.getOpportunities(0, 5, "deadline", "asc");
        List<OpportunityResponseDTO> page1 = service.getOpportunities(1, 5, "deadline", "asc");

        assertEquals(5, page0.size());
        assertEquals(5, page1.size());
        assertNotEquals(page0.get(0).getTitle(), page1.get(0).getTitle());
    }

    // === MockMvc / Integration Tests ===
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCreateAndSearchAndPagedEndpoints() throws Exception {
        // Create opportunity with CSRF
        mockMvc.perform(post("/api/opportunities")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Java Developer"))
                .andExpect(jsonPath("$.organizationName").value("TechOrg"))
                .andExpect(jsonPath("$.expired").value(false));

        // Search by title substring
        mockMvc.perform(get("/api/opportunities/search")
                        .param("title", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Java Developer"));

        // Paged + filter + substring
        mockMvc.perform(get("/api/opportunities/paged")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sortBy", "deadline")
                        .param("sortDir", "asc")
                        .param("type", "JOB")
                        .param("category", "IT")
                        .param("title", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Java Developer"));
    }
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"}) // Mock an authenticated user
    void testUpdateOpportunityEndpoint() throws Exception {
        // 1️⃣ First, create an opportunity
        var createResult = mockMvc.perform(post("/api/opportunities")
                        .with(csrf()) // CSRF token required for POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Java Developer"))
                .andReturn();

        // Extract the ID from the response
        String createResponse = createResult.getResponse().getContentAsString();
        Long opportunityId = objectMapper.readTree(createResponse).get("id").asLong();

        // 2️⃣ Prepare update data
        OpportunityRequestDTO updateDto = new OpportunityRequestDTO();
        updateDto.setTitle("Java Backend Developer");
        updateDto.setType("Internship");
        updateDto.setCategory("software developement");
        updateDto.setOrganizationName("Malam Engineering plc");
        updateDto.setLocation("Ethiopia");
        updateDto.setDeadline(dto1.getDeadline().plusDays(5)); // Extend deadline
        updateDto.setExternalLink(dto1.getExternalLink());

        // 3️⃣ Perform PUT request to update the opportunity
        mockMvc.perform(put("/api/opportunities/{id}", opportunityId)
                        .with(csrf()) // CSRF token required for PUT
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Java Backend Developer"))
                .andExpect(jsonPath("$.type").value("Internship"))
                .andExpect(jsonPath("$.category").value("software developement"))
                .andExpect(jsonPath("$.organizationName").value("Malam Engineering plc"))
                .andExpect(jsonPath("$.location").value("Ethiopia"));
    }
}