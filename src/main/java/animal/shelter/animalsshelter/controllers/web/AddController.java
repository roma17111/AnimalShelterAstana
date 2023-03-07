package animal.shelter.animalsshelter.controllers.web;

import animal.shelter.animalsshelter.model.Dog;
import animal.shelter.animalsshelter.repository.TestJPA;
import animal.shelter.animalsshelter.service.DogService;
import animal.shelter.animalsshelter.service.ReportService;
import animal.shelter.animalsshelter.service.UserService;
import org.springframework.web.bind.annotation.*;

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
}
