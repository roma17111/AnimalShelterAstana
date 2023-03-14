package animal.shelter.animalsshelter.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dog")
public class Dog {
    public Dog() {
    }

    public enum DogType {
        PUPPY,
        ADULT,
        DISABLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dog_id")
    private Integer id;

    @Column(name = "state_id")
    private int stateId;

    @Column(name = "chat_id")
    private long chatId;
    @Column(name = "nickname")
    private String nickname;

    private String age;

    @Column(name = "dog_type")
    @Enumerated(EnumType.STRING)
    private DogType dogType;

    private String breed;
    private String description;

    @Column(name = "dog_photo")
    private byte[] dogPhoto;

}
