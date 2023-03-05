package animal.shelter.animalsshelter.service;

import animal.shelter.animalsshelter.model.Report;

import java.util.List;

public interface ReportService {
    Report saveReport(Report report);
    Report getReportById(Integer id);
    List<Report> getAllReports();
    void deleteReport(Integer id);
}

