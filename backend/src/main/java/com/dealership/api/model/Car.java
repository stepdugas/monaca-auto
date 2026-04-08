package com.dealership.api.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity that maps to the {@code cars} table.
 */
@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer year;

    private String trim;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    private Integer mileage;

    @Column(unique = true, length = 17)
    private String vin;

    @Column(columnDefinition = "TEXT")
    private String description;

    /** JSON array of feature strings, e.g. ["Backup Camera","Sunroof"] */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> features = new ArrayList<>();

    /** "New", "Used", "Certified" */
    private String condition;

    /** "available", "pending", "sold" */
    @Column(nullable = false)
    private String status = "available";

    private String transmission;
    private String engine;
    private String driveTrain;
    private String exteriorColor;
    private String interiorColor;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("sortOrder ASC")
    private List<CarImage> images = new ArrayList<>();

    /** KBB value in dollars (nullable if not yet fetched or VIN invalid) */
    @Column(precision = 12, scale = 2)
    private BigDecimal kbbValue;

    /** Last time KBB value was fetched/updated */
    private LocalDateTime kbbLastUpdated;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Constructor
    public Car() {}

    // Getters
    public Long getId() { return id; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public Integer getYear() { return year; }
    public String getTrim() { return trim; }
    public BigDecimal getPrice() { return price; }
    public Integer getMileage() { return mileage; }
    public String getVin() { return vin; }
    public String getDescription() { return description; }
    public List<String> getFeatures() { return features; }
    public String getCondition() { return condition; }
    public String getStatus() { return status; }
    public String getTransmission() { return transmission; }
    public String getEngine() { return engine; }
    public String getDriveTrain() { return driveTrain; }
    public String getExteriorColor() { return exteriorColor; }
    public String getInteriorColor() { return interiorColor; }
    public List<CarImage> getImages() { return images; }
    public BigDecimal getKbbValue() { return kbbValue; }
    public LocalDateTime getKbbLastUpdated() { return kbbLastUpdated; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setMake(String make) { this.make = make; }
    public void setModel(String model) { this.model = model; }
    public void setYear(Integer year) { this.year = year; }
    public void setTrim(String trim) { this.trim = trim; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setMileage(Integer mileage) { this.mileage = mileage; }
    public void setVin(String vin) { this.vin = vin; }
    public void setDescription(String description) { this.description = description; }
    public void setFeatures(List<String> features) { this.features = features; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setStatus(String status) { this.status = status; }
    public void setTransmission(String transmission) { this.transmission = transmission; }
    public void setEngine(String engine) { this.engine = engine; }
    public void setDriveTrain(String driveTrain) { this.driveTrain = driveTrain; }
    public void setExteriorColor(String exteriorColor) { this.exteriorColor = exteriorColor; }
    public void setInteriorColor(String interiorColor) { this.interiorColor = interiorColor; }
    public void setImages(List<CarImage> images) { this.images = images; }
    public void setKbbValue(BigDecimal kbbValue) { this.kbbValue = kbbValue; }
    public void setKbbLastUpdated(LocalDateTime kbbLastUpdated) { this.kbbLastUpdated = kbbLastUpdated; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
