package com.dealership.api.repository;

import com.dealership.api.model.StaffMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StaffMemberRepository extends JpaRepository<StaffMember, Long> {
    List<StaffMember> findAllByOrderBySortOrderAscCreatedAtAsc();
}
