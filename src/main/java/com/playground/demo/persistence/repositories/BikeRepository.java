package com.playground.demo.persistence.repositories;

import com.playground.demo.persistence.entities.BikeEntity;
import com.playground.demo.persistence.entities.enums.AssetStatus;
import com.playground.demo.persistence.entities.enums.BikeType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BikeRepository extends JpaRepository<BikeEntity, Integer> {

    List<BikeEntity> findByType(@NotNull BikeType type);

    List<BikeEntity> findByStatus(@NotNull AssetStatus status);

    @Query("SELECT b FROM BikeEntity b WHERE b.lastMaintenanceDate <= :date")
    List<BikeEntity> findByLastMaintenanceDateBeforeOrEqual(@NotNull LocalDate date);
}
