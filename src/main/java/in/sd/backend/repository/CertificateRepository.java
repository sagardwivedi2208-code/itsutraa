package in.sd.backend.repository;
import in.sd.backend.model.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface CertificateRepository extends JpaRepository<Certificate,Long>{ Optional<Certificate> findByCertificateId(String id); List<Certificate> findByStudentOrderByIssuedAtDesc(Student s); Optional<Certificate> findByStudentAndCourse(Student s,Course c); }
