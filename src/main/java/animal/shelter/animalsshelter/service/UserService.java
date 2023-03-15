package animal.shelter.animalsshelter.service;

import animal.shelter.animalsshelter.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    /**
     * Метод сохраняет нового пользователя в базу данных.
     *
     * @param user объект нового пользователя.
     * @return сохраненный объект пользователя.
     */
    User saveUser(User user);

    /**
     * Метод возвращает пользователя из базы данных по указанному идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return объект пользователя или null, если пользователь не найден.
     */
    User getUserById(Integer id);

    /**
     * Метод возвращает список всех пользователей, сохраненных в базе данных.
     *
     * @return список всех пользователей.
     */
    List<User> getAllUsers();

    /**
     * Метод удаляет пользователя из базы данных по указанному идентификатору.
     *
     * @param id идентификатор пользователя.
     */
    void deleteUser(Integer id);

    /**
     * Метод, позволяюющий передать собаку из приюта хозяину
     */
    User addDogToUser(Integer userId, Integer dogId);

    /**
     * Метод, позволяюющий передать кота из приюта хозяину
     */
    User addCatToUser(Integer userID, Integer catID);

    /**
     * Метод позволяет переменить статус пользователя на волонтёра
     * с правами доступа к закрытым для всех запросам
     */

    User getAdmin(Integer userId);

    /**
     * Этот запрос позволяет забрать собаку у плохого хозяина
     */
    User takeDogfromUser(Integer userId);

    /**
     * Этот запрос позволяет забрать кота у плохого хозяина
     */
    User takeCatfromUser(Integer uderID);

    @Transactional(readOnly = true)
    User findByChatId(long id);

    @Transactional
    List<User> findNewUsers();

    User deleteCatToUser(Integer userID, Integer catID);

    User deleteDogToUser(Integer userId, Integer dogId);
}
