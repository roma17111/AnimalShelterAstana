package animal.shelter.animalsshelter.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "volunteer")
public class Volunteer {

    public Volunteer() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "volunteer_id")
    private Integer id;

    @Column(name = "chat_id", nullable = false, unique = true)
    private long chatId;

    @Column(name = "given_name")
    private String givenName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;
}