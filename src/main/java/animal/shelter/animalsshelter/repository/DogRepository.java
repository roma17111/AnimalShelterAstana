package animal.shelter.animalsshelter.repository;

import animal.shelter.animalsshelter.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {
}
