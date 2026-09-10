package in.sd.backend.controller;

import in.sd.backend.model.Course;
import in.sd.backend.model.Internship;
import in.sd.backend.model.Student;
import in.sd.backend.repository.CourseRepository;
import in.sd.backend.repository.EnrollmentRepository;
import in.sd.backend.repository.InternshipRepository;
import in.sd.backend.repository.PaymentRepository;
import in.sd.backend.repository.StudentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired private EnrollmentRepository enrollmentRepo;
    @Autowired private StudentRepository studentRepo;
    @Autowired private CourseRepository courseRepo;
    @Autowired private PaymentRepository paymentRepo;
    @Autowired private InternshipRepository internshipRepo;

    private boolean allowed(HttpSession s) {
        Student x = (Student) s.getAttribute("loggedStudent");
        return x != null && x.isAdmin();
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession s, Model m) {
        if (!allowed(s)) return "redirect:/login";
        m.addAttribute("enrollments", enrollmentRepo.findAll());
        m.addAttribute("students", studentRepo.findAll());
        m.addAttribute("courses", courseRepo.findAll());
        m.addAttribute("payments", paymentRepo.findAll());
        m.addAttribute("paidCount", paymentRepo.countByStatus("PAID"));
        m.addAttribute("studentCount", studentRepo.countByIsAdminFalse());
        m.addAttribute("courseCount", courseRepo.count());
        m.addAttribute("internshipCount", internshipRepo.count());

        Map<String, Long> sources = new LinkedHashMap<>();
        for (Student st : studentRepo.findAll()) {
            if (!st.isAdmin()) sources.merge(st.getRegistrationSource() == null ? "Website" : st.getRegistrationSource(), 1L, Long::sum);
        }
        m.addAttribute("sources", sources);
        return "admin-dashboard";
    }

    @GetMapping("/courses")
    public String courses(Model m, HttpSession s) {
        if (!allowed(s)) return "redirect:/login";
        m.addAttribute("courses", courseRepo.findAll());
        return "admin-courses";
    }

    @GetMapping("/courses/new")
    public String newCourse(Model m, HttpSession s) {
        if (!allowed(s)) return "redirect:/login";
        m.addAttribute("course", new Course());
        return "course-form";
    }

    @PostMapping("/courses/save")
    public String save(@ModelAttribute Course c, @RequestParam(value="imageFile", required=false) MultipartFile imageFile,
                       HttpSession s, Model m) {
        if (!allowed(s)) return "redirect:/login";
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                c.setImageUrl(saveImage(imageFile));
            }
            if (c.getOriginalPrice() <= 0) c.setOriginalPrice(c.getPrice());
            courseRepo.save(c);
            return "redirect:/admin/courses?success=Course+saved+successfully";
        } catch (Exception e) {
            m.addAttribute("course", c);
            m.addAttribute("error", "Unable to save course: " + e.getMessage());
            return "course-form";
        }
    }

    @GetMapping("/courses/edit/{id}")
    public String edit(@PathVariable Long id, Model m, HttpSession s) {
        if (!allowed(s)) return "redirect:/login";
        m.addAttribute("course", courseRepo.findById(id).orElseThrow());
        return "course-form";
    }

    @GetMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id, HttpSession s) {
        if (!allowed(s)) return "redirect:/login";
        if (enrollmentRepo.existsByCourseId(id) || paymentRepo.existsByCourseId(id)) {
            return "redirect:/admin/courses?error=Course+cannot+be+deleted+because+students+or+payments+are+linked+to+it";
        }
        courseRepo.deleteById(id);
        return "redirect:/admin/courses?success=Course+deleted";
    }

    @GetMapping("/students")
    public String students(Model m, HttpSession s) {
        if (!allowed(s)) return "redirect:/login";
        m.addAttribute("students", studentRepo.findAll());
        return "admin-students";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id, HttpSession s) {
        if (!allowed(s)) return "redirect:/login";
        Student st = studentRepo.findById(id).orElse(null);
        if (st != null && !st.isAdmin() && !enrollmentRepo.existsByStudentId(id) && !paymentRepo.existsByStudentId(id)) studentRepo.delete(st);
        return "redirect:/admin/students?success=Student+deleted";
    }

    @GetMapping("/internships")
    public String internships(Model m, HttpSession s) {
        if (!allowed(s)) return "redirect:/login";
        m.addAttribute("internships", internshipRepo.findAll());
        return "admin-internships";
    }

    @GetMapping("/internships/new")
    public String newInternship(Model m, HttpSession s) {
        if (!allowed(s)) return "redirect:/login";
        m.addAttribute("internship", new Internship());
        return "internship-form";
    }

    @PostMapping("/internships/save")
    public String saveInternship(@ModelAttribute Internship i, @RequestParam(value="imageFile", required=false) MultipartFile imageFile,
                                 HttpSession s) throws IOException {
        if (!allowed(s)) return "redirect:/login";
        if (imageFile != null && !imageFile.isEmpty()) i.setImageUrl(saveImage(imageFile));
        if (i.getOriginalPrice() <= 0) i.setOriginalPrice(i.getPrice());
        internshipRepo.save(i);
        return "redirect:/admin/internships?success=Internship+saved";
    }

    @GetMapping("/internships/edit/{id}")
    public String editInternship(@PathVariable Long id, Model m, HttpSession s) {
        if (!allowed(s)) return "redirect:/login";
        m.addAttribute("internship", internshipRepo.findById(id).orElseThrow());
        return "internship-form";
    }

    @GetMapping("/internships/delete/{id}")
    public String deleteInternship(@PathVariable Long id, HttpSession s) {
        if (!allowed(s)) return "redirect:/login";
        internshipRepo.deleteById(id);
        return "redirect:/admin/internships?success=Internship+deleted";
    }

    private String saveImage(MultipartFile file) throws IOException {
        String original = file.getOriginalFilename() == null ? "image" : file.getOriginalFilename();
        String clean = original.replaceAll("[^a-zA-Z0-9._-]", "_");
        String filename = System.currentTimeMillis() + "_" + clean;
        Path dir = Paths.get("uploads");
        Files.createDirectories(dir);
        Files.copy(file.getInputStream(), dir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/" + filename;
    }
}
