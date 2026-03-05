package br.com.victorvale.academymanagerbackend.services;

import br.com.victorvale.academymanagerbackend.dto.AttendanceDTO;
import br.com.victorvale.academymanagerbackend.mapper.AttendaceMapper;
import br.com.victorvale.academymanagerbackend.model.Attendance;
import br.com.victorvale.academymanagerbackend.model.Student;
import br.com.victorvale.academymanagerbackend.repository.AttendanceRepository;
import br.com.victorvale.academymanagerbackend.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final AttendaceMapper attendaceMapper;

    public AttendanceService(AttendanceRepository attendanceRepository, StudentRepository studentRepository, AttendaceMapper attendaceMapper) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.attendaceMapper = attendaceMapper;
    }

    public AttendanceDTO registerCheckIn(Long studentId, LocalDate classDate) {
        var student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found!"));

        LocalDate dateOfClass = (classDate != null) ? classDate : LocalDate.now();

        var attendance = Attendance.builder()
                .student(student)
                .classDate(dateOfClass)
                .checkInTime(LocalDateTime.now())
                .build();

        var savedAttendance = attendanceRepository.save(attendance);

        return attendaceMapper.toDTO(savedAttendance);
    }

    @Transactional
    public void syncClassAttendance(LocalDate classDate, List<Long> presentStudentIds) {
        List<Attendance> existingAttendances = attendanceRepository.findByClassDate(classDate);
        List<Long> existingStudentIds = existingAttendances.stream()
                .map(a -> a.getStudent().getId())
                .toList();

        List<Attendance> toRemove = existingAttendances.stream()
                .filter(a -> !presentStudentIds.contains(a.getStudent().getId()))
                .toList();
        attendanceRepository.deleteAll(toRemove);

        List<Long> toAddIds = presentStudentIds.stream()
                .filter(id -> !existingStudentIds.contains(id))
                .toList();

        if (!toAddIds.isEmpty()) {
            List<Student> studentsToAdd = studentRepository.findAllById(toAddIds);

            List<Attendance> newAttendances = studentsToAdd.stream()
                    .map(student -> Attendance.builder()
                            .student(student)
                            .classDate(classDate)
                            .checkInTime(LocalDateTime.now())
                            .build())
                    .toList();

            attendanceRepository.saveAll(newAttendances);
        }
    }

    public List<AttendanceDTO> getStudentHistory(Long studentId) {
        return attendanceRepository.findByStudentIdOrderByCheckInTimeDesc(studentId).stream()
                .map(attendaceMapper::toDTO)
                .toList();
    }

    public List<AttendanceDTO> getAttendancesByDate(LocalDate classDate) {
        return attendanceRepository.findByClassDate(classDate).stream()
                .map(attendaceMapper::toDTO)
                .toList();
    }
}
