package animal.shelter.animalsshelter.controllers.web;

import animal.shelter.animalsshelter.model.Report;
import animal.shelter.animalsshelter.service.CallVolunteerMsgService;
import animal.shelter.animalsshelter.service.FileService;
import animal.shelter.animalsshelter.service.ImageParser;
import animal.shelter.animalsshelter.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

    @GetMapping(value = "/photo")
    public ResponseEntity getPhotoReport(HttpServletResponse response) {
        fileService.getPdfDocument();
       return ResponseEntity.ok().build();
    }



    private void responseFile(HttpServletResponse response, File imgFile) {
        try (InputStream is = new FileInputStream(imgFile);
             OutputStream os = response.getOutputStream();) {
            byte[] buffer = new byte[1024]; // пул буферов потока файлов изображений
            while (is.read(buffer) != -1) {
                os.write(buffer);
            }
            os.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}