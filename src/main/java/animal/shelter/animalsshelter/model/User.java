package animal.shelter.animalsshelter.model;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "chat_id", nullable = false, unique = true)
    private Long chatId;

    @Pattern(message = "Bad formed person first_name: ${validatedValue}",
    regexp = "^[А-Я][а-я][A-Z][a-z]")// Проставила Кирилицу и латиницу
    @Size(min = 3)
    @NonNull
    @Column(name = "first_name")
    private String firstName;

    @Pattern(message = "Bad formed person last_name: ${validatedValue}",
            regexp = "^[А-Я][а-я][A-Z][a-z]")//Проставила Кирилицу и латиницу
    @Size(min = 3)
    @NonNull
    @Column(name = "last_name")
    private String lastName;

    @Pattern(message = "PhoneNumber has invalid format: ${validatedValue}",
            regexp = "[0-9]")//Проставила только цифры
    @Size(min = 9, max = 10) // чтобы номер можно было ввести с 8 и без
    @NonNull
    @Column(name = "phone_number")
    private String phoneNumber;

    @Email( message = "Email address has invalid format: ${validatedValue}",
    regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    @NonNull
    @Size(min = 7,max = 130)
    @Column(name = "email")
    private String email;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dog_id", referencedColumnName = "dog_id")
    private Dog dog;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "report_id", referencedColumnName = "report_id")
    private Report report;

    public User() {

    }
}
