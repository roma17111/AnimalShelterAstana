package animal.shelter.animalsshelter.service;

import animal.shelter.animalsshelter.model.Dog;

import java.util.List;

public interface DogService {

    /**
     * Метод для сохранения собаки в базу данных.
     * @param dog - объект собаки.
     * @return объект собаки, сохраненный в базу данных.
     */
    Dog saveDog(Dog dog);

    /**
     * Метод для получения объекта собаки из базы данных по id.
     * @param id - id объекта собаки.
     * @return объект собаки, найденный по id.
     */
    Dog getDogById(Integer id);

    /**
     * Метод для получения списка всех собак из базы данных.
     * @return список всех собак в базе данных.
     */
    List<Dog> getAllDogs();

    /**
     * Метод для удаления объекта собаки из базы данных по id.
     * @param id - id объекта собаки для удаления.
     */
    void deleteDog(Integer id);
}
