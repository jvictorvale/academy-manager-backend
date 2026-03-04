package br.com.victorvale.academymanagerbackend.repository;

import br.com.victorvale.academymanagerbackend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
