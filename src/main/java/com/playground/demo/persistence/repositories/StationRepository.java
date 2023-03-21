package com.playground.demo.persistence.repositories;

import com.playground.demo.persistence.entities.StationEntity;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<StationEntity, Integer> {

    @Query("SELECT s FROM StationEntity as s WHERE ST_DistanceSphere(s.coordinates, :currentLocation) < :radiusInMeters")
    List<StationEntity> findAllInRadius(Point currentLocation, int radiusInMeters);
}
