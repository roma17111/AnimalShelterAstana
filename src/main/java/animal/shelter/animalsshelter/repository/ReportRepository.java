package animal.shelter.animalsshelter.repository;

import animal.shelter.animalsshelter.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для доступа к сущностям отчетов.
 * JpaRepository - интерфейс, предоставляющий набор стандартных методов для работы с данными в базе данных.
 * Наследуется от JpaRepository, параметризован классом Report и Integer - типом идентификатора.
 * Аннотация @Repository указывает, что данный класс является репозиторием, который должен быть сканирован Spring-контейнером для создания бина.
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
}
