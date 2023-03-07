package animal.shelter.animalsshelter.controllers;

import animal.shelter.animalsshelter.model.TestEntity;
import animal.shelter.animalsshelter.repository.TestJPA;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WebController {


    private final TestJPA testJPA;

    public WebController(TestJPA testJPA) {
        this.testJPA = testJPA;
    }

    @GetMapping("/get/all/testEntities")
    public List<TestEntity> getAllTestEntity() {
        List<TestEntity> entities = testJPA.findAll();
        return entities;
    }
}
