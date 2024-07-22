package org.roadmap.weatherapp.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.roadmap.weatherapp.model.Location;
import org.roadmap.weatherapp.model.User;

import java.util.Optional;

public class UserDao implements DAO<User> {

    @Override
    public User save(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
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
