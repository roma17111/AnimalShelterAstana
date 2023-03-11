package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.model.Dog;
import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.repository.UserRepository;
import animal.shelter.animalsshelter.service.DogService;
import animal.shelter.animalsshelter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DogService dogService;

    /**
     * Метод сохраняет нового пользователя в базу данных.
     * @param user объект нового пользователя.
     * @return сохраненный объект пользователя.
     */
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Метод возвращает пользователя из базы данных по указанному идентификатору.
     * @param id идентификатор пользователя.
     * @return объект пользователя или null, если пользователь не найден.
     */
    @Override
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Метод возвращает список всех пользователей, сохраненных в базе данных.
     * @return список всех пользователей.
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Метод удаляет пользователя из базы данных по указанному идентификатору.
     * @param id идентификатор пользователя.
     */
    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public User addDogToUser(Integer userId, Integer dogId) {
        User user = getUserById(userId);
        user.setDog(dogService.getDogById(dogId));
        saveUser(user);
        return user;
    }

    @Override
    public User getAdmin(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user.isNotified() == true) {
            user.setNotified(false);
            saveUser(user);
        } else {
            user.setNotified(true);
            saveUser(user);
        }


        return user;
    }

    @Override
    public User takeDogfromUser(Integer userId) {
        User user = getUserById(userId);
        user.setDog(null);
        saveUser(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User findByChatId(long id) {
        return userRepository.findByChatId(id);
    }

    @Override
    @Transactional
    public List<User> findNewUsers() {
        List<User> users = userRepository.findNewUsers();
        users.forEach((user) -> user.setNotified(true));
        userRepository.saveAll(users);
        return users;
    }
}
