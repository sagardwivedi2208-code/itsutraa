package in.sd.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.sd.backend.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByTitleIgnoreCase(String title);
}