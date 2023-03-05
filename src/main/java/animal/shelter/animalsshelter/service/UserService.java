package animal.shelter.animalsshelter.service;

import animal.shelter.animalsshelter.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    User getUserById(long id);
    List<User> getAllUsers();
    void deleteUser(long id);
}
