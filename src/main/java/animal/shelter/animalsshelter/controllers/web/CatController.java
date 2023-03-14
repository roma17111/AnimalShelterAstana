package animal.shelter.animalsshelter.controllers.web;

import animal.shelter.animalsshelter.model.Cat;
import animal.shelter.animalsshelter.model.Dog;
import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.service.CatService;
import animal.shelter.animalsshelter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для операций с котами
 */

@RestController
@RequestMapping("/cat")
public class CatController {

    private final UserService userService;

    private final CatService catService;

    public CatController(UserService userService, CatService catService) {
        this.userService = userService;
        this.catService = catService;
    }

    /**
     * При помощи этого запроса можно добавить кота в новый приют
     *
     * @param cat Параметром метода является объект класса Cat,
     *            который создаётся при отправлении запроса волонтёром на сервер
     */
    @PostMapping("/")
    @Operation(summary = "Добавить кота",
            description = "Данный запрос позволяет поставить на учёт нового кота")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public Cat addCat(@RequestBody Cat cat) {
        return catService.saveCat(cat);
    }

    /**
     * При помощи этого запроса можно передать кота из приюта хозяину
     *
     * @param catId  - id кота из таблицы БД
     * @param userId - id человека из БД
     */
    @PutMapping("/broadcast/{userId}/{catId}")
    @ResponseBody
    @Operation(summary = "передать кота новому хозяину",
            description = "Данный запрос позволяет передать кота новому владельцу")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public User addCatToUser(@PathVariable Integer userId,
                             @PathVariable Integer catId) {
        return userService.addCatToUser(userId, catId);
    }

    /**
     * При помощи этого запроса можно посмотреть
     * список котов из приюта
     */
    @GetMapping("/all")
    @Operation(summary = "Список котов",
            description = "Данный запрос позволяет получить полный список" +
                    "доступных котов в приюте")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public List<Cat> getAllCats() {
        return catService.findAllCats();
    }

    /**
     * При помощи этого запроса можно забрать кота
     * у плохого хозяина
     *
     * @param id - id User из таблицы БД
     */
    @PutMapping("/withdrawal/{id}")
    @ResponseBody
    @Operation(summary = "Отобрать кота у недобросовестного хозяина",
            description = "Данный запрос позволяет обнулить поле Cat у User")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public User takeCatFromBadUser(@PathVariable Integer id) {
        return userService.takeCatfromUser(id);
    }

    @DeleteMapping("/removal")
    @Operation(summary = "Удалить кошечку из базы",
            description = "Данный запрос позволяет удалить животное из семейства кошачьих " +
                    "из базы по id")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public void deleteCat(@RequestParam long id) {
        catService.deleteCatById(id);
    }
}
