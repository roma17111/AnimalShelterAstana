package animal.shelter.animalsshelter.controllers.web;

import animal.shelter.animalsshelter.model.Report;
import animal.shelter.animalsshelter.service.CallVolunteerMsgService;
import animal.shelter.animalsshelter.service.FileService;
import animal.shelter.animalsshelter.service.ImageParser;
import animal.shelter.animalsshelter.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.springframework.http.MediaType.ALL;

/**
 * Контроллер для операций с отчётами хазяев собак
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private final CallVolunteerMsgService msgService;
    private final ImageParser imageParser;

    private final FileService fileService;

    public ReportController(ReportService reportService, CallVolunteerMsgService msgService, ImageParser imageParser, FileService fileService) {
        this.reportService = reportService;
        this.msgService = msgService;
        this.imageParser = imageParser;
        this.fileService = fileService;
    }

    /**
     * При помощи этого запроса можно получить
     * список всех отправленных отчётов
     */
    @GetMapping("/all")
    @Operation(summary = "Получить список отправленных отчётов",
            description = "Данный запрос позволяет получить полный список отправленных отчётов когда-либо")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public String getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return reports.toString();
    }

    /**
     * При помощи этого запроса можно получить
     * список всех сообщений волонтёру из БД
     */
    @GetMapping("/messages")
    @Operation(summary = "Посмотреть список вопросов от пользователей",
            description = "Данный запрос позволяет получить полный вопросов пользователей   ")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public String getAllMessages() {
        return msgService.getAllCallVolunteerMsgs().toString();
    }

    /**
     * Метод удаления отчёта по id.
     *
     * @param id идентификатор удаляемого отчёта
     */
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

    @GetMapping(value = "/download")
    @Operation(summary = "Загрузить отчёты PDF",
            description = "Данный запрос позволяет загрузить отчёты, отправленные усыновителями" )
    public ResponseEntity getPhotoReport(HttpServletResponse response) {
        fileService.getPdfDocument();
        try {
            File file = new File("document.pdf");
            InputStreamResource inputStream = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"document.pdf\"")
                    .contentLength(Files.size(file.toPath()))
                    .body(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}