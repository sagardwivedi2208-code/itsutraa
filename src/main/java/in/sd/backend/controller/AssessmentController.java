package in.sd.backend.controller;

import in.sd.backend.model.*;
import in.sd.backend.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
public class AssessmentController {
 private final CourseRepository courseRepo; private final EnrollmentRepository enrollmentRepo; private final QuizQuestionRepository questionRepo; private final CertificateRepository certificateRepo; private final CertificatePdfService pdfService;
 public AssessmentController(CourseRepository c,EnrollmentRepository e,QuizQuestionRepository q,CertificateRepository cert,CertificatePdfService p){courseRepo=c;enrollmentRepo=e;questionRepo=q;certificateRepo=cert;pdfService=p;}
 private Student student(HttpSession s){return (Student)s.getAttribute("loggedStudent");}
 private boolean enrolled(Student s,Course c){return s!=null && enrollmentRepo.findByStudent(s).stream().anyMatch(e->e.getCourse()!=null && e.getCourse().getId().equals(c.getId()) && e.isPaymentStatus());}
 @GetMapping("/assessment/{courseId}") public String quiz(@PathVariable Long courseId,HttpSession session,Model m){Student s=student(session); Course c=courseRepo.findById(courseId).orElse(null); if(s==null)return "redirect:/login"; if(c==null)return "redirect:/courses"; if(!enrolled(s,c))return "redirect:/course-details/"+courseId; Optional<Certificate> old=certificateRepo.findByStudentAndCourse(s,c); if(old.isPresent()){m.addAttribute("certificate",old.get());m.addAttribute("course",c);return "certificate-success";} List<QuizQuestion> qs=questionRepo.findByCourseOrderByIdAsc(c);m.addAttribute("course",c);m.addAttribute("questions",qs);return "assessment";}
 @PostMapping("/assessment/{courseId}/submit") public String submit(@PathVariable Long courseId,@RequestParam Map<String,String> answers,HttpSession session,Model m){Student s=student(session);Course c=courseRepo.findById(courseId).orElse(null);if(s==null)return "redirect:/login";if(c==null||!enrolled(s,c))return "redirect:/courses";List<QuizQuestion> qs=questionRepo.findByCourseOrderByIdAsc(c);int score=0;for(QuizQuestion q:qs){if(q.getCorrectOption().equalsIgnoreCase(answers.get("q"+q.getId())))score++;}m.addAttribute("score",score);m.addAttribute("total",qs.size());m.addAttribute("course",c);if(score>=7){Certificate cert=certificateRepo.findByStudentAndCourse(s,c).orElseGet(Certificate::new);if(cert.getCertificateId()==null){cert.setCertificateId("ITS-"+java.time.Year.now().getValue()+"-"+UUID.randomUUID().toString().substring(0,8).toUpperCase());}cert.setStudent(s);cert.setCourse(c);cert.setScore(score);cert.setTotalQuestions(qs.size());cert.setIssuedAt(java.time.LocalDateTime.now());certificateRepo.save(cert);m.addAttribute("certificate",cert);return "certificate-success";}return "assessment-result";}
 @GetMapping("/certificate/verify/{certificateId}") public String verify(@PathVariable String certificateId,Model m){certificateRepo.findByCertificateId(certificateId).ifPresent(c->m.addAttribute("certificate",c));m.addAttribute("certificateId",certificateId);return "certificate-verify";}
 @GetMapping("/certificate/download/{certificateId}") @ResponseBody public org.springframework.http.ResponseEntity<byte[]> download(@PathVariable String certificateId){Optional<Certificate> cert=certificateRepo.findByCertificateId(certificateId);if(cert.isEmpty())return org.springframework.http.ResponseEntity.notFound().build();byte[] pdf=pdfService.create(cert.get());return org.springframework.http.ResponseEntity.ok().header("Content-Disposition","attachment; filename=IT-SUTRAA-Certificate-"+certificateId+".pdf").contentType(org.springframework.http.MediaType.APPLICATION_PDF).body(pdf);}
}
