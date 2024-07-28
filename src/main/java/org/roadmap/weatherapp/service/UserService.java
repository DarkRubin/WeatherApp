package org.roadmap.weatherapp.service;

import org.roadmap.weatherapp.dao.DAO;
import org.roadmap.weatherapp.dao.UserDao;
import org.roadmap.weatherapp.exception.IncorrectEmailOrPasswordException;
import org.roadmap.weatherapp.exception.UserAlreadyRegistredException;
import org.roadmap.weatherapp.model.User;

import static at.favre.lib.crypto.bcrypt.BCrypt.*;

public class UserService {

    private static final int COST = 5;
    private final DAO<User> dao = new UserDao();

    public User signIn(String email, String password) throws IncorrectEmailOrPasswordException {
        User user = dao.search(new User(email, password)).orElseThrow(IncorrectEmailOrPasswordException::new);
        char[] inputPassword = password.toCharArray();
        char[] savedPassword = user.getPassword().toCharArray();
        if (verifyer().verify(inputPassword, savedPassword).verified) {
            return user;
        } else throw new IncorrectEmailOrPasswordException();
    }

    public User signUp(String email, String password) throws UserAlreadyRegistredException {
        try {
            String hashedPassword = withDefaults().hashToString(COST, password.toCharArray());
            User user = new User(email, hashedPassword);
            return dao.save(user);
        } catch (Exception e) {
            throw new UserAlreadyRegistredException();
        }

    }
}
