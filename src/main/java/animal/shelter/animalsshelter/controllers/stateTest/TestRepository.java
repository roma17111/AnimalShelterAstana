package animal.shelter.animalsshelter.controllers.stateTest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestRepository extends JpaRepository<TestUser,Long> {
    @Query("SELECT u FROM TestUser u WHERE u.notified = false " +
            "AND u.numberPhone IS NOT NULL AND u.email IS NOT NULL")
    List<TestUser> findNewUsers();

    TestUser findByChatId(long id);
}
