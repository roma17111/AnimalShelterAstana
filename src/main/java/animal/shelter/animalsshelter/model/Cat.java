package animal.shelter.animalsshelter.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_id")
    private long id;
    private String name;
    private String age;
    private String description;
    private byte[] photo;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
