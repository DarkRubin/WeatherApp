package org.roadmap.weatherapp.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.roadmap.weatherapp.exception.UserAlreadyRegisteredException;
import org.roadmap.weatherapp.model.Location;
import org.roadmap.weatherapp.model.User;
import org.roadmap.weatherapp.util.PaginationResult;

import java.util.List;
import java.util.Optional;

public class UserDao implements DAO<User> {

    @Override
    public User save(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.persist(user);
                tx.commit();
            } catch (ConstraintViolationException e) {
                tx.rollback();
                throw new UserAlreadyRegisteredException();
            }
        }
        return user;
    }

    public PaginationResult<Location> getLocations(User user, int page) {
        int pageSize = PaginationResult.PAGE_SIZE;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            List<Location> locations = session
                    .createSelectionQuery("from Location where user = :user ", Location.class)
                    .setParameter("user", user)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();

            long locationsCount = session.createSelectionQuery("select count(id) from Location where user = :user", Long.class)
                    .setParameter("user", user)
                    .getSingleResult();

            tx.commit();
            return new PaginationResult<>(locations, page, locationsCount);
        }
    }

    @Override
    public Optional<User> search(User user) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User where login = :login", User.class)
                    .setParameter("login", user.getLogin())
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
