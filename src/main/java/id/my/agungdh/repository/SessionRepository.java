package id.my.agungdh.repository;

import id.my.agungdh.entity.Session;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class SessionRepository implements PanacheRepositoryBase<Session, Long> {

    public Optional<Session> findByTokenHash(String tokenHash) {
        return find("tokenHash", tokenHash).firstResultOptional();
    }
}
