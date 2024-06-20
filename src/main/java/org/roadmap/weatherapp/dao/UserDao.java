package org.roadmap.weatherapp.dao;

import org.hibernate.Session;
import org.roadmap.weatherapp.model.User;

public class UserDao implements DAO<User>{

    @Override
    public User save(User user) {
        try (Session session = sessionFactory.openSession()) {

        }
        return null;
    }

    @Override
    public User search(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void delete(User user) {

    }
}
