package id.my.agungdh.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "sessions")
public class Session extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "token_hash", nullable = false, length = 64, unique = true)
    public String tokenHash;

    @Column(name = "user_id", nullable = false)
    public Long userId;

    @Column(name = "ip_address", length = 45)
    public String ipAddress;

    @Column(name = "user_agent", length = 500)
    public String userAgent;

    @Column(name = "expires_at", nullable = false)
    public LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
