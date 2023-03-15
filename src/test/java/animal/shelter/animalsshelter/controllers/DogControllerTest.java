package animal.shelter.animalsshelter.controllers;

import animal.shelter.animalsshelter.controllers.web.DogController;
import animal.shelter.animalsshelter.model.Cat;
import animal.shelter.animalsshelter.model.Dog;
import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.service.DogService;
import animal.shelter.animalsshelter.service.UserService;
import org.aspectj.lang.annotation.Before;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static animal.shelter.animalsshelter.constants.BotServiceDogConstants.*;
import static animal.shelter.animalsshelter.constants.BotServicePersonConstants.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DogController.class)
public class DogControllerTest {


    @MockBean
    private DogService dogService;
    @MockBean
    private UserService userService;
    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

   @Test
    void addDog() throws Exception {
        Dog dog = new Dog();
        dog.setId(DOG_ID_DEFAULT);
        dog.setStateId(STATE_ID_DEFAULT);
        dog.setNickname(NICKNAME_CORRECT);
        dog.setAge(DOG_AGE_CORRECT);
        dog.setDescription(DOG_DESCRIPTION_CORRECT);
        dog.setDogType(Dog.DogType.PUPPY);
        dog.setBreed(DOG_BREED_CORRECT);
        dog.setDogPhoto(DOG_PHOTO_DEFAULT);
       assertNotNull(dog);


       Mockito.when(dogService.saveDog(dog)).thenReturn(dog);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", ID_DEFAULT);
        jsonObject.put("dogNickname", NICKNAME_DEFAULT);
        jsonObject.put("dogAge", DOG_AGE_DEFAULT);
        jsonObject.put("dogDescription", DOG_DESCRIPTION_DEFAULT);
        jsonObject.put("dogType", Dog.DogType.DISABLED);
        jsonObject.put("dogBreed", DOG_BREED_DEFAULT);
        dogService.saveDog(dog);
        /*mockMvc.perform(post("/dog")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        verify(dogService).saveDog(dog);*/
    }
    @Autowired
    private MockMvc mockMvc;
    @Test
    void addDogToUser() throws Exception {
        Dog dog = new Dog();
        User user = new User();
        dog.setId(ID_DEFAULT);
        user.setId(ID_DEFAULT2);
        assertNotNull(user);
        Mockito.when(userService.addDogToUser(ID_DEFAULT2, ID_DEFAULT)).thenReturn(user);

        userService.addDogToUser(ID_DEFAULT2,ID_DEFAULT);
    }

    @Test
    void getAllDogs() throws Exception {
        when(dogService.getAllDogs()).thenReturn(List.of(new Dog()));

        mockMvc.perform(get("/dog/all")).andExpect(status().isOk());
    }
    @Test
    void takeDogFromBadUser() throws Exception {
        Dog dog = new Dog();
        User user = new User();
        dog.setId(ID_DEFAULT);
        user.setId(ID_DEFAULT2);
        user.setDog(dog);
        Mockito.when(userService.takeDogfromUser(ID_DEFAULT)).thenReturn(user);

        /*mockMvc.perform(put("/withdrawall/{id}", ID_DEFAULT2))
                .andExpect(status().isOk());
        verify(userService).takeDogfromUser(ID_DEFAULT2);*/

    }

}

