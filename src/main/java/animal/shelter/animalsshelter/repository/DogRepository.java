package animal.shelter.animalsshelter.repository;

import animal.shelter.animalsshelter.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository<Dog, Long> {
}
