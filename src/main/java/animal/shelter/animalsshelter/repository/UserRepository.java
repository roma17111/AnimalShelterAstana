package animal.shelter.animalsshelter.repository;

import animal.shelter.animalsshelter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
