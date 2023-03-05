package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.model.Report;
import animal.shelter.animalsshelter.repository.ReportRepository;
import animal.shelter.animalsshelter.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    @Autowired
    private final ReportRepository reportRepository;

    @Override
    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public Report getReportById(long id) {
        return reportRepository.findById(id).orElse(null);
    }

    @Override
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    @Override
    public void deleteReport(long id) {
        reportRepository.deleteById(id);
    }
}
