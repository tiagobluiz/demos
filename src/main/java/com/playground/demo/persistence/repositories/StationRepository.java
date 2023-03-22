package com.playground.demo.persistence.repositories;

import com.playground.demo.persistence.entities.StationEntity;
import com.playground.demo.persistence.entities.enums.StationStatus;
import jakarta.validation.constraints.NotNull;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<StationEntity, Integer> {

    @Query("SELECT s FROM StationEntity as s WHERE (:currentLocation IS NULL OR ST_DistanceSphere(s.coordinates, :currentLocation) < :radiusInMeters) AND s.status IN :statuses")
    List<StationEntity> findAllInRadius(Point currentLocation, Integer radiusInMeters, @NotNull List<StationStatus> statuses);
}
