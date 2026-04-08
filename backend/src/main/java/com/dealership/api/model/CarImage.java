package com.dealership.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * JPA entity for the {@code car_images} table.
 * Each row is one photo for a car (Cloudinary URL).
 */
@Entity
@Table(name = "car_images")
public class CarImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Back-reference to the parent car — excluded from JSON to avoid cycles. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    @JsonIgnore
    private Car car;

    @Column(nullable = false)
    private String imageUrl;

    private boolean isPrimary = false;

    private int sortOrder = 0;

    // Constructor
    public CarImage() {}

    // Getters
    public Long getId() { return id; }
    public Car getCar() { return car; }
    public String getImageUrl() { return imageUrl; }
    public boolean isPrimary() { return isPrimary; }
    public int getSortOrder() { return sortOrder; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setCar(Car car) { this.car = car; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setPrimary(boolean primary) { isPrimary = primary; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
