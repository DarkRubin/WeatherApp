package org.roadmap.weatherapp.dao;


import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.roadmap.weatherapp.model.UserSession;

public class SessionDao implements DAO<UserSession> {

    @Override
    @Transactional
    public UserSession save(UserSession userSession) {
        try (Session session = sessionFactory.openSession()) {
            session.persist(userSession);
        }
        return userSession;
    }

    @Override
    public UserSession search(UserSession userSession) {
        try (Session session = sessionFactory.openSession()) {
            session.persist(userSession);
        }
        return userSession;
    }

    @Override
    public UserSession update(UserSession userSession) {
        try (Session session = sessionFactory.openSession()) {
            return session.merge(userSession);
        }
    }

    @Override
    public void delete(UserSession userSession) {

    }
}
