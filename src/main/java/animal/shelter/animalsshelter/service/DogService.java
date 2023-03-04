package animal.shelter.animalsshelter.service;

import animal.shelter.animalsshelter.model.Dog;

import java.util.List;

public interface DogService {
    Dog saveDog(Dog dog);
    Dog getDogById(Integer id);
    List<Dog> getAllDogs();
    void deleteDog(Integer id);
}
