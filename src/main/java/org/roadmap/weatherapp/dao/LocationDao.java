package org.roadmap.weatherapp.dao;

import org.hibernate.SessionFactory;
import org.roadmap.weatherapp.model.Location;

public class LocationDao implements DAO<Location> {

    private final SessionFactory sessionFactory = DbUtils.getSessionFactory();

    @Override
    public Location save(Location location) {
        return null;
    }

    @Override
    public Location search(Location location) {
        return null;
    }

    @Override
    public Location update(Location location) {
        return null;
    }

    @Override
    public void delete(Location location) {

    }
}
