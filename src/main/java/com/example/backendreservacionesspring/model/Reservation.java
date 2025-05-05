package com.example.backendreservacionesspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "reservations")
@Getter @Setter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @JsonProperty("start_time")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @JsonProperty("end_time")
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "create_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "space_id", nullable = false)
    @JsonIgnore
    private Space space;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    // Campos adicionales para los IDs
    @Transient
    public Long getSpace_id() {
        return space != null ? space.getId() : null;
    }

    @Transient
    public Long getUser_id() {
        return user != null ? user.getId() : null;
    }
}
