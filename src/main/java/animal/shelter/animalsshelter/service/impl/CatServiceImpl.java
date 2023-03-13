package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.model.Cat;
import animal.shelter.animalsshelter.repository.CatRepository;
import animal.shelter.animalsshelter.service.CatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatServiceImpl implements CatService {

    private final CatRepository catRepository;

    /**
     * Метод для сохранения кошки в базу данных.
     * @param cat - объект собаки.
     * @return объект кошки, сохраненный в базу данных.
     */
    @Override
    public Cat saveCat(Cat cat) {
        return catRepository.save(cat);
    }

    /**
     * Метод для получения объекта кошки из базы данных по id.
     * @param id - id объекта кошки.
     * @return объект собаки, найденный по id.
     */
    @Override
    public Cat findCatById(long id) {
        return catRepository.findById(id).orElse(null);
    }

    /**
     * Метод для удаления объекта кошки из базы данных по id.
     * @param id - id объекта собаки для удаления.
     */
    @Override
    public void deleteCatById(long id) {
        catRepository.deleteById(id);
    }

    /**
     * Метод для получения списка всех кошек из базы данных.
     * @return список всех кошек в базе данных.
     */
    @Override
    public List<Cat> findAllCats() {
        return Collections.unmodifiableList(catRepository.findAll());
    }
}
