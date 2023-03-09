package animal.shelter.animalsshelter.controllers.web;

import animal.shelter.animalsshelter.model.Dog;
import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.service.DogService;
import animal.shelter.animalsshelter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Контроллер для операций с собаками
 */
@RestController
@RequestMapping("/dog")
public class DogController {

    private final UserService userService;
    private final DogService dogService;

    public DogController(UserService userService, DogService dogService) {
        this.userService = userService;
        this.dogService = dogService;
    }

    @PostMapping("/new")
    @Operation(summary = "Добавить собаку",
            description = "Данный запрос позволяет поставить на учёт новую собаку")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public Dog addDog(@RequestBody Dog dog) {
        return dogService.saveDog(dog);
    }

    @PutMapping("/broadcast/{userId}/{dogId}")
    @ResponseBody
    @Operation(summary = "передать собаку новому хозяину",
            description = "Данный запрос позволяет передать собаку новому владельцу")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public User addDogToUser(@PathVariable Integer userId,
                             @PathVariable Integer dogId) {
        return userService.addDogToUser(userId, dogId);
    }

    @GetMapping("/all")
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

    @PutMapping("/withdrawal/{id}")
    @ResponseBody
    @Operation(summary = "Отобрать собаку у недобросовестного хозяина",
            description = "Данный запрос позволяет обнулить поле Dog у User")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public User takeDogFromBadUser(@PathVariable Integer id) {
        return userService.takeDogfromUser(id);
    }
}
