package co.edu.sena.productsreact.service;

import co.edu.sena.productsreact.entity.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PasswordResetMailService {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetMailService.class);
    private final RestTemplate restTemplate;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.mail.from}")
    private String fromAddress;

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    public void sendResetLink(User user, String token) {
        String resetLink = UriComponentsBuilder
                .fromUriString(frontendUrl)
                .path("/reset-password")
                .queryParam("token", token)
                .build()
                .toUriString();

        String html = """
                <h2>Recuperacion de contrasena - EFORM</h2>

                <p>Hola %s</p>

                <p>Recibimos una solicitud para cambiar tu contrasena.</p>

                <p>
                    <a href="%s">
                        Haz clic aqui para restablecer tu contrasena
                    </a>
                </p>

                <p>Este enlace vence en 30 minutos.</p>

                <p>Si no solicitaste este cambio puedes ignorar este correo.</p>
                """.formatted(user.getUsername(), resetLink);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", brevoApiKey);

        Map<String, Object> body = Map.of(
                "sender", Map.of(
                        "name", "EFORM",
                        "email", fromAddress
                ),
                "to", List.of(
                        Map.of("email", user.getEmail())
                ),
                "subject", "Recuperacion de contrasena - EFORM",
                "htmlContent", html
        );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://api.brevo.com/v3/smtp/email",
                    HttpMethod.POST,
                    request,
                    String.class
            );
            log.debug("Password reset email sent successfully. Status: {}", response.getStatusCode());
        } catch (Exception e) {
            log.error("Error sending password reset email to: {}", user.getEmail(), e);

            throw e;
        }
    }
}