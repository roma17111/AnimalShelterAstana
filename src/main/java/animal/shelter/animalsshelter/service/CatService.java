package animal.shelter.animalsshelter.service;

import animal.shelter.animalsshelter.model.Cat;

import java.util.List;

public interface CatService {
    /**
     * Метод для сохранения собаки в базу данных.
     * @param dog - объект собаки.
     * @return объект собаки, сохраненный в базу данных.
     */
    Cat saveCat(Cat cat);

    /**
     * Метод для получения объекта собаки из базы данных по id.
     * @param id - id объекта собаки.
     * @return объект собаки, найденный по id.
     */
    Cat findCatById(long id);

    /**
     * Метод для удаления объекта собаки из базы данных по id.
     * @param id - id объекта собаки для удаления.
     */
    void deleteCatById(long id);

    /**
     * Метод для получения списка всех собак из базы данных.
     * @return список всех собак в базе данных.
     */
    List<Cat> findAllCats();
}
