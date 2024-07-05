package org.roadmap.weatherapp.dao;

import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.roadmap.weatherapp.model.User;

import java.util.Optional;

public class UserDao implements DAO<User>{

    @Override
    @Transactional
    public User save(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.persist(user);
        }
        return user;
    }

    @Override
    public Optional<User> search(User user) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User where login = :login and password = :password", User.class)
                    .setParameter("login", user.getLogin())
                    .setParameter("password", user.getPassword()) //todo
                    .uniqueResultOptional();
        }
    }

    @Override
    public User update(User user) {
        try (Session session = sessionFactory.openSession()) {
            return session.merge(user);
        }
    }

    @Override
    public void delete(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.remove(user);
        }
    }
}
