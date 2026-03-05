package br.com.victorvale.academymanagerbackend.mapper;

import br.com.victorvale.academymanagerbackend.dto.AttendanceDTO;
import br.com.victorvale.academymanagerbackend.model.Attendance;
import br.com.victorvale.academymanagerbackend.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AttendaceMapper {
    public Attendance toEntity(AttendanceDTO dto, Student student){
        return Attendance.builder()
                .student(student)
                .classDate(dto.getClassDate())
                .checkInTime(LocalDateTime.now())
                .build();
    }

    public AttendanceDTO toDTO(Attendance entity){
        return AttendanceDTO.builder()
                .id(entity.getId())
                .studentId(entity.getStudent().getId())
                .studentName(entity.getStudent().getName())
                .classDate(entity.getClassDate())
                .checkInTime(entity.getCheckInTime())
                .build();
    }
}
