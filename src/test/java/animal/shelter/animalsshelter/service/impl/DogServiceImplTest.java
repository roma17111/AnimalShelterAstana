package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.model.Dog;
import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.repository.DogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static animal.shelter.animalsshelter.constants.BotServiceDogConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DogServiceImplTest {


    @Mock
    private DogRepository dogRepository;

    @InjectMocks
    private DogServiceImpl dogServiceImpl;

    @Test
    void saveDogTest() {
        Dog expected = new Dog();

        expected.setNickname(NICKNAME_DEFAULT);
        expected.setDescription(DOG_DESCRIPTION_DEFAULT);
        expected.setAge(DOG_AGE_DEFAULT);
        expected.setDogType(DOG_TYPE_DEFAULT);
        expected.setBreed(DOG_BREED_DEFAULT);
        expected.setDogPhoto(DOG_PHOTO_DEFAULT);

        when(dogRepository.save(any(Dog.class))).thenReturn(expected);
        Dog actual = dogServiceImpl.saveDog(expected);

        assertThat(actual.getNickname()).isEqualTo(expected.getNickname());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getAge()).isEqualTo(expected.getAge());
        assertThat(actual.getDogType()).isEqualTo(expected.getDogType());
        assertThat(actual.getBreed()).isEqualTo(expected.getBreed());
        assertThat(actual.getDogPhoto()).isEqualTo(expected.getDogPhoto());
    }


    @Test
    void getDogByIdTest() {
        Dog expected = new Dog();

        expected.setNickname(NICKNAME_DEFAULT);
        expected.setDescription(DOG_DESCRIPTION_DEFAULT);
        expected.setAge(DOG_AGE_DEFAULT);
        expected.setDogType(DOG_TYPE_DEFAULT);
        expected.setBreed(DOG_BREED_DEFAULT);
        expected.setDogPhoto(DOG_PHOTO_DEFAULT);


        when(dogRepository.findById(any(Integer.class))).thenReturn(Optional.of(expected));
        Dog actual = dogServiceImpl.getDogById(1);

        assertThat(actual.getNickname()).isEqualTo(expected.getNickname());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getAge()).isEqualTo(expected.getAge());
        assertThat(actual.getDogType()).isEqualTo(expected.getDogType());
        assertThat(actual.getBreed()).isEqualTo(expected.getBreed());
        assertThat(actual.getDogPhoto()).isEqualTo(expected.getDogPhoto());
    }


    @Test
    void getAllDogsTest() {

        ArrayList<Dog> expected = new ArrayList<>();

        Dog expectedDog = new Dog();

        expectedDog.setNickname(NICKNAME_DEFAULT);
        expectedDog.setDescription(DOG_DESCRIPTION_DEFAULT);
        expectedDog.setAge(DOG_AGE_DEFAULT);
        expectedDog.setDogType(DOG_TYPE_DEFAULT);
        expectedDog.setBreed(DOG_BREED_DEFAULT);
        expectedDog.setDogPhoto(DOG_PHOTO_DEFAULT);
        expected.add(expectedDog);

        Dog expectedDog1 = new Dog();

        expectedDog1.setNickname(NICKNAME_DEFAULT);
        expectedDog1.setDescription(DOG_DESCRIPTION_DEFAULT);
        expectedDog1.setAge(DOG_AGE_DEFAULT);
        expectedDog1.setDogType(DOG_TYPE_DEFAULT);
        expectedDog1.setBreed(DOG_BREED_DEFAULT);
        expectedDog1.setDogPhoto(DOG_PHOTO_DEFAULT);
        expected.add(expectedDog1);

        Dog expectedDog2 = new Dog();

        expectedDog2.setNickname(NICKNAME_DEFAULT);
        expectedDog2.setDescription(DOG_DESCRIPTION_DEFAULT);
        expectedDog2.setAge(DOG_AGE_DEFAULT);
        expectedDog2.setDogType(DOG_TYPE_DEFAULT);
        expectedDog2.setBreed(DOG_BREED_DEFAULT);
        expectedDog2.setDogPhoto(DOG_PHOTO_DEFAULT);
        expected.add(expectedDog2);

        when(dogRepository.findAll()).thenReturn(expected);
        Collection<Dog> actual = dogServiceImpl.getAllDogs();

        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void deleteDogTest() {
        Dog dog = new Dog();

        lenient().when(dogRepository.findById(dog.getId())).thenReturn(Optional.of(dog));
        dogServiceImpl.deleteDog(dog.getId());
    verify(dogRepository).deleteById(dog.getId());
    }
}

