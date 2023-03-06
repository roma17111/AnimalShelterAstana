package animal.shelter.animalsshelter.service;

import animal.shelter.animalsshelter.model.Report;

import java.util.List;

public interface ReportService {
    /**
     * Сохраняет новый отчет в базу данных.
     * @param report - отчет, который нужно сохранить.
     * @return сохраненный отчет.
     */
    Report saveReport(Report report);

    /**
     * Возвращает отчет с указанным идентификатором.
     * @param id - идентификатор отчета.
     * @return отчет с указанным идентификатором или null, если отчет не найден.
     */
    Report getReportById(Integer id);

    /**
     * Возвращает список всех сохраненных отчетов.
     * @return список всех сохраненных отчетов.
     */
    List<Report> getAllReports();

    /**
     * Удаляет отчет с указанным идентификатором.
     * @param id - идентификатор отчета, который нужно удалить.
     */
    void deleteReport(Integer id);
}

