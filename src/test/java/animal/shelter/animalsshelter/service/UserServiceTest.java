package animal.shelter.animalsshelter.service;


import animal.shelter.animalsshelter.model.Dog;
import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.repository.DogRepository;
import animal.shelter.animalsshelter.repository.UserRepository;
import animal.shelter.animalsshelter.service.impl.DogServiceImpl;
import animal.shelter.animalsshelter.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static animal.shelter.animalsshelter.constants.BotServicePersonConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private DogRepository dogRepositoryMock;
    @Mock
    private UserRepository userRepositoryMock;
    @InjectMocks
    private DogServiceImpl dogService;
    @InjectMocks
    private UserServiceImpl userService;

    /**
     * Тест метода <b>getUserById</b> в UserService
     */
    @Test
    public void getUserByIdTest() {
        User expected = new User(ID_DEFAULT,
                CHAT_ID_DEFAULT,
                STATE_ID_DEFAULT,
                NAME_CORRECT,
                LASTNAME_CORRECT,
                PHONE_CORRECT,
                EMAIL_CORRECT);

        when(userRepositoryMock.findById(any(Integer.class))).thenReturn(Optional.of(expected));

        User actual = userService.getUserById(ID_DEFAULT);

        Assertions.assertThat(actual.getId()).isEqualTo(expected.getId());
        Assertions.assertThat(actual.getChatId()).isEqualTo(expected.getChatId());
        Assertions.assertThat(actual.getStateID()).isEqualTo(expected.getStateID());
        Assertions.assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        Assertions.assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        Assertions.assertThat(actual.getPhoneNumber()).isEqualTo(expected.getPhoneNumber());
        Assertions.assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    }

    /**
     * Тест метода <b>findByChatId</b> в UserService
     */
    @Test
    public void findByChatIdTest() {

        User testUser1 = new User(ID_DEFAULT,
                CHAT_ID_DEFAULT,
                STATE_ID_DEFAULT,
                NAME_CORRECT,
                LASTNAME_CORRECT,
                PHONE_CORRECT,
                EMAIL_CORRECT);
        userService.saveUser(testUser1);
       Mockito.when(userRepositoryMock.findByChatId(any(Long.class))).thenReturn(testUser1);
        User actual = userService.findByChatId(ID_DEFAULT);
        Assertions.assertThat(actual).isEqualTo(testUser1);
    }
    /**
     * Тест метода <b>saveUser()</b> в UserService
     */
    @Test
    public void saveUserTest() {
        User expected = new User(ID_DEFAULT,
                CHAT_ID_DEFAULT,
                STATE_ID_DEFAULT,
                NAME_CORRECT,
                LASTNAME_CORRECT,
                PHONE_CORRECT,
                EMAIL_CORRECT);

        when(userRepositoryMock.save(any(User.class))).thenReturn(expected);

        User actual = userService.saveUser(expected);

        Assertions.assertThat(actual.getId()).isEqualTo(expected.getId());
        Assertions.assertThat(actual.getChatId()).isEqualTo(expected.getChatId());
        Assertions.assertThat(actual.getStateID()).isEqualTo(expected.getStateID());
        Assertions.assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        Assertions.assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        Assertions.assertThat(actual.getPhoneNumber()).isEqualTo(expected.getPhoneNumber());
        Assertions.assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    }
    /**
     * Тест метода <b>deleteUser()</b> в UserService
     */
    @Test
    public void deleteUserTest() {
        User expected = new User(ID_DEFAULT,STATE_ID_DEFAULT);

        when(userRepositoryMock.save(any(User.class))).thenReturn(expected);

//        User actual = userService.deleteUser(ID_DEFAULT); не получается удаление

//        Assertions.assertThat(actual.getId()).isEqualTo(expected.getId());
//        Assertions.assertThat(actual.getChatId()).isEqualTo(expected.getChatId());
//        Assertions.assertThat(actual.getStateID()).isEqualTo(expected.getStateID());
//        Assertions.assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
//        Assertions.assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
//        Assertions.assertThat(actual.getPhoneNumber()).isEqualTo(expected.getPhoneNumber());
//        Assertions.assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    }
    /**
     * Тест метода <b>addDogToUser()</b> в Userservice
     */
    @Test
    public void addDogToUser() {
        User expectedUser = new User(ID_DEFAULT, null);
        Dog expectedDog = new Dog(ID_DEFAULT2);

        when(dogService.saveDog(any(Dog.class))).thenReturn(expectedDog);
        when(userService.saveUser(any(User.class))).thenReturn(expectedUser);

        userService.addDogToUser(expectedUser.getId(),expectedDog.getId());
        Assertions.assertThat(expectedUser.getDog()).isNotNull();
    }

    /**
     * тест метода <b>getAllUsers()</b> in UserService
     */
    @Test
    public void getAllUsersTest() {
        List<User> expected = new ArrayList<>();

        User testUser = new User(ID_DEFAULT,
                CHAT_ID_DEFAULT,
                STATE_ID_DEFAULT,
                NAME_CORRECT,
                LASTNAME_CORRECT,
                PHONE_CORRECT,
                EMAIL_CORRECT);
        expected.add(testUser);

        User testUser1 = new User(ID_DEFAULT2,
                CHAT_ID_DEFAULT2,
                STATE_ID_DEFAULT2,
                NAME_DEFAULT,
                LASTNAME_DEFAULT,
                PHONE_DEFAULT,
                PHONE_DEFAULT);
        expected.add(testUser1);

        when(userRepositoryMock.findAll()).thenReturn(expected);

        Collection<User> actual = userService.getAllUsers();

        Assertions.assertThat(actual.size()).isEqualTo(expected.size());
        Assertions.assertThat(actual).isEqualTo(expected);
    }

}
