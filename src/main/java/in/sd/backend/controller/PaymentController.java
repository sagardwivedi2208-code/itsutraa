package in.sd.backend.controller;

import in.sd.backend.model.*;
import in.sd.backend.repository.*;
import in.sd.backend.service.RazorpayService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
public class PaymentController {
 @Autowired private CourseRepository courseRepo; @Autowired private EnrollmentRepository enrollmentRepo;
 @Autowired private PaymentRepository paymentRepo; @Autowired private RazorpayService razorpay;
 @GetMapping("/payment/start/{courseId}") public String start(@PathVariable Long courseId,HttpSession session,Model model){
  Student s=(Student)session.getAttribute("loggedStudent"); if(s==null)return "redirect:/login";
  Course c=courseRepo.findById(courseId).orElse(null); if(c==null)return "redirect:/courses";
  if(enrollmentRepo.existsByStudentAndCourse(s,c)) return "redirect:/profile";
  try { Map<String,Object> order=razorpay.createOrder(c.getPrice()*100,"IT-SUTRAA-"+s.getId()+"-"+c.getId());
   model.addAttribute("course",c); model.addAttribute("orderId",order.get("id")); model.addAttribute("keyId",razorpay.getKeyId()); return "payment";
  } catch(Exception e){ return "redirect:/course-details/"+courseId+"?paymentError=1"; }
 }
 @PostMapping("/payment/verify") public String verify(@RequestParam Long courseId,@RequestParam String razorpay_order_id,@RequestParam String razorpay_payment_id,@RequestParam String razorpay_signature,HttpSession session,Model model){
  Student s=(Student)session.getAttribute("loggedStudent"); if(s==null)return "redirect:/login";
  Course c=courseRepo.findById(courseId).orElse(null); if(c==null)return "redirect:/courses";
  try{
   Payment p=new Payment(); p.setStudent(s);p.setCourse(c);p.setAmount(c.getPrice());p.setRazorpayOrderId(razorpay_order_id);p.setRazorpayPaymentId(razorpay_payment_id);p.setRazorpaySignature(razorpay_signature);
   if(razorpay.verify(razorpay_order_id,razorpay_payment_id,razorpay_signature)){
    p.setStatus("PAID"); paymentRepo.save(p);
    if(!enrollmentRepo.existsByStudentAndCourse(s,c)){Enrollment e=new Enrollment();e.setStudent(s);e.setCourse(c);e.setPaymentStatus(true);e.setPaymentId(razorpay_payment_id);enrollmentRepo.save(e);}
    return "redirect:/profile?success=1";
   }
   p.setStatus("FAILED");paymentRepo.save(p); model.addAttribute("error","Payment verification failed."); return "redirect:/course-details/"+courseId;
  }catch(Exception e){model.addAttribute("error","Payment could not be verified.");return "redirect:/course-details/"+courseId;}
 }
}
