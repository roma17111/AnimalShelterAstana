package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.model.Volunteer;
import animal.shelter.animalsshelter.repository.VolunteerRepository;
import animal.shelter.animalsshelter.service.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private Volunteer volunteer;

    /**
     * Метод для сохранения волонтера в базу данных.
     * @param volunteer - объект волонтера.
     * @return объект волонтера, сохраненный в базу данных.
     */
    @Override
    public Volunteer saveVolunteer(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    /**
     * Метод для получения объекта волонтера из базы данных по id.
     * @param id - id объекта волонтера.
     * @return объект волонтера, найденный по id.
     */
    @Override
    public Volunteer getVolunteerById(Integer id) {
        return volunteerRepository.findById(id).orElse(null);
    }

    /**
     * Метод для получения списка всех волонтеров из базы данных.
     * @return список всех волонтеров в базе данных.
     */
    @Override
    public List<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
    }

    /**
     * Метод для удаления объекта волонтера из базы данных по id.
     * @param id - id объекта волонтера для удаления.
     */
    @Override
    public void deleteVolunteer(Integer id) {
        volunteerRepository.deleteById(id);
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }
}
