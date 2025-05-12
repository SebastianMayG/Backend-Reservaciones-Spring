package com.example.backendreservacionesspring.repository;


import com.example.backendreservacionesspring.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(value = "SELECT * FROM schedules WHERE space_id = :spaceId", nativeQuery = true)
    List<Schedule> findSchedulesBySpaceIdNative(@Param("spaceId") Long spaceId);

    // Consulta para obtener todos los horarios con space_id
    @Query("SELECT s.id as id, s.start_time as start_time, s.end_time as end_time, " +
            "s.createdAt as created_at, s.updatedAt as updated_at, " +
            "s.space.id as space_id FROM Schedule s")
    List<ScheduleWithSpaceId> findAllWithSpaceId();

    // Consulta para horarios por espacio (con space_id)
    @Query("SELECT s.id as id, s.start_time as start_time, s.end_time as end_time, " +
            "s.createdAt as created_at, s.updatedAt as updated_at, " +
            "s.space.id as space_id FROM Schedule s WHERE s.space.id = :spaceId")
    List<ScheduleWithSpaceId> findSchedulesBySpaceId(@Param("spaceId") Long spaceId);

    void deleteBySpaceId(Long id);
}
