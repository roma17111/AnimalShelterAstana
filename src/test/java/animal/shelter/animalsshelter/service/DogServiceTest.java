package animal.shelter.animalsshelter.service;

import animal.shelter.animalsshelter.model.Dog;
import animal.shelter.animalsshelter.repository.DogRepository;
import animal.shelter.animalsshelter.service.impl.DogServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static animal.shelter.animalsshelter.constants.BotServiceDogConstants.*;
import static animal.shelter.animalsshelter.constants.BotServicePersonConstants.*;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@ExtendWith(MockitoExtension.class)
public class DogServiceTest {
    @Mock
    private DogRepository dogRepositoryMock;
    @InjectMocks
    private DogServiceImpl dogService;

    /**
     * Тест метода <b>getDogById</b> в DogService
     */
    @Test
    public void getDogByIdTest() {
        Dog expected = new Dog();
        expected.setNickname(NICKNAME_CORRECT);
        expected.setDescription(DOG_DESCRIPTION_DEFAULT);
        expected.setDogType(DOG_TYPE_DEFAULT);
        expected.setDogPhoto(DOG_PHOTO_DEFAULT);

        when(dogRepositoryMock.findById(1)).thenReturn(Optional.of(expected));

        Dog actual = dogService.getDogById(1);

        Assertions.assertThat(actual.getId()).isEqualTo(expected.getId());
        Assertions.assertThat(actual.getNickname()).isEqualTo(expected.getNickname());
        Assertions.assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        Assertions.assertThat(actual.getDogType()).isEqualTo(expected.getDogType());
        Assertions.assertThat(actual.getDogPhoto()).isEqualTo(expected.getDogPhoto());
    }

    /**
     * Тест метода <b>saveDog()</b> в DogService
     */
    @Test
    public void saveDogTest() {
        Dog expected = new Dog();
        expected.setNickname(NICKNAME_CORRECT);
        expected.setDescription(DOG_DESCRIPTION_DEFAULT);
        expected.setDogType(DOG_TYPE_DEFAULT);
        expected.setDogPhoto(DOG_PHOTO_DEFAULT);

        when(dogRepositoryMock.save(expected)).thenReturn(expected);

        Dog actual = dogService.saveDog(expected);

        Assertions.assertThat(actual.getId()).isEqualTo(expected.getId());
        Assertions.assertThat(actual.getNickname()).isEqualTo(expected.getNickname());
        Assertions.assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        Assertions.assertThat(actual.getDogType()).isEqualTo(expected.getDogType());
        Assertions.assertThat(actual.getDogPhoto()).isEqualTo(expected.getDogPhoto());
    }

    /**
     * тест метода <b>getAllDogs()</b> in UserService
     */
    @Test
    public void getAllDogsTest() {
        List<Dog> expected = new ArrayList<>();

        Dog expected1 = new Dog();
        expected1.setId(1);
        expected1.setNickname(NICKNAME_CORRECT);
        expected1.setDescription(DOG_DESCRIPTION_DEFAULT);
        expected1.setDogType(DOG_TYPE_DEFAULT);
        expected1.setDogPhoto(DOG_PHOTO_DEFAULT);
        expected.add(expected1);

        Dog expected2 = new Dog();
        expected1.setId(2);
        expected1.setNickname(NICKNAME_CORRECT);
        expected1.setDescription(DOG_DESCRIPTION_DEFAULT);
        expected1.setDogType(DOG_TYPE_DEFAULT);
        expected1.setDogPhoto(DOG_PHOTO_DEFAULT);
        expected.add(expected2);

        when(dogRepositoryMock.findAll()).thenReturn(expected);

        Collection<Dog> actual = dogService.getAllDogs();

        Assertions.assertThat(actual.size()).isEqualTo(expected.size());
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
