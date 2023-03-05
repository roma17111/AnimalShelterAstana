package animal.shelter.animalsshelter.service;

import animal.shelter.animalsshelter.model.Dog;

import java.util.List;

public interface DogService {
    Dog saveDog(Dog dog);
    Dog getDogById(long id);
    List<Dog> getAllDogs();
    void deleteDog(long id);
}
