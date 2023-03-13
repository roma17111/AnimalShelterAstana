package animal.shelter.animalsshelter.service;

import animal.shelter.animalsshelter.model.Volunteer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VolunteerService {

    /**
     * Метод для сохранения волонтера в базу данных.
     * @param volunteer - объект волонтера.
     * @return объект волонтера, сохраненный в базу данных.
     */
    Volunteer saveVolunteer(Volunteer volunteer);

    /**
     * Метод для получения объекта волонтера из базы данных по id.
     * @param id - id объекта волонтера.
     * @return объект волонтера, найденный по id.
     */
    Volunteer getVolunteerById(Integer id);

    /**
     * Метод для получения списка всех волонтеров из базы данных.
     * @return список всех волонтеров в базе данных.
     */
    List<Volunteer> getAllVolunteers();

    /**
     * Метод для удаления объекта волонтера из базы данных по id.
     * @param id - id объекта волонтера для удаления.
     */
    void deleteVolunteer(Integer id);
}