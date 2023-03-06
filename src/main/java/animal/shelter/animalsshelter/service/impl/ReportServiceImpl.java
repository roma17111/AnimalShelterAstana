package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.model.Report;
import animal.shelter.animalsshelter.repository.ReportRepository;
import animal.shelter.animalsshelter.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    @Autowired
    private final ReportRepository reportRepository;

    /**
     * Сохраняет новый отчет в базу данных.
     * @param report - отчет, который нужно сохранить.
     * @return сохраненный отчет.
     */
    @Override
    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }

    /**
     * Возвращает отчет с указанным идентификатором.
     * @param id - идентификатор отчета.
     * @return отчет с указанным идентификатором или null, если отчет не найден.
     */
    @Override
    public Report getReportById(Integer id) {
        return reportRepository.findById(id).orElse(null);
    }

    /**
     * Возвращает список всех сохраненных отчетов.
     * @return список всех сохраненных отчетов.
     */
    @Override
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    /**
     * Удаляет отчет с указанным идентификатором.
     * @param id - идентификатор отчета, который нужно удалить.
     */
    @Override
    public void deleteReport(Integer id) {
        reportRepository.deleteById(id);
    }
}
