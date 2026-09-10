package in.sd.backend.repository;

import in.sd.backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByEmail(String email);
    long countByIsAdminFalse();
}
