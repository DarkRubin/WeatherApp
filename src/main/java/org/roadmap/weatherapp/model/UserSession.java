package org.roadmap.weatherapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "sessions")
public class UserSession extends Model {
    @Id
    @Column(name = "id", nullable = false)
    @UuidGenerator
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "expires_at")
    private Instant expiresAt;

    public UserSession(User user, int lifeTimeHours) {
        this.user = user;
        Instant date = new Date().toInstant();
        this.expiresAt = date.plusSeconds(lifeTimeHours * 3600L);
    }
}