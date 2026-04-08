package com.dealership.api.service;

import com.dealership.api.model.Car;
import com.dealership.api.repository.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service for managing car valuations, including KBB price fetching and updates.
 */
@Service
public class ValuationService {

    private static final Logger logger = LoggerFactory.getLogger(ValuationService.class);

    private final CarRepository carRepository;
    private final KBBClient kbbClient;

    public ValuationService(CarRepository carRepository, KBBClient kbbClient) {
        this.carRepository = carRepository;
        this.kbbClient = kbbClient;
    }

    /**
     * Refresh KBB valuation for a specific car by ID.
     *
     * @param carId The ID of the car to refresh
     * @return Updated Car object with new KBB value
     * @throws RuntimeException if car not found
     */
    @Transactional
    public Car refreshKBBValuation(Long carId) {
        Car car = carRepository.findById(carId)
            .orElseThrow(() -> new RuntimeException("Car not found: " + carId));

        return refreshKBBValuation(car);
    }

    /**
     * Refresh KBB valuation for a car entity.
     *
     * @param car The car entity to update
     * @return Updated car with new KBB value and timestamp
     */
    @Transactional
    public Car refreshKBBValuation(Car car) {
        if (car.getVin() == null || car.getVin().isEmpty()) {
            logger.warn("Cannot refresh KBB: Car {} has no VIN", car.getId());
            return car;
        }

        Optional<BigDecimal> kbbValue = kbbClient.fetchKBBValueByVin(car.getVin());

        if (kbbValue.isPresent()) {
            car.setKbbValue(kbbValue.get());
            car.setKbbLastUpdated(LocalDateTime.now());
            logger.info("Updated KBB value for car {}: ${}", car.getId(), kbbValue.get());
        } else {
            logger.warn("Failed to fetch KBB value for car {} (VIN: {})", car.getId(), car.getVin());
            // Still update the timestamp to avoid repeated failed attempts immediately
            car.setKbbLastUpdated(LocalDateTime.now());
        }

        return carRepository.save(car);
    }

    /**
     * Batch refresh KBB valuations for all available cars.
     * Use with caution — will make many API calls.
     *
     * @return Number of cars successfully updated
     */
    @Transactional
    public int refreshAllKBBValuations() {
        var cars = carRepository.findAll();
        int successCount = 0;

        for (Car car : cars) {
            try {
                refreshKBBValuation(car);
                successCount++;
            } catch (Exception e) {
                logger.error("Error refreshing KBB for car {}: {}", car.getId(), e.getMessage());
            }
        }

        logger.info("Batch KBB refresh completed: {} cars processed", successCount);
        return successCount;
    }

    /**
     * Check if a car's KBB value needs refreshing (older than specified threshold).
     *
     * @param car The car to check
     * @param maxAgeHours Maximum age of KBB data in hours
     * @return true if KBB needs refresh, false otherwise
     */
    public boolean needsKBBRefresh(Car car, int maxAgeHours) {
        if (car.getKbbLastUpdated() == null) {
            return true;
        }

        LocalDateTime threshold = LocalDateTime.now().minusHours(maxAgeHours);
        return car.getKbbLastUpdated().isBefore(threshold);
    }
}
