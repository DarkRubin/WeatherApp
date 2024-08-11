package org.roadmap.weatherapp.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.roadmap.weatherapp.exception.LocationAlreadyAddedException;
import org.roadmap.weatherapp.model.Location;
import org.roadmap.weatherapp.model.User;

import java.util.Optional;

public class LocationDao implements DAO<Location> {

    @Override
    public Location save(Location location) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.persist(location);
                tx.commit();
            } catch (ConstraintViolationException e) {
                tx.rollback();
                throw new LocationAlreadyAddedException();
            }
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

    @Override
    public Location update(Location location) {
        try (Session session = sessionFactory.openSession()) {
            return session.merge(location);
        }
    }

    @Override
    public void delete(Location location) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.remove(location);
            tx.commit();
        }
    }
}
