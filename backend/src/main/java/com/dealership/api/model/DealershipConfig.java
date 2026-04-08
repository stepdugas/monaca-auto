package com.dealership.api.model;

import jakarta.persistence.*;

/**
 * Maps to the existing {@code dealership_config} table.
 * Used by managers to update store hours, contact info, etc. at runtime.
 */
@Entity
@Table(name = "dealership_config")
public class DealershipConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String key;

    @Column(columnDefinition = "TEXT")
    private String value;

    // Constructors
    public DealershipConfig() {}

    public DealershipConfig(String key, String value) {
        this.key = key;
        this.value = value;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
