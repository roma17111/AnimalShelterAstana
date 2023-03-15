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

    /**
     * При помощи этого запроса можно добавить собаку
     * в новый приют
     *
     * @param dog Параметром метода является объект класса Dog,
     *            который создаётся при отправлении запроса волонтёром на сервер
     */
    @PostMapping("/")
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

    /**
     * При помощи этого запроса можно передать собаку хозяину
     * из приюта
     *
     * @param dogId  - id собаки из таблицы БД
     * @param userId - id человека из БД
     */
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

    /**
     * При помощи этого запроса можно посмотреть
     * список собак из приюта
     */
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

    /**
     * При помощи этого запроса можно забрать собаку
     * у плохого хозяина
     *
     * @param id - id User из таблицы БД
     */
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

    /**
     * Удаляет собаку из базы данных по её идентификатору.
     *
     * @param id идентификатор собаки в базе данных
     */
    @DeleteMapping("/removal")
    @Operation(summary = "Удалить собакена из базы",
            description = "Данный запрос позволяет собаку " +
                    "из базы по id")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public void deleteDog(@RequestParam Integer id) {
        dogService.getDogById(id);
    }
}
