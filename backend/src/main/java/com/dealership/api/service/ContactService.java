package com.dealership.api.service;

import com.dealership.api.dto.ContactRequest;
import com.dealership.api.model.ContactSubmission;
import com.dealership.api.repository.ContactSubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContactService {

    private final ContactSubmissionRepository contactRepository;

    // Constructor for dependency injection
    public ContactService(ContactSubmissionRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Transactional
    public ContactSubmission save(ContactRequest req) {
        ContactSubmission sub = new ContactSubmission();
        sub.setName(req.getName());
        sub.setEmail(req.getEmail());
        sub.setPhone(req.getPhone());
        sub.setMessage(req.getMessage());
        sub.setCarId(req.getCarId());
        return contactRepository.save(sub);
    }

    public List<ContactSubmission> findAll() {
        return contactRepository.findAllByOrderByCreatedAtDesc();
    }
}
