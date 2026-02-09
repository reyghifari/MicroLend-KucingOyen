package com.kucingoyen.microlend.repository;

import com.kucingoyen.microlend.model.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for SystemConfig entity.
 * Provides access to system configuration stored in the database.
 */
@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, String> {

    /**
     * Find configuration by key.
     * 
     * @param key The configuration key
     * @return Optional containing the SystemConfig if found
     */
    Optional<SystemConfig> findByKey(String key);
}
