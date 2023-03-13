package animal.shelter.animalsshelter.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Cat {
    public enum CatType {
        KITTEN,
        CAT,
        DISABLED
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_id")
    private long id;
    private String name;
    private String age;
    @Column(name = "cat_type")
    @Enumerated(EnumType.STRING)
    private CatType catType;
    private String description;
    private byte[] photo;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
