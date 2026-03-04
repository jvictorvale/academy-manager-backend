package br.com.victorvale.academymanagerbackend.services;

import br.com.victorvale.academymanagerbackend.dto.StudentDTO;
import br.com.victorvale.academymanagerbackend.mapper.StudentMapper;
import br.com.victorvale.academymanagerbackend.model.Student;
import br.com.victorvale.academymanagerbackend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

        var dto = studentMapper.toDTO(student);
        return dto;
    }

    public List<Student> findAll(){
        var students = studentRepository.findAll();

        forEach.
    }
}
