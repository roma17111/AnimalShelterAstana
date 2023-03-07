package animal.shelter.animalsshelter.controllers.web;

import animal.shelter.animalsshelter.model.Dog;
import animal.shelter.animalsshelter.model.Report;
import animal.shelter.animalsshelter.model.TestEntity;
import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.repository.TestJPA;
import animal.shelter.animalsshelter.service.DogService;
import animal.shelter.animalsshelter.service.ReportService;
import animal.shelter.animalsshelter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для получения списков данных.
 */
@RestController
@RequestMapping("/get/all")
public class GetController {

    private final UserService userService;
    private final ReportService reportService;
    private final TestJPA testJPA;
    private final DogService dogService;

    public GetController(UserService userService, ReportService reportService, TestJPA testJPA, DogService dogService) {
        this.userService = userService;
        this.reportService = reportService;
        this.testJPA = testJPA;
        this.dogService = dogService;
    }

    @GetMapping("/testEntities")
    @Operation(summary = "Получить все записи с тестовой таблицы",
            description = "Данный запрос позволяет проверить работоспособность базы данных")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public List<TestEntity> getAllTestEntity() {
        List<TestEntity> entities = testJPA.findAll();
        return entities;
    }

    @GetMapping("/reports")
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

    @GetMapping("/users")
    @Operation(summary = "Получить список всех пользователей",
            description = "Данный запрос позволяет получить полный список" +
                    "всех зарегистрированныз пользователей")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/dogs")
    @Operation(summary = "Список собак",
            description = "Данный запрос позволяет получить полный список" +
                    "доступных собак в приюте")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public List<Dog> getAllDogs() {
        return dogService.getAllDogs();
    }
}