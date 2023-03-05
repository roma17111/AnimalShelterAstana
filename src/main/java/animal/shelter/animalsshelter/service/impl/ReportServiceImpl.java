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

    @Override
    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public Report getReportById(Integer id) {
        return reportRepository.findById(id).orElse(null);
    }

    @Override
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    @Override
    public void deleteReport(Integer id) {
        reportRepository.deleteById(id);
    }
}
