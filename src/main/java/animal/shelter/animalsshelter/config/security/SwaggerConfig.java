package animal.shelter.animalsshelter.config.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        servers = {
                @Server(url = "https://asha.java-mouse.ru", description = "Prod server"),
                @Server(url = "http://localhost:8080", description = "Local server")
        }
)
@Configuration
public class SwaggerConfig {
}
