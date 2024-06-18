package org.roadmap.weatherapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('users_id_seq'")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "login", length = Integer.MAX_VALUE)
    private String login;

    @Column(name = "password", length = Integer.MAX_VALUE)
    private String password;

}