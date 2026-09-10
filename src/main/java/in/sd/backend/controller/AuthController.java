package in.sd.backend.controller;

import in.sd.backend.model.PasswordResetToken;
import in.sd.backend.model.Student;
import in.sd.backend.repository.PasswordResetTokenRepository;
import in.sd.backend.repository.StudentRepository;
import in.sd.backend.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class AuthController {
 @Autowired private StudentRepository studentRepo;
 @Autowired private PasswordEncoder passwordEncoder;
 @Autowired private PasswordResetTokenRepository resetRepo;
 @Autowired private EmailService emailService;
 @Value("${app.base-url:http://localhost:8080}") private String baseUrl;
 @Value("${app.environment:development}") private String environment;

 @GetMapping("/register") public String showRegisterForm(Model model){model.addAttribute("student",new Student());return "register";}
 @PostMapping("/register") public String registerStudent(@ModelAttribute Student student, @RequestParam(value="source", defaultValue="Website") String source, Model model){
  if(studentRepo.findByEmail(student.getEmail())!=null){model.addAttribute("error","Email already registered");return "register";}
  student.setPassword(passwordEncoder.encode(student.getPassword())); student.setAdmin(false); student.setRegistrationSource(source); studentRepo.save(student);
  model.addAttribute("message","Registration successful! Your account has been created. Please login."); return "login";
 }
 @GetMapping("/login") public String showLoginForm(){return "login";}
 @PostMapping("/login") public String loginStudent(@RequestParam String email,@RequestParam String password,HttpSession session,Model model){
  Student student=studentRepo.findByEmail(email);
  if(student==null || !passwordEncoder.matches(password,student.getPassword())){model.addAttribute("error","Invalid email or password");return "login";}
  session.setAttribute("loggedStudent",student);
  return student.isAdmin()?"redirect:/admin/dashboard":"redirect:/profile";
 }

 @GetMapping("/forgot-password")
 public String forgotPasswordPage(){ return "forgot-password"; }

 @PostMapping("/forgot-password")
 public String sendResetLink(@RequestParam String email, Model model) {
  // Always show the same message so an attacker cannot discover registered emails.
  Student student = studentRepo.findByEmail(email.trim());
  if (student != null) {
      resetRepo.deleteByStudentId(student.getId());
      PasswordResetToken token = new PasswordResetToken();
      token.setToken(UUID.randomUUID().toString().replace("-", ""));
      token.setStudent(student);
      token.setExpiresAt(LocalDateTime.now().plusMinutes(15));
      resetRepo.save(token);
      String resetUrl = baseUrl.replaceAll("/$", "") + "/reset-password?token=" + token.getToken();
      try {
          emailService.sendPasswordReset(student.getEmail(), student.getName(), resetUrl);
      } catch (Exception ex) {
          System.err.println("[IT SUTRAA] Password reset email could not be sent. Configure spring.mail.* settings.");
          if ("development".equalsIgnoreCase(environment)) {
          System.err.println("[LOCAL RESET LINK - DEVELOPMENT ONLY] " + resetUrl);
      }
      }
  }
  model.addAttribute("message","If an account exists for that email, a password reset link has been sent. Please check your inbox.");
  return "forgot-password";
 }

 @GetMapping("/reset-password")
 public String resetPasswordPage(@RequestParam String token, Model model) {
  PasswordResetToken reset = resetRepo.findByToken(token).orElse(null);
  if (reset == null || reset.isUsed() || reset.getExpiresAt().isBefore(LocalDateTime.now())) {
      model.addAttribute("error","This password reset link is invalid or expired. Please request a new one.");
      return "forgot-password";
  }
  model.addAttribute("token", token);
  return "reset-password";
 }

 @PostMapping("/reset-password")
 public String resetPassword(@RequestParam String token, @RequestParam String password, @RequestParam String confirmPassword, Model model) {
  PasswordResetToken reset = resetRepo.findByToken(token).orElse(null);
  if (reset == null || reset.isUsed() || reset.getExpiresAt().isBefore(LocalDateTime.now())) {
      model.addAttribute("error","This password reset link is invalid or expired. Please request a new one.");
      return "forgot-password";
  }
  if (password == null || password.length() < 8) {
      model.addAttribute("error","Password must be at least 8 characters."); model.addAttribute("token", token); return "reset-password";
  }
  if (!password.equals(confirmPassword)) {
      model.addAttribute("error","Passwords do not match."); model.addAttribute("token", token); return "reset-password";
  }
  Student student = reset.getStudent();
  student.setPassword(passwordEncoder.encode(password));
  studentRepo.save(student);
  reset.setUsed(true);
  resetRepo.save(reset);
  model.addAttribute("message","Password changed successfully. Please login with your new password.");
  return "login";
 }

 @GetMapping("/logout") public String logout(HttpSession session){session.invalidate();return "redirect:/login";}
}
