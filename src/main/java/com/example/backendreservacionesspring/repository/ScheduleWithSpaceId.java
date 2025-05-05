package com.example.backendreservacionesspring.repository;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;

public interface ScheduleWithSpaceId {
    Long getId();

    @JsonFormat(pattern = "HH:mm")
    LocalTime getStart_time();

    @JsonFormat(pattern = "HH:mm")
    LocalTime getEnd_time();

    LocalDateTime getCreated_at();
    LocalDateTime getUpdated_at();
    Long getSpace_id();
}
