package animal.shelter.animalsshelter.controllers.web;

import animal.shelter.animalsshelter.model.CallVolunteerMsg;
import animal.shelter.animalsshelter.model.Report;
import animal.shelter.animalsshelter.service.CallVolunteerMsgService;
import animal.shelter.animalsshelter.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для операций с отчётами хазяев собак
 * **/
@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private final CallVolunteerMsgService msgService;

    public ReportController(ReportService reportService, CallVolunteerMsgService msgService) {
        this.reportService = reportService;
        this.msgService = msgService;
    }

    /**
     * При помощи этого запроса можно получить
     * список всех отправленных отчётов*/
    @GetMapping("/all")
    @Operation(summary = "Получить список отправленных отчётов",
            description = "Данный запрос позволяет получить полный список отправленных отчётов когда-либо")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public List<Report> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return reports;
    }

    /**
     * При помощи этого запроса можно получить
     * список всех сообщений волонтёру из БД*/
    @GetMapping("/messages")
    @Operation(summary = "Посмотреть список вопросов от пользователей",
            description = "Данный запрос позволяет получить полный вопросов пользователей   ")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public List<CallVolunteerMsg> getAllMessages() {
        return msgService.getAllCallVolunteerMsgs();
    }

    @DeleteMapping("/removal")
    @Operation(summary = "Удалить отчёт",
            description = "Данный запрос позволяет удалить отчёт, отправленный усыновителем " +
                    "из базы по id")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public void deleteReport(@RequestParam Integer id) {
        reportService.deleteReport(id);
    }
}
