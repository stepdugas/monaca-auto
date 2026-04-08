package com.dealership.api.service;

import com.dealership.api.dto.CarFilterRequest;
import com.dealership.api.model.Car;
import com.dealership.api.model.CarImage;
import com.dealership.api.repository.CarRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Business logic for car inventory operations.
 */
@Service
public class CarService {

    private final CarRepository carRepository;

    // Constructor for dependency injection
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // ── Public queries ────────────────────────────────────────────────

    /**
     * Returns a paginated, filtered list of cars.
     * Only returns cars with status="available" unless a specific status is requested.
     */
    public Page<Car> findAll(CarFilterRequest req) {
        Specification<Car> spec = buildSpec(req);
        Sort sort = parseSort(req.getSort());
        Pageable pageable = PageRequest.of(req.getPage(), req.getSize(), sort);
        return carRepository.findAll(spec, pageable);
    }

    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }

    // ── Admin mutations ──────────────────────────────────────────────

    @Transactional
    public Car create(Car car) {
        // Link images back to the car (owning side must be set)
        if (car.getImages() != null) {
            car.getImages().forEach(img -> img.setCar(car));
        }
        return carRepository.save(car);
    }

    @Transactional
    public Car update(Long id, Car updated) {
        Car existing = carRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Car not found: " + id));

        // Copy all mutable fields
        existing.setMake(updated.getMake());
        existing.setModel(updated.getModel());
        existing.setYear(updated.getYear());
        existing.setTrim(updated.getTrim());
        existing.setPrice(updated.getPrice());
        existing.setMileage(updated.getMileage());
        existing.setVin(updated.getVin());
        existing.setDescription(updated.getDescription());
        existing.setFeatures(updated.getFeatures());
        existing.setCondition(updated.getCondition());
        existing.setStatus(updated.getStatus());
        existing.setTransmission(updated.getTransmission());
        existing.setEngine(updated.getEngine());
        existing.setDriveTrain(updated.getDriveTrain());
        existing.setExteriorColor(updated.getExteriorColor());
        existing.setInteriorColor(updated.getInteriorColor());

        // Replace images collection
        existing.getImages().clear();
        if (updated.getImages() != null) {
            updated.getImages().forEach(img -> {
                img.setCar(existing);
                existing.getImages().add(img);
            });
        }

        return carRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        carRepository.deleteById(id);
    }

    // ── Private helpers ──────────────────────────────────────────────

    private Specification<Car> buildSpec(CarFilterRequest req) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (req.getMake() != null && !req.getMake().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("make")), "%" + req.getMake().toLowerCase() + "%"));
            }
            if (req.getModel() != null && !req.getModel().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("model")), "%" + req.getModel().toLowerCase() + "%"));
            }
            if (req.getYear() != null) {
                predicates.add(cb.equal(root.get("year"), req.getYear()));
            }
            if (req.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), req.getMinPrice()));
            }
            if (req.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), req.getMaxPrice()));
            }
            if (req.getMaxMileage() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("mileage"), req.getMaxMileage()));
            }
            if (req.getCondition() != null && !req.getCondition().isBlank()) {
                predicates.add(cb.equal(root.get("condition"), req.getCondition()));
            }
            // Default to showing only available cars on the public site
            String status = req.getStatus() != null ? req.getStatus() : "available";
            predicates.add(cb.equal(root.get("status"), status));

            if (req.getSearch() != null && !req.getSearch().isBlank()) {
                String pattern = "%" + req.getSearch().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("make")),  pattern),
                    cb.like(cb.lower(root.get("model")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Sort parseSort(String sortParam) {
        if (sortParam == null || sortParam.isBlank()) return Sort.by(Sort.Direction.DESC, "createdAt");
        String[] parts = sortParam.split(",");
        Sort.Direction dir = parts.length > 1 && parts[1].equalsIgnoreCase("asc")
            ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(dir, parts[0]);
    }
}
