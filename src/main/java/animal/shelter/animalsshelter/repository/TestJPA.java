package animal.shelter.animalsshelter.repository;

import animal.shelter.animalsshelter.model.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface    TestJPA extends JpaRepository<TestEntity, Long> {

    Optional<TestEntity> findById(Long id);
}
