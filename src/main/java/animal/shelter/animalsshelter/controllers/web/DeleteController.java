package animal.shelter.animalsshelter.controllers.web;

import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.repository.TestJPA;
import animal.shelter.animalsshelter.service.DogService;
import animal.shelter.animalsshelter.service.ReportService;
import animal.shelter.animalsshelter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для удаления сущностей из базы данных.
 */
@RestController
public class DeleteController {

    private final UserService userService;
    private final ReportService reportService;
    private final TestJPA testJPA;
    private final DogService dogService;

    public DeleteController(UserService userService, ReportService reportService, TestJPA testJPA, DogService dogService) {
        this.userService = userService;
        this.reportService = reportService;
        this.testJPA = testJPA;
        this.dogService = dogService;
    }

    @PutMapping("/takedog")
    @Operation(summary = "Отобрать собаку у недобросовестного хозяина",
            description = "Данный запрос позволяет обнулить поле Dog у User")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public User takeDogFromBadUser(@RequestParam Integer id) {
        return userService.takeDogfromUser(id);
    }
}
