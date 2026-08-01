package com.dealership.api.controller;

import com.dealership.api.model.Car;
import com.dealership.api.security.JwtTokenProvider;
import com.dealership.api.service.CarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Admin car management endpoints with manual token validation.
 * These endpoints are permitAll() in SecurityConfig — auth is handled
 * entirely in this controller via the token query parameter.
 */
@RestController
@RequestMapping("/api/cars")
public class AdminCarsController {

    private final CarService carService;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminCarsController(CarService carService, JwtTokenProvider jwtTokenProvider) {
        this.carService = carService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private ResponseEntity<?> validateToken(String token) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "No token provided"));
        }
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid or expired token"));
        }
        String role = jwtTokenProvider.getRole(token);
        if (!"ADMIN".equals(role) && !"MANAGER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "Insufficient permissions"));
        }
        return null;
    }

    @PostMapping
    public ResponseEntity<?> createCar(@RequestParam String token, @Valid @RequestBody Car car) {
        ResponseEntity<?> err = validateToken(token);
        if (err != null) return err;
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.create(car));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCar(@PathVariable Long id, @RequestParam String token, @Valid @RequestBody Car car) {
        ResponseEntity<?> err = validateToken(token);
        if (err != null) return err;
        return ResponseEntity.ok(carService.update(id, car));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Long id, @RequestParam String token) {
        ResponseEntity<?> err = validateToken(token);
        if (err != null) return err;
        carService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
