package animal.shelter.animalsshelter.controllers;

import animal.shelter.animalsshelter.controllers.web.CatController;
import animal.shelter.animalsshelter.model.Cat;
import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.service.CatService;
import animal.shelter.animalsshelter.service.UserService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static animal.shelter.animalsshelter.constants.BotServiceCatConstants.*;
import static animal.shelter.animalsshelter.constants.BotServiceDogConstants.NICKNAME_DEFAULT;
import static animal.shelter.animalsshelter.constants.BotServicePersonConstants.*;
import static animal.shelter.animalsshelter.constants.BotServicePersonConstants.LASTNAME_CORRECT;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CatController.class)
    class CatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatService catService;
    @MockBean
    private UserService userService;

/*    @Test
    void addCat() throws Exception {
        Cat cat = new Cat();
        cat.setId(ID_DEFAULT);
        cat.setStateId(STATE_ID_DEFAULT);
        cat.setName(NICKNAME_DEFAULT);
        cat.setAge(CAT_AGE_DEFAULT);
        cat.setDescription(CAT_DESCRIPTION_DEFAULT);
        cat.setCatType(Cat.CatType.KITTEN);
        cat.setBreed(CAT_BREED_DEFAULT);
        cat.setPhoto(CAT_PHOTO_DEFAULT);

        when(catService.saveCat(cat)).thenReturn(cat);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", ID_DEFAULT);
        jsonObject.put("catNickname", NICKNAME_DEFAULT);
        jsonObject.put("catAge", CAT_AGE_DEFAULT);
        jsonObject.put("catDescription", CAT_DESCRIPTION_DEFAULT);
        jsonObject.put("catType", Cat.CatType.KITTEN);
        jsonObject.put("catBreed", CAT_BREED_DEFAULT);
        mockMvc.perform(post("/cat")
                                .content(jsonObject.toString())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonObject.toString()));
        verify(catService).saveCat(cat);
    }*/
/*    @Test
    void addCatToUser() throws Exception{
        Cat cat = new Cat();
        cat.setId(ID_DEFAULT);
        User user = new User();
        user.setId(ID_DEFAULT2);
        when(userService.addCatToUser(ID_DEFAULT2, ID_DEFAULT)).thenReturn(user);
        mockMvc.perform(put("/broadcast/{userId}",ID_DEFAULT))
                .andExpect(status().isOk());

        verify(userService).addCatToUser(ID_DEFAULT2,ID_DEFAULT);
    }*/
 /*   @Test
    void takeCatFromBadUser() throws Exception{
        Cat cat = new Cat();
        cat.setId(ID_DEFAULT);
        User user = new User();
        user.setId(ID_DEFAULT2);
        user.setCat(cat);
        mockMvc.perform(put("/withdrawal/{id}", ID_DEFAULT2))
                .andExpect(status().isOk());
        verify(userService).takeCatfromUser(ID_DEFAULT2);
    }*/

 /*   @Test
    void getAllCats() throws Exception {
        when(catService.findAllCats()).thenReturn(List.of(new Cat()));

        mockMvc.perform(get("/cat/all")).andExpect(status().isOk());
    }*/
  /*  @Test
    void deleteCat() throws Exception {
        mockMvc.perform(delete("/cat/{id}", ID_DEFAULT))
                .andExpect(status().isOk());
        verify(catService).deleteCatById(ID_DEFAULT);
    }*/
}