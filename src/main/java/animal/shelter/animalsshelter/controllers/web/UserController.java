package animal.shelter.animalsshelter.controllers.web;

import animal.shelter.animalsshelter.model.TestEntity;
import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.repository.TestJPA;
import animal.shelter.animalsshelter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для проведения операция с пользователями.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final TestJPA testJPA;

    public UserController(UserService userService, TestJPA testJPA) {
        this.userService = userService;
        this.testJPA = testJPA;
    }

    /**
     * Данный запрос позволяет
     * получить данные из тестовой таблицы
     * @return Возвращает коллекцию со списком из БД*/
    @GetMapping("/entities")
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

    /**
     * При помощи этого запроса можно получить
     * список реальных пользователей приюта*/

    @GetMapping("/all")
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

    /**
     * При помощи этого запроса можно выдать волонтёрские
     * права реальному пользователю
     * @param id Данный метод принимает параметр на вход
     * чат id telegram пользователя*/

    @PostMapping("/admin/{id}")
    @ResponseBody
    @Operation(summary = "Сделать пользователя волонтёром",
            description = "Данный запрос позволяет выдать пользователю волонтёрские права" +
                    "всех зарегистрированныз пользователей")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public User setAdminUser(@PathVariable Integer id) {
        return userService.getAdmin(id);
    }

    /**
     * При помощи этого запроса можно получить пользователя
     * по id из базы данных
     * @param id PRIMARY KEY - первичный ключ*/

    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "Найти пользоватетя по id телаграм чата",
            description = "Данный запрос позволяет найти пользователя по id чата в телеграм" +
                    "всех зарегистрированныз пользователей")
    public User findByChatId(@PathVariable long id) {
        return userService.findByChatId(id);
    }

    @DeleteMapping("/removal")
    @Operation(summary = "Удалить пользователя",
            description = "Данный запрос позволяет удалить зарегистрированного пользователя " +
                    "из базы по id")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public void deleteUser(@RequestParam Integer id) {
        userService.deleteUser(id);
    }
}