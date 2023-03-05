package animal.shelter.animalsshelter.service;

import animal.shelter.animalsshelter.model.Report;

import java.util.List;

public interface ReportService {
    Report saveReport(Report report);
    Report getReportById(long id);
    List<Report> getAllReports();
    void deleteReport(long id);
}

