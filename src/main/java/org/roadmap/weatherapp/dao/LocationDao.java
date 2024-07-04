package org.roadmap.weatherapp.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.roadmap.weatherapp.model.Location;
import org.roadmap.weatherapp.model.User;

import java.util.List;
import java.util.Optional;

public class LocationDao implements DAO<Location> {

    private final SessionFactory sessionFactory = DbUtils.getSessionFactory();

    @Override
    public Location save(Location location) {
        try (Session session = sessionFactory.openSession()) {
            session.persist(location);
        }
        return location;
    }

    @Override
    public Optional<Location> search(Location location) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Location where user = :user", Location.class)
                    .setParameter("user", location.getUser())
                    .uniqueResultOptional();
        }
    }

    public List<Location> searchUserLocation(User user) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Location where user = :user", Location.class)
                    .setParameter("user", user)
                    .getResultList();
        }
    }

    @Override
    public Location update(Location location) {
        try (Session session = sessionFactory.openSession()) {
            return session.merge(location);
        }
    }

    @Override
    public void delete(Location location) {
        try (Session session = sessionFactory.openSession()) {
            session.remove(location);
        }
    }
}
