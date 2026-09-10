package in.sd.backend.model;

import jakarta.persistence.*;

@Entity
public class Internship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String domain;
    private String title;
    @Column(length = 1500)
    private String description;
    private String duration;
    private int price;
    private int originalPrice;
    private String imageUrl;

    public Internship() {}
    public Internship(String domain, String title, String description, String duration, int price, String imageUrl) {
        this.domain = domain; this.title = title; this.description = description;
        this.duration = duration; this.price = price; this.imageUrl = imageUrl;
    }
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getDomain(){return domain;} public void setDomain(String domain){this.domain=domain;}
    public String getTitle(){return title;} public void setTitle(String title){this.title=title;}
    public String getDescription(){return description;} public void setDescription(String description){this.description=description;}
    public String getDuration(){return duration;} public void setDuration(String duration){this.duration=duration;}
    public int getPrice(){return price;} public void setPrice(int price){this.price=price;}
    public int getOriginalPrice(){return originalPrice;} public void setOriginalPrice(int originalPrice){this.originalPrice=originalPrice;}
    public String getImageUrl(){return imageUrl;} public void setImageUrl(String imageUrl){this.imageUrl=imageUrl;}
}
