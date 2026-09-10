package in.sd.backend.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity public class Certificate {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(unique=true,nullable=false,length=40) private String certificateId;
 @ManyToOne(optional=false) private Student student; @ManyToOne(optional=false) private Course course;
 private int score,totalQuestions; private LocalDateTime issuedAt;
 public Long getId(){return id;} public void setId(Long v){id=v;} public String getCertificateId(){return certificateId;} public void setCertificateId(String v){certificateId=v;}
 public Student getStudent(){return student;} public void setStudent(Student v){student=v;} public Course getCourse(){return course;} public void setCourse(Course v){course=v;}
 public int getScore(){return score;} public void setScore(int v){score=v;} public int getTotalQuestions(){return totalQuestions;} public void setTotalQuestions(int v){totalQuestions=v;} public LocalDateTime getIssuedAt(){return issuedAt;} public void setIssuedAt(LocalDateTime v){issuedAt=v;}
}
