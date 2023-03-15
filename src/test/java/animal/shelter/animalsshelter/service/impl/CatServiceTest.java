package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.model.Cat;
import animal.shelter.animalsshelter.model.Report;
import animal.shelter.animalsshelter.repository.CatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static animal.shelter.animalsshelter.constants.BotServiceCatConstants.*;
import static animal.shelter.animalsshelter.constants.BotServicePersonConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatServiceTest {

    @Mock
    private CatRepository catRepository;

    @InjectMocks
    private CatServiceImpl catService;

    @Test
    void saveCatTest() {

        Cat cat = new Cat();

        cat.setStateId(STATE_ID_DEFAULT2);
        cat.setName("Руслан");
        cat.setAge("1");
        cat.setDescription("Test");
        cat.setCatType(Cat.CatType.KITTEN);
        cat.setDescription("test");

        when(catRepository.save(any(Cat.class))).thenReturn(cat);
        Cat actual = catService.saveCat(cat);

        assertThat(actual.getStateId()).isEqualTo(cat.getStateId());
        assertThat(actual.getDescription()).isEqualTo(cat.getDescription());
        assertThat(actual.getPhoto()).isEqualTo(cat.getPhoto());
        assertThat(actual.getName()).isEqualTo(cat.getName());
        assertThat(actual.getAge()).isEqualTo(cat.getAge());
        assertThat(actual.getStateId()).isEqualTo(cat.getStateId());
        assertThat(actual.getCatType()).isEqualTo(cat.getCatType());
    }


    @Test
    void getCatByIdTest() {

        Cat cat = new Cat();

        cat.setId(ID_DEFAULT);
        cat.setStateId(STATE_ID_DEFAULT2);
        cat.setName("Руслан");
        cat.setAge("1");
        cat.setDescription("Test");
        cat.setCatType(Cat.CatType.KITTEN);
        cat.setDescription("test");;

        when(catRepository.findById(any(Long.class))).thenReturn(Optional.of(cat));
        Cat actual = catService.findCatById(ID_DEFAULT);

        assertThat(actual.getStateId()).isEqualTo(cat.getStateId());
        assertThat(actual.getDescription()).isEqualTo(cat.getDescription());
        assertThat(actual.getPhoto()).isEqualTo(cat.getPhoto());
        assertThat(actual.getName()).isEqualTo(cat.getName());
        assertThat(actual.getAge()).isEqualTo(cat.getAge());
        assertThat(actual.getStateId()).isEqualTo(cat.getStateId());
        assertThat(actual.getCatType()).isEqualTo(cat.getCatType());
    }


    @Test
    void getAllReportsTest() {

        Cat cat1 = new Cat();

        ArrayList<Cat> expected = new ArrayList<>();

        Cat cat = new Cat();

        cat.setId(ID_DEFAULT);
        cat.setStateId(STATE_ID_DEFAULT2);
        cat.setName("Руслан");
        cat.setAge("1");
        cat.setDescription("Test");
        cat.setCatType(Cat.CatType.KITTEN);
        cat.setDescription("test");;
        expected.add(cat);
        expected.add(cat1);

        when(catRepository.findAll()).thenReturn(expected);
        Collection<Cat> actual = catService.findAllCats();
        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void deleteCatTest() {
        Cat cat = new Cat();

        cat.setStateId(STATE_ID_DEFAULT2);
        cat.setName("Руслан");
        cat.setAge("1");
        cat.setDescription("Test");
        cat.setCatType(Cat.CatType.KITTEN);
        cat.setDescription("test");;

        lenient().when(catRepository.findById(cat.getId())).thenReturn(Optional.of(cat));
       catService.deleteCatById(cat.getId());
        verify(catRepository).deleteById(cat.getId());
    }
}
