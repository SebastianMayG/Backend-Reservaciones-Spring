package com.example.backendreservacionesspring.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lists")
@Getter @Setter
public class List {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_list")
    private LocalDateTime dateList;

    @Column(name = "create_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    private LocalDateTime updateAt;

    @ManyToMany
    @JoinTable(
            name = "student_list",
            joinColumns = @JoinColumn(name = "id_list"), // FK de List
            inverseJoinColumns = @JoinColumn(name = "id_student") // FK de Student
    )
    private Set<Student> students = new HashSet<>();
}
