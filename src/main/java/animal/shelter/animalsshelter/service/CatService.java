package animal.shelter.animalsshelter.service;

import animal.shelter.animalsshelter.model.Cat;

import java.util.List;

public interface CatService {
    Cat saveCat(Cat cat);

    Cat findCatById(long id);

    void deleteCatById(long id);

    List<Cat> findAllCats();
}
