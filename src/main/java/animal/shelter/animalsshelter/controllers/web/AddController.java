package animal.shelter.animalsshelter.controllers.web;

import animal.shelter.animalsshelter.model.Dog;
import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.repository.TestJPA;
import animal.shelter.animalsshelter.service.DogService;
import animal.shelter.animalsshelter.service.ReportService;
import animal.shelter.animalsshelter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер контроллера для добавления новых сущностей.
 */
@RestController
@RequestMapping("/add")
public class AddController {

    private final UserService userService;
    private final ReportService reportService;
    private final TestJPA testJPA;
    private final DogService dogService;

    public AddController(UserService userService, ReportService reportService, TestJPA testJPA, DogService dogService) {
        this.userService = userService;
        this.reportService = reportService;
        this.testJPA = testJPA;
        this.dogService = dogService;
    }

    @PostMapping("/dog")
    @Operation(summary = "Добавить собаку",
            description = "Данный запрос позволяет поставить на учёт новую собаку")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public Dog addDog(@RequestParam String nickname,
                      @RequestParam String introductionRules,
                      @RequestParam String requiredDocuments,
                      @RequestParam String transportationRecommendations,
                      @RequestParam String homeArrangementRecommendations,
                      @RequestParam String primaryCommunicationTips,
                      @RequestParam String recommendedKynologists,
                      @RequestParam String refusalReasons,
                      @RequestParam Dog.DogType dogType) {
        Dog dog = new Dog();
        dog.setNickname(nickname);
        dog.setRequiredDocuments(requiredDocuments);
        dog.setDogType(dogType);
        dog.setIntroductionRules(introductionRules);
        dog.setTransportationRecommendations(transportationRecommendations);
        dog.setHomeArrangementRecommendations(homeArrangementRecommendations);
        dog.setPrimaryCommunicationTips(primaryCommunicationTips);
        dog.setRecommendedKynologists(recommendedKynologists);
        dog.setRefusalReasons(refusalReasons);
        return dogService.saveDog(dog);
    }

    @PostMapping("/givedog")
    @Operation(summary = "передать собаку новому хозяину",
            description = "Данный запрос позволяет передать собаку новому владельцу")
    @ApiResponse(responseCode = "200",
            description = "Операция успешна")
    @ApiResponse(responseCode = "400",
            description = "параметры запроса отсутствуют или имеют некорректный формат;")
    @ApiResponse(responseCode = "500",
            description = "произошла ошибка, не зависящая от вызывающей стороны.")
    public User addDogToUser(@RequestParam Integer userId,
                             @RequestParam Integer dogId) {
        return userService.addDogToUser(userId, dogId);
    }
}
