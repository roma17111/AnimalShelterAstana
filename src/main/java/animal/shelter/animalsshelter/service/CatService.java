package animal.shelter.animalsshelter.service;

import animal.shelter.animalsshelter.model.Cat;

import java.util.List;

public interface CatService {
    /**
     * Метод для сохранения кота в базу данных.
     * @param cat - объект собаки.
     * @return объект собаки, сохраненный в базу данных.
     */
    Cat saveCat(Cat cat);

    /**
     * Метод для получения объекта кота из базы данных по id.
     * @param id - id объекта собаки.
     * @return объект собаки, найденный по id.
     */
    Cat findCatById(long id);

    /**
     * Метод для удаления объекта кота из базы данных по id.
     * @param id - id объекта собаки для удаления.
     */
    void deleteCatById(long id);

    /**
     * Метод для получения списка всех котов из базы данных.
     * @return список всех собак в базе данных.
     */
    List<Cat> findAllCats();
}
