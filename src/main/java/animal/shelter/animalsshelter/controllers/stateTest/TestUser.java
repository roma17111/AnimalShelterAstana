package animal.shelter.animalsshelter.controllers.stateTest;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TestUser {
    private long id;
    private long chatId;
    private int stateId;
    private String name;

    public TestUser(Long chatId, int stateId) {
        this.chatId = chatId;
        this.stateId = stateId;
    }

    @Override
    public String toString() {
        return " chatId - " + chatId +'\n'+
                "Имя - " + name;
    }
}
