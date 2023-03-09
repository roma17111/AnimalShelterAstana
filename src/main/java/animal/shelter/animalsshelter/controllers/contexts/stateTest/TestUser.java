package animal.shelter.animalsshelter.controllers.contexts.stateTest;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class TestUser {
    @Id
    @GeneratedValue
    private long id;
    private long chatId;
    private int stateId;

    private String numberPhone;

    private String email;
    private String name;
    private boolean admin;
    private boolean notified = false;

    public TestUser(Long chatId, int stateId) {
        this.chatId = chatId;
        this.stateId = stateId;
    }

    @Override
    public String toString() {
        return " chatId - " + chatId +'\n'+
                "Имя - " + name +'\n'+
                "Номер телефона  - " + numberPhone +'\n'+
                "email - " + email;
    }
}
