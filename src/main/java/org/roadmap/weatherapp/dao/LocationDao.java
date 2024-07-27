package org.roadmap.weatherapp.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.roadmap.weatherapp.model.Location;
import org.roadmap.weatherapp.model.User;

import java.util.Optional;

public class LocationDao implements DAO<Location> {

    @Override
    public Location save(Location location) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(location);
            tx.commit();
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
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());
        delete(latitude, longitude, location.getUser());
    }

    public void delete(String latitude, String longitude, User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from Location where user = :user " +
                            "and latitude = :latitude " +
                            "and longitude = :longitude")
                    .setParameter("user", user)
                    .setParameter("latitude", latitude)
                    .setParameter("longitude", longitude)
                    .executeUpdate();
            tx.commit();
        }
    }
}
