package animal.shelter.animalsshelter.controllers;


import animal.shelter.animalsshelter.controllers.contexts.stateTest.TestRepository;
import animal.shelter.animalsshelter.controllers.contexts.stateTest.TestUser;
import animal.shelter.animalsshelter.controllers.contexts.stateTest.TestUserService;
import animal.shelter.animalsshelter.controllers.web.CatController;
import animal.shelter.animalsshelter.controllers.web.UserController;
import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.repository.UserRepository;
import animal.shelter.animalsshelter.service.UserService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static animal.shelter.animalsshelter.constants.BotServicePersonConstants.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(UserController.class)
public class UserTest {
/*    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TestRepository userRepository;
    @MockBean
    private TestUserService testUserService;

    @MockBean
    private UserRepository userRepositoryMock;
    @MockBean
    private UserService userService;

    public UserTest(TestUserService testUserService) {
        this.testUserService = testUserService;
    }

    @Test
    void findByChatId() throws Exception {
        TestUser testUser = new TestUser();
        testUser.setChatId(CHAT_ID_DEFAULT);

        when(testUserService.findByChatId(anyLong())).thenReturn(testUser);

        mockMvc.perform(
                        get("/user/{id}", ID_DEFAULT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CHAT_ID_DEFAULT));

        verify(testUserService).findByChatId(ID_DEFAULT);
    }

    @Test
    void addUser() throws Exception {
        User user = new User();
        user.setId(ID_DEFAULT);
        user.setChatId(CHAT_ID_DEFAULT);
        user.setStateID(ID_DEFAULT);
        user.setFirstName(NAME_CORRECT);
        user.setLastName(LASTNAME_CORRECT);
        user.setPhoneNumber(PHONE_CORRECT);
        user.setEmail(EMAIL_CORRECT);
        JSONObject userObject = new JSONObject();
        userObject.put("id", ID_DEFAULT);
        userObject.put("firstName", NAME_CORRECT);
        userObject.put("lastName", LASTNAME_CORRECT);

        when(userService.saveUser(User.class.newInstance())).thenReturn(user);

        mockMvc.perform(
                        post("/user")
                                .content(userObject.toString())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userObject.toString()));

        verify(userService).saveUser(user);
    }

    @Test
    void updateUser() throws Exception {
        User user = new User();
        user.setId(ID_DEFAULT);
        user.setChatId(CHAT_ID_DEFAULT);
        user.setStateID(ID_DEFAULT);
        user.setFirstName(NAME_CORRECT);
        user.setLastName(LASTNAME_CORRECT);
        user.setPhoneNumber(PHONE_CORRECT);
        user.setEmail(EMAIL_CORRECT);
        JSONObject userObject = new JSONObject();
        userObject.put("id", ID_DEFAULT);
        userObject.put("firstName", NAME_CORRECT);
        userObject.put("lastName", LASTNAME_CORRECT);


//            when(testUserService.updateUser(user).thenReturn(user);

        mockMvc.perform(
                        put("/user")
                                .content(userObject.toString())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userObject.toString()));

//            verify(testUserService).updateUser(user); пока метод есть только в TestUser

    }

    @Test
    void remove() throws Exception {
        mockMvc.perform(
                        delete("/user/{id}", ID_DEFAULT))
                .andExpect(status().isOk());
//        verify(user).removeById(CHAT_ID_DEFAULT); пока такого метода нет
    }

    @Test
    void findAllUsers() throws Exception {
        when(testUserService.findAllUsers()).thenReturn(List.of(new TestUser()));
        mockMvc.perform(get("/user/entities")).andExpect(status().isOk());
    }*/
}



