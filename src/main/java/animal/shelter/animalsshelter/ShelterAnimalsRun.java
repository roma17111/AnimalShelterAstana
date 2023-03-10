package animal.shelter.animalsshelter;

import animal.shelter.animalsshelter.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class ShelterAnimalsRun {
    public static void main(String[] args) {
        SpringApplication.run(ShelterAnimalsRun.class);
        User user = new User();
    }
}
