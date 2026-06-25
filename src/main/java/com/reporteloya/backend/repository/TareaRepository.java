package com.reporteloya.backend.repository;

import com.reporteloya.backend.entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, UUID> {
}