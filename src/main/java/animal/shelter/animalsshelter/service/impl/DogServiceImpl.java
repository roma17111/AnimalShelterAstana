package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.model.Dog;
import animal.shelter.animalsshelter.repository.DogRepository;
import animal.shelter.animalsshelter.service.DogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RequiredArgsConstructor
public class DogServiceImpl implements DogService {

    @Autowired
    private final DogRepository dogRepository;

    @Override
    public Dog saveDog(Dog dog) {
        return dogRepository.save(dog);
    }

    @Override
    public Dog getDogById(Integer id) {
        return dogRepository.findById(id).orElse(null);
    }

    @Override
    public List<Dog> getAllDogs() {
        return dogRepository.findAll();
    }

    @Override
    public void deleteDog(Integer id) {
        dogRepository.deleteById(id);
    }
}
