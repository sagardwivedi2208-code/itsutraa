package in.sd.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(unique = true)
    private String email;
    private String phone;
    private String password;
    private boolean isAdmin = false;
    private String registrationSource = "Website";
    private LocalDateTime registeredAt = LocalDateTime.now();

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public String getEmail(){return email;} public void setEmail(String email){this.email=email;}
    public String getPhone(){return phone;} public void setPhone(String phone){this.phone=phone;}
    public String getPassword(){return password;} public void setPassword(String password){this.password=password;}
    public boolean isAdmin(){return isAdmin;} public void setAdmin(boolean admin){isAdmin=admin;}
    public String getRegistrationSource(){return registrationSource;} public void setRegistrationSource(String registrationSource){this.registrationSource=registrationSource;}
    public LocalDateTime getRegisteredAt(){return registeredAt;} public void setRegisteredAt(LocalDateTime registeredAt){this.registeredAt=registeredAt;}
}
