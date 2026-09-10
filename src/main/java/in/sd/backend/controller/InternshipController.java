package in.sd.backend.controller;

import in.sd.backend.repository.InternshipRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/internships")
public class InternshipController {
    private final InternshipRepository repository;
    public InternshipController(InternshipRepository repository){this.repository=repository;}
    @GetMapping public String internships(Model model){model.addAttribute("internships", repository.findAll()); return "internships";}
    @GetMapping("/{id}") public String details(@PathVariable Long id, Model model){
        return repository.findById(id).map(i->{model.addAttribute("internship",i);return "internship-details";}).orElse("redirect:/internships");
    }
}
