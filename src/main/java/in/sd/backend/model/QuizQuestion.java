package in.sd.backend.model;
import jakarta.persistence.*;
@Entity public class QuizQuestion {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) private Course course;
 @Column(length=500,nullable=false) private String question;
 @Column(length=300,nullable=false) private String optionA,optionB,optionC,optionD;
 @Column(length=1,nullable=false) private String correctOption;
 public Long getId(){return id;} public void setId(Long v){id=v;} public Course getCourse(){return course;} public void setCourse(Course v){course=v;}
 public String getQuestion(){return question;} public void setQuestion(String v){question=v;} public String getOptionA(){return optionA;} public void setOptionA(String v){optionA=v;} public String getOptionB(){return optionB;} public void setOptionB(String v){optionB=v;} public String getOptionC(){return optionC;} public void setOptionC(String v){optionC=v;} public String getOptionD(){return optionD;} public void setOptionD(String v){optionD=v;} public String getCorrectOption(){return correctOption;} public void setCorrectOption(String v){correctOption=v;}
}
