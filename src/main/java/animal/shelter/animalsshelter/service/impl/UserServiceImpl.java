package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.repository.UserRepository;
import animal.shelter.animalsshelter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
