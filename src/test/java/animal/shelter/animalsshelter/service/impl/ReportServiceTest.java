package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.model.CallVolunteerMsg;
import animal.shelter.animalsshelter.model.Report;
import animal.shelter.animalsshelter.repository.ReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static animal.shelter.animalsshelter.constants.BotServicePersonConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    void saveReportTest() {

        Report report = new Report();

        report.setStateId(STATE_ID_DEFAULT2);
        report.setDiet("Test");
        report.setPhoto(new byte[1234]);
        report.setUserInfo(EMAIL_CORRECT);
        report.setBehaviorChange("Test");

        when(reportRepository.save(any(Report.class))).thenReturn(report);
        Report actual = reportService.saveReport(report);

        assertThat(actual.getStateId()).isEqualTo(report.getStateId());
        assertThat(actual.getDiet()).isEqualTo(report.getDiet());
        assertThat(actual.getPhoto()).isEqualTo(report.getPhoto());
        assertThat(actual.getBehaviorChange()).isEqualTo(report.getBehaviorChange());
        assertThat(actual.getUserInfo()).isEqualTo(report.getUserInfo());
    }


    @Test
    void getReportByIdTest() {

        Report report = new Report();

        report.setId(ID_DEFAULT);
        report.setStateId(STATE_ID_DEFAULT2);
        report.setDiet("Test");
        report.setPhoto(new byte[1234]);
        report.setUserInfo(EMAIL_CORRECT);
        report.setBehaviorChange("Test");

        when(reportRepository.findById(any(Integer.class))).thenReturn(Optional.of(report));
        Report actual = reportService.getReportById(ID_DEFAULT);

        assertThat(actual.getStateId()).isEqualTo(report.getStateId());
        assertThat(actual.getDiet()).isEqualTo(report.getDiet());
        assertThat(actual.getPhoto()).isEqualTo(report.getPhoto());
        assertThat(actual.getBehaviorChange()).isEqualTo(report.getBehaviorChange());
        assertThat(actual.getUserInfo()).isEqualTo(report.getUserInfo());
    }


    @Test
    void getAllReportsTest() {

        Report report1 = new Report();

        ArrayList<Report> expected = new ArrayList<>();

        Report report = new Report();

        report.setId(ID_DEFAULT);
        report.setStateId(STATE_ID_DEFAULT2);
        report.setDiet("Test");
        report.setPhoto(new byte[1234]);
        report.setUserInfo(EMAIL_CORRECT);
        report.setBehaviorChange("Test");
        expected.add(report);
        expected.add(report1);
        when(reportRepository.findAll()).thenReturn(expected);
        Collection<Report> actual = reportService.getAllReports();
        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void deleteMsgTest() {
        Report report = new Report();
        report.setId(ID_DEFAULT);

        lenient().when(reportRepository.findById(report.getId())).thenReturn(Optional.of(report));
        reportService.deleteReport(report.getId());
        verify(reportRepository).deleteById(report.getId());
    }
}
