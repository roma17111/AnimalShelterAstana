package animal.shelter.animalsshelter.repository;

import animal.shelter.animalsshelter.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
