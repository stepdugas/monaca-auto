package com.dealership.api.dto;

import java.math.BigDecimal;

/**
 * Query parameters for GET /api/inventory.
 * All fields are optional — null means "no filter".
 */
public class CarFilterRequest {
    private String  make;
    private String  model;
    private Integer year;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer maxMileage;
    private String  condition;
    private String  status;
    private String  search;      // free-text search on make/model/description
    private int     page = 0;
    private int     size = 12;
    private String  sort = "createdAt,desc";

    // Getters
    public String getMake() { return make; }
    public String getModel() { return model; }
    public Integer getYear() { return year; }
    public BigDecimal getMinPrice() { return minPrice; }
    public BigDecimal getMaxPrice() { return maxPrice; }
    public Integer getMaxMileage() { return maxMileage; }
    public String getCondition() { return condition; }
    public String getStatus() { return status; }
    public String getSearch() { return search; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public String getSort() { return sort; }

    // Setters
    public void setMake(String make) { this.make = make; }
    public void setModel(String model) { this.model = model; }
    public void setYear(Integer year) { this.year = year; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }
    public void setMaxMileage(Integer maxMileage) { this.maxMileage = maxMileage; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setStatus(String status) { this.status = status; }
    public void setSearch(String search) { this.search = search; }
    public void setPage(int page) { this.page = page; }
    public void setSize(int size) { this.size = size; }
    public void setSort(String sort) { this.sort = sort; }
}
