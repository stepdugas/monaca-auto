package com.dealership.api.repository;

import com.dealership.api.model.DealershipConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DealershipConfigRepository extends JpaRepository<DealershipConfig, Long> {

    Optional<DealershipConfig> findByKey(String key);

    List<DealershipConfig> findAllByOrderByKeyAsc();
}
