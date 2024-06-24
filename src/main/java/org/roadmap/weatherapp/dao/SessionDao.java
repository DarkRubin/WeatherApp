package org.roadmap.weatherapp.dao;


import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.roadmap.weatherapp.model.UserSession;

import java.util.Optional;

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
    public Optional<UserSession> search(UserSession userSession) {
        return search(userSession.getId());
    }

    public Optional<UserSession> search(String id) {
        try (Session session = sessionFactory.openSession()){
            return Optional.ofNullable(session.get(UserSession.class, id));
        }
    }

    @Override
    public UserSession update(UserSession userSession) {
        try (Session session = sessionFactory.openSession()) {
            return session.merge(userSession);
        }
    }

    @Override
    public void delete(UserSession userSession) {
        try (Session session = sessionFactory.openSession()) {
            session.remove(userSession);
        }
    }
}
