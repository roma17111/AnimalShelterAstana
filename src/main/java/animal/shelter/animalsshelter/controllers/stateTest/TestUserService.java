package animal.shelter.animalsshelter.controllers.stateTest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TestUserService {

    private final TestRepository userRepository;

    public TestUserService(TestRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public TestUser findByChatId(long id) {
        return userRepository.findByChatId(id);
    }

    @Transactional(readOnly = true)
    public List<TestUser> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public List<TestUser> findNewUsers() {
        List<TestUser> users = userRepository.findNewUsers();
        users.forEach((user) -> user.setNotified(true));
        userRepository.saveAll(users);
        return users;
    }

    @Transactional
    public void addUser(TestUser user) {
        user.setAdmin(userRepository.count() == 0);
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(TestUser user) {
        userRepository.save(user);
    }
}
