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

        User user = new User();

        Dog expected = new Dog();

        expected.setNickname(NICKNAME_DEFAULT);
        expected.setIntroductionRules(DOG_DESCRIPTION_DEFAULT);
        expected.setRequiredDocuments(DOG_DESCRIPTION_DEFAULT);
        expected.setTransportationRecommendations(DOG_DESCRIPTION_DEFAULT);
        expected.setDogType(DOG_TYPE_DEFAULT);
        expected.setHomeArrangementRecommendations(DOG_DESCRIPTION_DEFAULT);
        expected.setPrimaryCommunicationTips(DOG_DESCRIPTION_DEFAULT);
        expected.setRecommendedKynologists(DOG_DESCRIPTION_DEFAULT);
        expected.setRefusalReasons(DOG_DESCRIPTION_DEFAULT);
        expected.setDogPhoto(DOG_PHOTO_DEFAULT);
        expected.setUser(user);

        when(dogRepository.save(any(Dog.class))).thenReturn(expected);
        Dog actual = dogServiceImpl.saveDog(expected);

        assertThat(actual.getNickname()).isEqualTo(expected.getNickname());
        assertThat(actual.getIntroductionRules()).isEqualTo(expected.getIntroductionRules());
        assertThat(actual.getRequiredDocuments()).isEqualTo(expected.getRequiredDocuments());
        assertThat(actual.getTransportationRecommendations()).isEqualTo(expected.getTransportationRecommendations());
        assertThat(actual.getDogType()).isEqualTo(expected.getDogType());
        assertThat(actual.getHomeArrangementRecommendations()).isEqualTo(expected.getHomeArrangementRecommendations());
        assertThat(actual.getPrimaryCommunicationTips()).isEqualTo(expected.getPrimaryCommunicationTips());
        assertThat(actual.getRecommendedKynologists()).isEqualTo(expected.getRecommendedKynologists());
        assertThat(actual.getRefusalReasons()).isEqualTo(expected.getRefusalReasons());
        assertThat(actual.getDogPhoto()).isEqualTo(expected.getDogPhoto());
        assertThat(actual.getUser()).isEqualTo(expected.getUser());
    }


    @Test
    void getDogByIdTest() {

        User user = new User();

        Dog expected = new Dog();

        expected.setId(1);
        expected.setNickname(NICKNAME_DEFAULT);
        expected.setIntroductionRules(DOG_DESCRIPTION_DEFAULT);
        expected.setRequiredDocuments(DOG_DESCRIPTION_DEFAULT);
        expected.setTransportationRecommendations(DOG_DESCRIPTION_DEFAULT);
        expected.setDogType(DOG_TYPE_DEFAULT);
        expected.setHomeArrangementRecommendations(DOG_DESCRIPTION_DEFAULT);
        expected.setPrimaryCommunicationTips(DOG_DESCRIPTION_DEFAULT);
        expected.setRecommendedKynologists(DOG_DESCRIPTION_DEFAULT);
        expected.setRefusalReasons(DOG_DESCRIPTION_DEFAULT);
        expected.setDogPhoto(DOG_PHOTO_DEFAULT);
        expected.setUser(user);

        when(dogRepository.findById(any(Integer.class))).thenReturn(Optional.of(expected));
        Dog actual = dogServiceImpl.getDogById(1);

        assertThat(actual.getNickname()).isEqualTo(expected.getNickname());
        assertThat(actual.getIntroductionRules()).isEqualTo(expected.getIntroductionRules());
        assertThat(actual.getRequiredDocuments()).isEqualTo(expected.getRequiredDocuments());
        assertThat(actual.getTransportationRecommendations()).isEqualTo(expected.getTransportationRecommendations());
        assertThat(actual.getDogType()).isEqualTo(expected.getDogType());
        assertThat(actual.getHomeArrangementRecommendations()).isEqualTo(expected.getHomeArrangementRecommendations());
        assertThat(actual.getPrimaryCommunicationTips()).isEqualTo(expected.getPrimaryCommunicationTips());
        assertThat(actual.getRecommendedKynologists()).isEqualTo(expected.getRecommendedKynologists());
        assertThat(actual.getRefusalReasons()).isEqualTo(expected.getRefusalReasons());
        assertThat(actual.getDogPhoto()).isEqualTo(expected.getDogPhoto());
        assertThat(actual.getUser()).isEqualTo(expected.getUser());
    }


    @Test
    void getAllDogsTest() {

        User user = new User();

        ArrayList<Dog> expected = new ArrayList<>();

        Dog dog = new Dog();
        dog.setNickname(NICKNAME_DEFAULT);
        dog.setIntroductionRules(DOG_DESCRIPTION_DEFAULT);
        dog.setRequiredDocuments(DOG_DESCRIPTION_DEFAULT);
        dog.setTransportationRecommendations(DOG_DESCRIPTION_DEFAULT);
        dog.setDogType(DOG_TYPE_DEFAULT);
        dog.setHomeArrangementRecommendations(DOG_DESCRIPTION_DEFAULT);
        dog.setPrimaryCommunicationTips(DOG_DESCRIPTION_DEFAULT);
        dog.setRecommendedKynologists(DOG_DESCRIPTION_DEFAULT);
        dog.setRefusalReasons(DOG_DESCRIPTION_DEFAULT);
        dog.setDogPhoto(DOG_PHOTO_DEFAULT);
        dog.setUser(user);

        Dog dog1 = new Dog();
        dog1.setNickname(NICKNAME_DEFAULT);
        dog1.setIntroductionRules(DOG_DESCRIPTION_DEFAULT);
        dog1.setRequiredDocuments(DOG_DESCRIPTION_DEFAULT);
        dog1.setTransportationRecommendations(DOG_DESCRIPTION_DEFAULT);
        dog1.setDogType(DOG_TYPE_DEFAULT);
        dog1.setHomeArrangementRecommendations(DOG_DESCRIPTION_DEFAULT);
        dog1.setPrimaryCommunicationTips(DOG_DESCRIPTION_DEFAULT);
        dog1.setRecommendedKynologists(DOG_DESCRIPTION_DEFAULT);
        dog1.setRefusalReasons(DOG_DESCRIPTION_DEFAULT);
        dog1.setDogPhoto(DOG_PHOTO_DEFAULT);
        dog1.setUser(user);

        Dog dog2 = new Dog();
        dog2.setNickname(NICKNAME_DEFAULT);
        dog2.setIntroductionRules(DOG_DESCRIPTION_DEFAULT);
        dog2.setRequiredDocuments(DOG_DESCRIPTION_DEFAULT);
        dog2.setTransportationRecommendations(DOG_DESCRIPTION_DEFAULT);
        dog2.setDogType(DOG_TYPE_DEFAULT);
        dog2.setHomeArrangementRecommendations(DOG_DESCRIPTION_DEFAULT);
        dog2.setPrimaryCommunicationTips(DOG_DESCRIPTION_DEFAULT);
        dog2.setRecommendedKynologists(DOG_DESCRIPTION_DEFAULT);
        dog2.setRefusalReasons(DOG_DESCRIPTION_DEFAULT);
        dog2.setDogPhoto(DOG_PHOTO_DEFAULT);
        dog2.setUser(user);

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

