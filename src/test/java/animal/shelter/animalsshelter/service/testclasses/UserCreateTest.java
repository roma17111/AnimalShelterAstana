package animal.shelter.animalsshelter.service.testclasses;

import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.service.UserService;
import animal.shelter.animalsshelter.util.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import static animal.shelter.animalsshelter.constants.BotServicePersonConstants.*;


public class UserCreateTest {


    User user = new User(12345,
            1,
            "Test",
            "test",
            "12345",
            "test");
    private User user1 = new User();
    User testUser = new User(ID_DEFAULT,
            CHAT_ID_DEFAULT,
            STATE_ID_DEFAULT,
            NAME_CORRECT,
            LASTNAME_CORRECT,
            PHONE_CORRECT,
            EMAIL_CORRECT);


    @Test
    public void testIsEmptyClass() {
        Assertions.assertNull(user1.getId());
        Assertions.assertEquals(user1.getChatId(),0);
        Assertions.assertNull(user1.getFirstName());
    }

    @Test
    public void testUserCreateUserIsNotEmpty() {
        Assertions.assertEquals(ID_DEFAULT, testUser.getId());
        Assertions.assertEquals(CHAT_ID_DEFAULT, testUser.getChatId());
        Assertions.assertEquals((int) STATE_ID_DEFAULT, testUser.getStateID());
        Assertions.assertEquals(NAME_CORRECT,testUser.getFirstName());
        Assertions.assertEquals(LASTNAME_CORRECT, testUser.getLastName());
        Assertions.assertEquals(PHONE_CORRECT, testUser.getPhoneNumber());
        Assertions.assertEquals(EMAIL_CORRECT, testUser.getEmail());
    }

    @Test
    public void testContainsCorrectEmailUser() {
        Assertions.assertTrue(Utils.isValidEmailAddress(testUser.getEmail()));
    }

}
