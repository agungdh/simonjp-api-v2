package id.my.agungdh.service;

import id.my.agungdh.entity.Session;
import id.my.agungdh.entity.User;
import id.my.agungdh.repository.SessionRepository;
import id.my.agungdh.repository.UserRepository;
import id.my.agungdh.util.PasswordHasher;
import id.my.agungdh.util.TokenHasher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.time.OffsetDateTime;
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

    @Inject
    TokenHasher tokenHasher;

    @Transactional
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new WebApplicationException("Username atau password salah", Response.Status.UNAUTHORIZED));

        if (!passwordHasher.verify(user.password, password)) {
            throw new WebApplicationException("Username atau password salah", Response.Status.UNAUTHORIZED);
        }

        String token = generateToken();
        String tokenHash = tokenHasher.hash(token);

        Session session = new Session();
        session.token = tokenHash;
        session.userId = user.id;
        session.expiresAt = OffsetDateTime.now().plusDays(SESSION_DURATION_DAYS);
        sessionRepository.persist(session);

        return token;
    }

    public Optional<User> validateSession(String token) {
        String tokenHash = tokenHasher.hash(token);

        return sessionRepository.findByToken(tokenHash)
                .filter(session -> session.expiresAt.isAfter(OffsetDateTime.now()))
                .map(session -> userRepository.findById(session.userId));
    }

    @Transactional
    public void logout(String token) {
        String tokenHash = tokenHasher.hash(token);
        sessionRepository.deleteByToken(tokenHash);
    }

    private String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
