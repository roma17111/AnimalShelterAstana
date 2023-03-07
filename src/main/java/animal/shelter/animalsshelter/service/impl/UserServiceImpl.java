package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.model.Dog;
import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.repository.UserRepository;
import animal.shelter.animalsshelter.service.DogService;
import animal.shelter.animalsshelter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
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
    public User addDogToUser(Integer userId, Integer dogId) {
        Dog dog = dogService.getDogById(dogId);
        User user = getUserById(userId);
        user.setDog(dog);
        saveUser(user);
        return user;
    }
}
