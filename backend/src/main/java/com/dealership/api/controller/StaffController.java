package com.dealership.api.controller;

import com.dealership.api.model.StaffMember;
import com.dealership.api.repository.StaffMemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StaffController {

    private final StaffMemberRepository staffRepo;

    public StaffController(StaffMemberRepository staffRepo) {
        this.staffRepo = staffRepo;
    }

    /** GET /api/public/staff — public, no auth */
    @GetMapping("/api/public/staff")
    public ResponseEntity<List<StaffMember>> getStaff() {
        return ResponseEntity.ok(staffRepo.findAllByOrderBySortOrderAscCreatedAtAsc());
    }

    /** POST /api/admin/staff — create staff member */
    @PostMapping("/api/admin/staff")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffMember> create(@RequestBody StaffMember member) {
        return ResponseEntity.status(HttpStatus.CREATED).body(staffRepo.save(member));
    }

    /** PUT /api/admin/staff/{id} — update staff member */
    @PutMapping("/api/admin/staff/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffMember> update(@PathVariable Long id, @RequestBody StaffMember updated) {
        return staffRepo.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setTitle(updated.getTitle());
            existing.setBio(updated.getBio());
            existing.setEmail(updated.getEmail());
            existing.setPhone(updated.getPhone());
            existing.setPhotoUrl(updated.getPhotoUrl());
            existing.setSortOrder(updated.getSortOrder());
            return ResponseEntity.ok(staffRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    /** DELETE /api/admin/staff/{id} */
    @DeleteMapping("/api/admin/staff/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        staffRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
