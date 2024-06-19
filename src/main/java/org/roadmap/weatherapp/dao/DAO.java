package org.roadmap.weatherapp.dao;

import org.roadmap.weatherapp.model.Model;

public interface DAO<L extends Model> {

    Class<L> save(L l);

    Class<L> search(L l);

    Class<L> update(L l);

    Class<L> delete(L l);

}

