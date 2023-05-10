package com.playground.demo.persistence.repositories;

import com.playground.demo.persistence.entities.DockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DockRepository extends JpaRepository<DockEntity, Integer> {
}
