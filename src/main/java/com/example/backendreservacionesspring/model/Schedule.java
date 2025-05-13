package com.example.backendreservacionesspring.model;

import com.example.backendreservacionesspring.LocalTimeSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedules")
@Getter @Setter
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @NotNull(message = "El startTime es obligatorio")
    @Column(name = "start_time", nullable = false, columnDefinition = "TIME(0)")
    private LocalTime start_time;

    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @NotNull(message = "El endTime es obligatorio")
    @Column(name = "end_time", nullable = false, columnDefinition = "TIME(0)")
    private LocalTime end_time;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @NotNull(message = "El espacio es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "space_id", nullable = false)
    @JsonIgnore
    private Space space;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_space", nullable = false)
//    private Space space;
}