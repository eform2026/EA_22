package co.edu.sena.productsreact;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProductsReactApiApplication {

    private static final Logger log = LoggerFactory.getLogger(ProductsReactApiApplication.class);

    public static void main(String[] args) {
        loadDotEnv();
        String envProfile = System.getProperty("SPRING_PROFILES_ACTIVE");
        if (envProfile != null && !envProfile.isBlank()
                && System.getProperty("spring.profiles.active") == null) {
            System.setProperty("spring.profiles.active", envProfile);
        }
        SpringApplication.run(ProductsReactApiApplication.class, args);
    }

    private static void loadDotEnv() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();
            dotenv.entries().forEach(entry -> {
                if (System.getProperty(entry.getKey()) == null
                        && System.getenv(entry.getKey()) == null) {
                    System.setProperty(entry.getKey(), entry.getValue());
                }
            });
        } catch (Exception ex) {
            log.warn("No se pudo cargar el archivo .env: {}", ex.getMessage());
        }
    }
}
