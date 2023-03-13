package animal.shelter.animalsshelter.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Cat {

    public Cat() {
    }

    public enum CatType {
        KITTEN,
        ADULT_CAT,
        DISABLED_CAT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_id")
    private long id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "age")
    private String age;

    @Column(name = "breed")
    private String breed;

    @Column(name = "description")
    private String description;

    @Column(name = "cat_photo")
    private byte[] photo;

    @Column(name = "cat_type")
    @Enumerated(EnumType.STRING)
    private Dog.DogType dogType;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "state_id")
    private int stateId;

    @Column(name = "chat_id")
    private long chatId;
}
