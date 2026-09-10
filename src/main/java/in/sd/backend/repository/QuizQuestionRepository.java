package in.sd.backend.repository;
import in.sd.backend.model.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion,Long>{ List<QuizQuestion> findByCourseOrderByIdAsc(Course course); long countByCourse(Course course); }
