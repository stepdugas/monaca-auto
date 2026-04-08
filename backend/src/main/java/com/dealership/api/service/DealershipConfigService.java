package com.dealership.api.service;

import com.dealership.api.model.DealershipConfig;
import com.dealership.api.repository.DealershipConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for reading and updating the dealership_config key/value table.
 * Used by managers to update store hours, contact info, etc.
 */
@Service
public class DealershipConfigService {

    private final DealershipConfigRepository repo;

    public DealershipConfigService(DealershipConfigRepository repo) {
        this.repo = repo;
    }

    /** Return all config entries as a flat map. */
    public Map<String, String> getAll() {
        return repo.findAllByOrderByKeyAsc().stream()
            .collect(Collectors.toMap(DealershipConfig::getKey, c -> c.getValue() != null ? c.getValue() : ""));
    }

    /** Return a single setting value, or null if not found. */
    public String get(String key) {
        return repo.findByKey(key).map(DealershipConfig::getValue).orElse(null);
    }

    /** Upsert a single setting. */
    @Transactional
    public DealershipConfig set(String key, String value) {
        DealershipConfig config = repo.findByKey(key)
            .orElseGet(() -> new DealershipConfig(key, null));
        config.setValue(value);
        return repo.save(config);
    }

    /** Bulk upsert — applies every key/value pair in the map. */
    @Transactional
    public Map<String, String> setAll(Map<String, String> updates) {
        updates.forEach(this::set);
        return getAll();
    }

    /** Return all config rows as a list (for admin display). */
    public List<DealershipConfig> list() {
        return repo.findAllByOrderByKeyAsc();
    }
}
