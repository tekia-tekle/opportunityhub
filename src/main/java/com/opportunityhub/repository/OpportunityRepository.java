package com.opportunityhub.repository;

import com.opportunityhub.model.Opportunity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {

    List<Opportunity> findByType(String type);

    List<Opportunity> findByCategory(String category);

    List<Opportunity> findByExpiredFalse();
    List<Opportunity> findByTitleContainingIgnoreCase(String title);

    //pagination
    Page<Opportunity> findByExpiredFalse(Pageable pageable);

    Page<Opportunity> findByTypeAndExpiredFalse(String type, Pageable pageable);

    Page<Opportunity> findByTitleContainingIgnoreCaseAndExpiredFalse(String title, Pageable pageable);

    Page<Opportunity> findByCategoryAndExpiredFalse(String category, Pageable pageable);



}