package in.sd.backend;

import in.sd.backend.model.Course;
import in.sd.backend.model.Student;
import in.sd.backend.repository.CourseRepository;
import in.sd.backend.repository.StudentRepository;
import in.sd.backend.repository.InternshipRepository;
import in.sd.backend.repository.EnrollmentRepository;
import in.sd.backend.repository.PaymentRepository;
import in.sd.backend.model.Internship;
import in.sd.backend.model.QuizQuestion;
import in.sd.backend.repository.QuizQuestionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
 private final CourseRepository courseRepo; private final StudentRepository studentRepo; private final InternshipRepository internshipRepo; private final EnrollmentRepository enrollmentRepo; private final PaymentRepository paymentRepo; private final QuizQuestionRepository quizRepo; private final PasswordEncoder encoder;
 @Value("${ADMIN_EMAIL:admin@itsutraa.com}") private String adminEmail;
 @Value("${ADMIN_PASSWORD:ChangeMe123!}") private String adminPassword;
 public DataInitializer(CourseRepository c,StudentRepository s,InternshipRepository i,EnrollmentRepository en,PaymentRepository p,QuizQuestionRepository q,PasswordEncoder e){courseRepo=c;studentRepo=s;internshipRepo=i;enrollmentRepo=en;paymentRepo=p;quizRepo=q;encoder=e;}
 public void run(String...args){
  removeLegacySummerInternship();
  seedOrUpdateCourses();
  seedOrUpdateInternships();
  seedQuizQuestions();
  if(studentRepo.findByEmail(adminEmail)==null){Student a=new Student();a.setName("IT SUTRAA Admin");a.setEmail(adminEmail);a.setPassword(encoder.encode(adminPassword));a.setAdmin(true);studentRepo.save(a);}
 }

 private void removeLegacySummerInternship(){
  Course old=courseRepo.findByTitleIgnoreCase("Summer Internship");
  if(old!=null && !enrollmentRepo.existsByCourseId(old.getId()) && !paymentRepo.existsByCourseId(old.getId())) courseRepo.delete(old);
 }
 private void seedOrUpdateCourses(){
  course("Core Java","Java fundamentals, OOP, collections and problem solving",49,199,"/images/java.jpg");
  course("Advanced Java","Servlets, JSP and enterprise Java foundations",49,199,"/images/advance.jpg");
  course("JDBC","Database connectivity and practical CRUD",19,99,"/images/jdbc.jpg");
  course("SQL","Write powerful queries and work with databases",19,99,"/images/sql.jpg");
  course("Spring Boot","Build production-style REST APIs with Spring Boot",199,499,"/images/springboot.png");
 }
 private void course(String t,String d,int offer,int original,String image){
  Course c=courseRepo.findAll().stream().filter(x->t.equalsIgnoreCase(x.getTitle())).findFirst().orElse(new Course());
  c.setTitle(t); c.setDescription(d); c.setPrice(offer); c.setOriginalPrice(original); c.setImageUrl(image); courseRepo.save(c);
 }
 private void seedOrUpdateInternships(){
  internship("Java","Java Development Internship","Build Java applications, practice OOP and collections, create APIs and complete a guided industry-style project.","3 Months",599,1499,"https://images.unsplash.com/photo-1780253256194-34e5867ccb8c?auto=format&fit=crop&fm=jpg&ixlib=rb-4.1.0&q=80&w=1400");
  internship("Python","Python Development Internship","Learn Python development through automation, APIs, problem solving and a practical project.","3 Months",599,1499,"https://images.unsplash.com/photo-1763568258612-0ae7f6eb1422?auto=format&fit=crop&fm=jpg&ixlib=rb-4.1.0&q=80&w=1400");
  internship("Web Development","Full Stack Web Development Internship","Work with HTML, CSS, JavaScript and backend fundamentals while building a responsive web project.","6 Months",999,2999,"https://images.unsplash.com/photo-1618477388954-7852f32655ec?auto=format&fit=crop&fm=jpg&ixlib=rb-4.1.0&q=80&w=1400");
  internship("Spring Boot","Backend Development with Spring Boot Internship","Develop REST APIs, authentication, database integration and a complete backend project using Spring Boot.","6 Months",999,2999,"https://images.unsplash.com/photo-1607705703571-c5a8695f18f6?auto=format&fit=crop&fm=jpg&ixlib=rb-4.1.0&q=80&w=1400");
  internship("Python & Data Analytics","Data Analytics Internship","Practice Python, SQL, data cleaning, visualization and a portfolio-ready analytics project.","3 Months",799,1999,"https://images.unsplash.com/photo-1763568258492-9569a0af2127?auto=format&fit=crop&fm=jpg&ixlib=rb-4.1.0&q=80&w=1400");
  internship("AI & Machine Learning","AI & Machine Learning Internship","Learn ML fundamentals, model building, evaluation and hands-on mini projects.","6 Months",1299,3499,"https://images.unsplash.com/photo-1774901128281-a884cd447af5?auto=format&fit=crop&fm=jpg&ixlib=rb-4.1.0&q=80&w=1400");
  internship("SQL & Database","SQL & Database Internship","Work with relational databases, queries, joins, normalization and database-driven applications.","3 Months",599,1499,"https://images.unsplash.com/photo-1760536928911-40831dacdbc3?auto=format&fit=crop&fm=jpg&ixlib=rb-4.1.0&q=80&w=1400");
  internship("Android","Android Development Internship","Build practical Android applications and understand UI, APIs, storage and app architecture.","6 Months",999,2999,"https://images.unsplash.com/photo-1781583749457-40bbdbf855a4?auto=format&fit=crop&fm=jpg&ixlib=rb-4.1.0&q=80&w=1400");
  internship("Cloud & DevOps","Cloud & DevOps Internship","Get practical exposure to Git, Linux, deployment, CI/CD concepts and cloud fundamentals.","6 Months",1299,3499,"https://images.unsplash.com/photo-1667372283496-893f0b1e7c16?auto=format&fit=crop&fm=jpg&ixlib=rb-4.1.0&q=80&w=1400");
  internship("Software Testing","Software Testing Internship","Practice manual testing, test cases, API testing and automation fundamentals with a project.","3 Months",599,1499,"https://images.unsplash.com/photo-1763568258492-9569a0af2127?auto=format&fit=crop&fm=jpg&ixlib=rb-4.1.0&q=80&w=1400");
 }
 private void seedQuizQuestions(){
  for(Course c: courseRepo.findAll()){
   if(quizRepo.countByCourse(c)>=10) continue;
   String t=c.getTitle().toLowerCase();
   String[][] qs;
   if(t.contains("sql")||t.contains("jdbc")) qs=new String[][]{
    {"Which SQL command reads data?","SELECT","INSERT","UPDATE","DELETE","A"},{"Which clause filters rows?","WHERE","ORDER BY","GROUP BY","JOIN","A"},{"Which key uniquely identifies a row?","Primary Key","Foreign Key","Index","View","A"},{"Which operation combines rows from tables?","JOIN","DROP","TRUNCATE","ALTER","A"},{"What does COUNT(*) return?","Number of rows","Column name","Table size in MB","Primary key","A"},{"Which command adds a new row?","INSERT","CREATE","ALTER","GRANT","A"},{"Which command changes existing rows?","UPDATE","SELECT","DROP","COMMIT","A"},{"What does normalization reduce?","Data redundancy","Passwords","Indexes","Queries","A"},{"Which is a relational database?","MySQL","HTML","CSS","Git","A"},{"Which statement saves a transaction?","COMMIT","ROLLBACK","SELECT","CREATE","A"}};
   else if(t.contains("spring")) qs=new String[][]{
    {"Spring Boot is mainly used for?","Building Java applications","Image editing","Video games","Spreadsheets","A"},{"Which annotation marks a REST controller?","@RestController","@Entity","@Table","@BeanOnly","A"},{"Which HTTP method usually creates data?","POST","GET","DELETE","TRACE","A"},{"Which layer commonly contains business logic?","Service","Database","Browser","CSS","A"},{"Which tool manages Java dependencies?","Maven","Photoshop","Figma","Excel","A"},{"Which annotation injects a dependency?","@Autowired","@HTML","@SQL","@CSS","A"},{"Which status means successful creation?","201","404","500","301","A"},{"Spring Data JPA is used for?","Database access","Image compression","UI design","Video rendering","A"},{"What is REST based on?","HTTP resources","Desktop widgets","Binary files only","CSS rules","A"},{"Which file commonly stores Spring configuration?","application.properties","index.css","README.exe","pom.png","A"}};
   else if(t.contains("advanced")) qs=new String[][]{
    {"Which Java technology handles server-side pages?","JSP","CSS","SQL","Git","A"},{"What does Servlet handle?","Web requests","Images only","Database hardware","OS boot","A"},{"Which object stores HTTP session data?","HttpSession","Scanner","StringBuilder","Thread","A"},{"Which is a Java collection?","ArrayList","HTML","JSON","CSS","A"},{"Which keyword inherits a class?","extends","implementsOnly","inherits","superclass","A"},{"Which interface is commonly used for sorting?","Comparator","RunnableOnly","SerializableOnly","CloneOnly","A"},{"What does JDBC connect Java to?","Databases","Browsers","Printers","CSS","A"},{"Which is checked exception?","IOException","ArithmeticException","NullPointerException","ArrayIndexOutOfBoundsException","A"},{"What is MVC?","Model View Controller","Main Variable Class","Module Version Cache","None","A"},{"Which layer displays data to users?","View","Model","Database","Repository","A"}};
   else qs=new String[][]{
    {"Which keyword creates an object?","new","make","object","create","A"},{"Which concept hides implementation details?","Encapsulation","Inheritance","Polymorphism","Compilation","A"},{"Which collection does not allow duplicates?","Set","List","Array","MapOnly","A"},{"Which keyword defines a constant variable?","final","staticOnly","const","fixed","A"},{"Which method starts a Java application?","main","start","runApp","execute","A"},{"Which keyword handles exceptions?","try","catchOnly","throwOnly","error","A"},{"Which OOP feature allows one interface, many forms?","Polymorphism","Compilation","Parsing","Packaging","A"},{"Which tool builds Java projects?","Maven","Chrome","MySQL","JSP","A"},{"What does API stand for?","Application Programming Interface","Applied Program Internet","Application Process Input","None","A"},{"Which HTTP method reads a resource?","GET","POST","PUT","DELETE","A"}};
   for(String[] q:qs){QuizQuestion x=new QuizQuestion();x.setCourse(c);x.setQuestion(q[0]);x.setOptionA(q[1]);x.setOptionB(q[2]);x.setOptionC(q[3]);x.setOptionD(q[4]);x.setCorrectOption(q[5]);quizRepo.save(x);}
  }
 }

 private void internship(String domain,String title,String description,String duration,int offer,int original,String image){
  Internship x=internshipRepo.findAll().stream().filter(i->title.equalsIgnoreCase(i.getTitle())).findFirst().orElse(new Internship());
  x.setDomain(domain);x.setTitle(title);x.setDescription(description);x.setDuration(duration);x.setPrice(offer);x.setOriginalPrice(original);x.setImageUrl(image);internshipRepo.save(x);
 }
}
