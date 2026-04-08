package com.dealership.api.repository;

import com.dealership.api.model.ContactSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactSubmissionRepository extends JpaRepository<ContactSubmission, Long> {

    /** Return all submissions newest first. */
    List<ContactSubmission> findAllByOrderByCreatedAtDesc();
}
