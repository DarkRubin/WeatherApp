package org.roadmap.weatherapp.service;

import org.roadmap.weatherapp.dao.DAO;
import org.roadmap.weatherapp.dao.UserDao;
import org.roadmap.weatherapp.exceptions.IncorrectEmailOrPasswordException;
import org.roadmap.weatherapp.exceptions.UserAlreadyExistException;
import org.roadmap.weatherapp.model.User;

public class UserService {

    private final DAO<User> dao = new UserDao();

    public User singIn(User user) throws IncorrectEmailOrPasswordException {
        return dao.search(user).orElseThrow(IncorrectEmailOrPasswordException::new);
    }

    public User singUp(User user) throws UserAlreadyExistException {
        try {
            return dao.save(user);
        } catch (Exception e) {
            throw new UserAlreadyExistException();
        }
    }
}
