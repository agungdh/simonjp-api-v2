package id.my.agungdh.repository;

import id.my.agungdh.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, Long> {

    public Optional<User> findByUuid(UUID uuid) {
        return find("uuid", uuid).firstResultOptional();
    }

    public Optional<User> findByUsername(String username) {
        return find("username = ?1", username).firstResultOptional();
    }
}
