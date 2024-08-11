package org.roadmap.weatherapp.dao;


import org.hibernate.Session;
import org.hibernate.Transaction;
import org.roadmap.weatherapp.model.UserSession;

import java.util.Optional;

public class SessionDao implements DAO<UserSession> {

    @Override
    public UserSession save(UserSession userSession) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(userSession);
            tx.commit();
        }
        return userSession;
    }

    @Override
    public Optional<UserSession> search(UserSession userSession) {
        return search(userSession.getId());
    }

    public Optional<UserSession> search(String id) {
        try (Session session = sessionFactory.openSession()) {
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
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from UserSession where id = :id")
                    .setParameter("id", userSession.getId())
                    .executeUpdate();
            tx.commit();
        }
    }
}
