package org.roadmap.weatherapp.dao;

import org.hibernate.SessionFactory;
import org.roadmap.weatherapp.model.Model;

import java.util.Optional;

public interface DAO<L extends Model> {

    SessionFactory sessionFactory = DbUtils.getSessionFactory();

    L save(L l);

    Optional<L> search(L l);

    L update(L l);

    void delete(L l);

}

