package in.sd.backend.controller;
import in.sd.backend.model.*; import in.sd.backend.repository.*; import jakarta.servlet.http.HttpSession; import org.springframework.stereotype.Controller; import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller public class StudentController { private final EnrollmentRepository enrollmentRepo; private final PaymentRepository paymentRepo; private final CertificateRepository certificateRepo;
 public StudentController(EnrollmentRepository e,PaymentRepository p,CertificateRepository c){enrollmentRepo=e;paymentRepo=p;certificateRepo=c;}
 @GetMapping("/profile") public String profile(HttpSession session,Model model){Student s=(Student)session.getAttribute("loggedStudent");if(s==null)return "redirect:/login";model.addAttribute("student",s);model.addAttribute("enrollments",enrollmentRepo.findByStudent(s));model.addAttribute("payments",paymentRepo.findByStudentOrderByCreatedAtDesc(s));model.addAttribute("certificates",certificateRepo.findByStudentOrderByIssuedAtDesc(s));return "profile";}
}
