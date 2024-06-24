package org.roadmap.weatherapp.service;

import org.roadmap.weatherapp.dao.DAO;
import org.roadmap.weatherapp.dao.UserDao;
import org.roadmap.weatherapp.exceptions.IncorrectEmailOrPasswordException;
import org.roadmap.weatherapp.model.User;

public class UserService {

    private final DAO<User> dao = new UserDao();

    public User singIn(String email, String password) {
        return dao.search(new User(email, password)).orElseThrow(IncorrectEmailOrPasswordException::new);
    }

    public User singUp(String email, String password) {
        return dao.save(new User(email, password));
    }
}
