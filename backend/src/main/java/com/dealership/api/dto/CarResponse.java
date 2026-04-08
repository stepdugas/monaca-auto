package com.dealership.api.dto;

import com.dealership.api.model.Car;
import com.dealership.api.model.CarImage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for returning car data in API responses.
 * Includes KBB valuation information.
 */
public class CarResponse {

    private Long id;
    private String make;
    private String model;
    private Integer year;
    private String trim;
    private BigDecimal price;
    private Integer mileage;
    private String vin;
    private String description;
    private List<String> features;
    private String condition;
    private String status;
    private String transmission;
    private String engine;
    private String driveTrain;
    private String exteriorColor;
    private String interiorColor;
    private List<CarImageResponse> images;
    private BigDecimal kbbValue;
    private LocalDateTime kbbLastUpdated;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor
    public CarResponse() {}

    /**
     * Factory method to create a CarResponse from a Car entity.
     */
    public static CarResponse from(Car car) {
        CarResponse response = new CarResponse();
        response.id = car.getId();
        response.make = car.getMake();
        response.model = car.getModel();
        response.year = car.getYear();
        response.trim = car.getTrim();
        response.price = car.getPrice();
        response.mileage = car.getMileage();
        response.vin = car.getVin();
        response.description = car.getDescription();
        response.features = new ArrayList<>(car.getFeatures());
        response.condition = car.getCondition();
        response.status = car.getStatus();
        response.transmission = car.getTransmission();
        response.engine = car.getEngine();
        response.driveTrain = car.getDriveTrain();
        response.exteriorColor = car.getExteriorColor();
        response.interiorColor = car.getInteriorColor();
        response.images = car.getImages().stream()
            .map(CarImageResponse::from)
            .collect(Collectors.toList());
        response.kbbValue = car.getKbbValue();
        response.kbbLastUpdated = car.getKbbLastUpdated();
        response.createdAt = car.getCreatedAt();
        response.updatedAt = car.getUpdatedAt();
        return response;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getTrim() { return trim; }
    public void setTrim(String trim) { this.trim = trim; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getMileage() { return mileage; }
    public void setMileage(Integer mileage) { this.mileage = mileage; }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getFeatures() { return features; }
    public void setFeatures(List<String> features) { this.features = features; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }

    public String getEngine() { return engine; }
    public void setEngine(String engine) { this.engine = engine; }

    public String getDriveTrain() { return driveTrain; }
    public void setDriveTrain(String driveTrain) { this.driveTrain = driveTrain; }

    public String getExteriorColor() { return exteriorColor; }
    public void setExteriorColor(String exteriorColor) { this.exteriorColor = exteriorColor; }

    public String getInteriorColor() { return interiorColor; }
    public void setInteriorColor(String interiorColor) { this.interiorColor = interiorColor; }

    public List<CarImageResponse> getImages() { return images; }
    public void setImages(List<CarImageResponse> images) { this.images = images; }

    public BigDecimal getKbbValue() { return kbbValue; }
    public void setKbbValue(BigDecimal kbbValue) { this.kbbValue = kbbValue; }

    public LocalDateTime getKbbLastUpdated() { return kbbLastUpdated; }
    public void setKbbLastUpdated(LocalDateTime kbbLastUpdated) { this.kbbLastUpdated = kbbLastUpdated; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    /**
     * Nested DTO for car images.
     */
    public static class CarImageResponse {
        private Long id;
        private String imageUrl;
        private Integer sortOrder;

        public static CarImageResponse from(CarImage image) {
            CarImageResponse response = new CarImageResponse();
            response.id = image.getId();
            response.imageUrl = image.getImageUrl();
            response.sortOrder = image.getSortOrder();
            return response;
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

        public Integer getSortOrder() { return sortOrder; }
        public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    }
}
