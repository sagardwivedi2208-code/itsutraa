package in.sd.backend.repository;

import in.sd.backend.model.Payment;
import in.sd.backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    long countByStatus(String status);
    boolean existsByCourseId(Long courseId);
    boolean existsByStudentId(Long studentId);
    List<Payment> findByStudentOrderByCreatedAtDesc(Student student);
}
