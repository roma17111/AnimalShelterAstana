package animal.shelter.animalsshelter.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class Config {

    @Value("${bot.name}")
    String botName;
    @Value("${bot.key}")
    String botKey;
}
