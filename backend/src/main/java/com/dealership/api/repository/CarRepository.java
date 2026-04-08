package com.dealership.api.repository;

import com.dealership.api.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data repository for {@link Car}.
 * JpaSpecificationExecutor enables dynamic filter queries.
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {

    /**
     * Find all cars with a specific status (e.g., "available", "sold", "pending").
     *
     * @param status The car status
     * @return List of cars matching the status
     */
    List<Car> findAllByStatus(String status);

    long countByStatus(String status);
}
