package in.sd.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false) private Student student;
    @ManyToOne(optional = false) private Course course;
    @Column(unique = true) private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    private int amount;
    private String status;
    private LocalDateTime createdAt = LocalDateTime.now();
    public Long getId(){return id;} public Student getStudent(){return student;} public void setStudent(Student v){student=v;}
    public Course getCourse(){return course;} public void setCourse(Course v){course=v;} public String getRazorpayOrderId(){return razorpayOrderId;} public void setRazorpayOrderId(String v){razorpayOrderId=v;}
    public String getRazorpayPaymentId(){return razorpayPaymentId;} public void setRazorpayPaymentId(String v){razorpayPaymentId=v;} public String getRazorpaySignature(){return razorpaySignature;} public void setRazorpaySignature(String v){razorpaySignature=v;}
    public int getAmount(){return amount;} public void setAmount(int v){amount=v;} public String getStatus(){return status;} public void setStatus(String v){status=v;} public LocalDateTime getCreatedAt(){return createdAt;}
}
