package in.sd.backend.controller;
import in.sd.backend.model.*; import in.sd.backend.repository.*; import jakarta.servlet.http.HttpSession; import org.springframework.beans.factory.annotation.Autowired; import org.springframework.stereotype.Controller; import org.springframework.web.bind.annotation.*;
@Controller public class EnrollmentController { @Autowired private StudentRepository studentRepo; @Autowired private CourseRepository courseRepo; @Autowired private EnrollmentRepository enrollmentRepo;
 @PostMapping("/enroll/{courseId}") public String oldEnroll(@PathVariable Long courseId,HttpSession session){ return "redirect:/payment/start/"+courseId; }
}
