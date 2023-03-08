package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.model.Dog;
import animal.shelter.animalsshelter.repository.DogRepository;
import animal.shelter.animalsshelter.service.DogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DogServiceImpl implements DogService {

    private final DogRepository dogRepository;

    /**
     * Метод для сохранения собаки в базу данных.
     * @param dog - объект собаки.
     * @return объект собаки, сохраненный в базу данных.
     */
    @Override
    public Dog saveDog(Dog dog) {
        return dogRepository.save(dog);
    }

    /**
     * Метод для получения объекта собаки из базы данных по id.
     * @param id - id объекта собаки.
     * @return объект собаки, найденный по id.
     */
    @Override
    public Dog getDogById(Integer id) {
        return dogRepository.findById(id).orElse(null);
    }

    /**
     * Метод для получения списка всех собак из базы данных.
     * @return список всех собак в базе данных.
     */
    @Override
    public List<Dog> getAllDogs() {
        return dogRepository.findAll();
    }

    /**
     * Метод для удаления объекта собаки из базы данных по id.
     * @param id - id объекта собаки для удаления.
     */
    @Override
    public void deleteDog(Integer id) {
        dogRepository.deleteById(id);
    }
}
