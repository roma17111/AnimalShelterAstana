package animal.shelter.animalsshelter.service;

import animal.shelter.animalsshelter.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    /**
     * Метод сохраняет нового пользователя в базу данных.
     * @param user объект нового пользователя.
     * @return сохраненный объект пользователя.
     */
    User saveUser(User user);

    /**
     * Метод возвращает пользователя из базы данных по указанному идентификатору.
     * @param id идентификатор пользователя.
     * @return объект пользователя или null, если пользователь не найден.
     */
    User getUserById(Integer id);

    /**
     * Метод возвращает список всех пользователей, сохраненных в базе данных.
     * @return список всех пользователей.
     */
    List<User> getAllUsers();

    /**
     * Метод удаляет пользователя из базы данных по указанному идентификатору.
     * @param id идентификатор пользователя.
     */
    void deleteUser(Integer id);

    User addDogToUser(Integer userId, Integer dogId);

    User takeDogfromUser(Integer userId);

    @Transactional(readOnly = true)
    User findByChatId(long id);

    @Transactional
    List<User> findNewUsers();
}
