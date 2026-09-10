package in.sd.backend.controller;
import in.sd.backend.model.*; import in.sd.backend.repository.*; import jakarta.servlet.http.HttpSession; import org.springframework.stereotype.Controller; import org.springframework.ui.Model; import org.springframework.web.bind.annotation.*;
@Controller public class CourseController {
 private final CourseRepository courseRepo; private final EnrollmentRepository enrollmentRepo; private final CertificateRepository certificateRepo;
 public CourseController(CourseRepository c,EnrollmentRepository e,CertificateRepository cert){courseRepo=c;enrollmentRepo=e;certificateRepo=cert;}
 @GetMapping("/courses") public String viewCourses(Model m){m.addAttribute("courses",courseRepo.findAll().stream().filter(c->!"Summer Internship".equalsIgnoreCase(c.getTitle())).toList());return "courses";}
 @GetMapping("/course-details/{id}") public String details(@PathVariable Long id,Model m,HttpSession session){Course c=courseRepo.findById(id).orElse(null);if(c==null||"Summer Internship".equalsIgnoreCase(c.getTitle()))return "redirect:/courses";Student s=(Student)session.getAttribute("loggedStudent");boolean enrolled=s!=null&&enrollmentRepo.findByStudent(s).stream().anyMatch(e->e.getCourse()!=null&&e.getCourse().getId().equals(id)&&e.isPaymentStatus());m.addAttribute("course",c);m.addAttribute("enrolled",enrolled);if(s!=null)certificateRepo.findByStudentAndCourse(s,c).ifPresent(x->m.addAttribute("certificate",x));return "course-details";}
}
