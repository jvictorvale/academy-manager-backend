package br.com.victorvale.academymanagerbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendances")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attendance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "class_date", nullable = false)
    private LocalDate classDate;

    @CreationTimestamp
    @Column(name = "check_in_time", nullable = false, updatable = false)
    private LocalDateTime checkInTime;
}
