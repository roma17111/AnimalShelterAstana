package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.model.Dog;
import animal.shelter.animalsshelter.repository.DogRepository;
import animal.shelter.animalsshelter.service.DogService;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DogServiceImpl implements DogService {

    @Autowired
    private final DogRepository dogRepository;

    @Override
    public Dog saveDog(Dog dog) {
        return dogRepository.save(dog);
    }

    @Override
    public Dog getDogById(long id) {
        return dogRepository.findById(Long.valueOf(id)).orElse(null);
    }

    @Override
    public List<Dog> getAllDogs() {
        return dogRepository.findAll();
    }

    @Override
    public void deleteDog(long id) {
        dogRepository.deleteById(id);
    }
}
