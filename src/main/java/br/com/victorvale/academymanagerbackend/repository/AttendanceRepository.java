package br.com.victorvale.academymanagerbackend.repository;

import br.com.victorvale.academymanagerbackend.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentIdOrderByCheckInTimeDesc(Long studentId);

    List<Attendance> findByClassDate(LocalDate classDate);
}
