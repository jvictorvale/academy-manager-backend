package br.com.victorvale.academymanagerbackend.mapper;

import br.com.victorvale.academymanagerbackend.dto.StudentDTO;
import br.com.victorvale.academymanagerbackend.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentMapper {
    public Student toEntity(StudentDTO dto){
        return Student.builder()
                .name(dto.getName())
                .age(dto.getAge())
                .belt(dto.getBelt())
                .weight(dto.getWeight())
                .height(dto.getHeight())
                .isActive(dto.isActive())
                .build();
    }

    public StudentDTO toDTO(Student entity){
        return StudentDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .age(entity.getAge())
                .belt(entity.getBelt())
                .weight(entity.getWeight())
                .height(entity.getHeight())
                .isActive(entity.isActive())
                .build();
    }

    public void updateEntityFromDTO(StudentDTO dto, Student entity) {
        entity.setName(dto.getName());
        entity.setAge(dto.getAge());
        entity.setBelt(dto.getBelt());
        entity.setWeight(dto.getWeight());
        entity.setHeight(dto.getHeight());
    }
}
