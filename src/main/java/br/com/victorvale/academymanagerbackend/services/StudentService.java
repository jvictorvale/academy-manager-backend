package br.com.victorvale.academymanagerbackend.services;

import br.com.victorvale.academymanagerbackend.dto.StudentDTO;
import br.com.victorvale.academymanagerbackend.exception.RequiredObjectIsNullException;
import br.com.victorvale.academymanagerbackend.exception.ResourceNotFoundException;
import br.com.victorvale.academymanagerbackend.mapper.StudentMapper;
import br.com.victorvale.academymanagerbackend.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    public StudentDTO findById(Long id){
        var student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        return studentMapper.toDTO(student);
    }

    public List<StudentDTO> findAll(){
        var students = studentRepository.findAll();

        return students.stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    public StudentDTO create(StudentDTO studentDTO){
        if (studentDTO == null) throw new RequiredObjectIsNullException();

        var student = studentMapper.toEntity(studentDTO);

        return studentMapper.toDTO(studentRepository.save(student));
    }

    public StudentDTO update(StudentDTO studentDTO){
        if (studentDTO == null) throw new RequiredObjectIsNullException();

        var student = studentRepository.findById(studentDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentDTO.getId()));

        studentMapper.updateEntityFromDTO(studentDTO, student);

        return studentMapper.toDTO(studentRepository.save(student));
    }

    public void softDelete(Long id){
        var student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        student.setActive(false);
        studentRepository.save(student);
    }

    public void Activate(Long id){
        var student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        student.setActive(true);
        studentRepository.save(student);
    }
}
