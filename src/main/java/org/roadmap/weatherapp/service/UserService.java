package org.roadmap.weatherapp.service;

import org.roadmap.weatherapp.dao.DAO;
import org.roadmap.weatherapp.dao.UserDao;
import org.roadmap.weatherapp.exception.IncorrectEmailOrPasswordException;
import org.roadmap.weatherapp.exception.UserAlreadyRegistredException;
import org.roadmap.weatherapp.model.User;

public class UserService {

    private final DAO<User> dao = new UserDao();

    public User signIn(User user) throws IncorrectEmailOrPasswordException {
        return dao.search(user).orElseThrow(IncorrectEmailOrPasswordException::new);
    }

    public User signUp(User user) throws UserAlreadyRegistredException {
        try {
            return dao.save(user);
        } catch (Exception e) {
            throw new UserAlreadyRegistredException();
        }

    }
}
