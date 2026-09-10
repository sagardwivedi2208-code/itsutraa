package in.sd.backend.repository;

import in.sd.backend.model.Course;
import in.sd.backend.model.Enrollment;
import in.sd.backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(Student student);
    boolean existsByStudentAndCourse(Student student, Course course);
    boolean existsByCourseId(Long courseId);
    boolean existsByStudentId(Long studentId);
    long countByCourseId(Long courseId);
}
