package co.edu.sena.productsreact.event;

import co.edu.sena.productsreact.entity.Role;
import co.edu.sena.productsreact.entity.User;
import co.edu.sena.productsreact.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultUserInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDefaultUsers() {
        createUserIfNotExists("carolina", "carolinatudi3@gmail.com", "08326590If", Role.ROLE_USER);
        createUserIfNotExists("admin", "admin@sena.edu.co", "admin123", Role.ROLE_ADMIN);
    }

    private void createUserIfNotExists(String username, String email, String password, Role role) {
        if (!userRepository.existsByEmail(email)) {
            User user = User.builder()
                    .username(username)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .role(role)
                    .build();
            
            userRepository.save(user);
            log.info("Usuario predeterminado creado: {} ({})", username, email);
        } else {
            log.info("Usuario ya existe: {}", email);
        }
    }
}
