package animal.shelter.animalsshelter.controllers.stateTest;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TestUser {
    private Long id;

    private Long chatId;
    private Integer stateId;
    private String name;


    @Override
    public String toString() {
        return " chatId - " + chatId +'\n'+
                "phone - " + name;
    }
}
