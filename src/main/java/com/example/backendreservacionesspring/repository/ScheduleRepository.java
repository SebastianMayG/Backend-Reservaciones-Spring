package com.example.backendreservacionesspring.repository;


import com.example.backendreservacionesspring.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(value = "SELECT * FROM schedules WHERE space_id = :spaceId", nativeQuery = true)
    List<Schedule> findSchedulesBySpaceIdNative(@Param("spaceId") Long spaceId);
}
