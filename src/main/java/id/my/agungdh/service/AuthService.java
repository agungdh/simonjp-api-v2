package id.my.agungdh.service;

import id.my.agungdh.entity.Session;
import id.my.agungdh.entity.User;
import id.my.agungdh.repository.SessionRepository;
import id.my.agungdh.repository.UserRepository;
import id.my.agungdh.util.PasswordHasher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class AuthService {

    private static final int SESSION_DURATION_DAYS = 7;

    @Inject
    UserRepository userRepository;

    @Inject
    SessionRepository sessionRepository;

    @Inject
    PasswordHasher passwordHasher;

    @Transactional
    public String login(String username, String password, String ipAddress, String userAgent) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new WebApplicationException("Username atau password salah", Response.Status.UNAUTHORIZED));

        if (!passwordHasher.verify(user.password, password)) {
            throw new WebApplicationException("Username atau password salah", Response.Status.UNAUTHORIZED);
        }

        String token = generateToken();
        String tokenHash = passwordHasher.hash(token);

        Session session = new Session();
        session.tokenHash = tokenHash;
        session.userId = user.id;
        session.ipAddress = ipAddress;
        session.userAgent = userAgent;
        session.expiresAt = LocalDateTime.now().plusDays(SESSION_DURATION_DAYS);
        sessionRepository.persist(session);

        return token;
    }

    public Optional<User> validateSession(String token) {
        String tokenHash = passwordHasher.hash(token);

        return sessionRepository.findByTokenHash(tokenHash)
                .filter(session -> session.expiresAt.isAfter(LocalDateTime.now()))
                .map(session -> userRepository.findById(session.userId));
    }

    private String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
