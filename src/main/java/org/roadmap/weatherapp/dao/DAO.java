package org.roadmap.weatherapp.dao;

import org.hibernate.SessionFactory;
import org.roadmap.weatherapp.model.Model;

public interface DAO<L extends Model> {

    SessionFactory sessionFactory = DbUtils.getSessionFactory();

    L save(L l);

    L search(L l);

    L update(L l);

    void delete(L l);

}

