package service;

import org.junit.jupiter.api.Test;
import org.roadmap.weatherapp.dao.UserDao;
import org.roadmap.weatherapp.exception.UserAlreadyRegisteredException;
import org.roadmap.weatherapp.model.User;
import org.roadmap.weatherapp.service.UserService;

import java.util.Optional;

class AuthorizationTest {

    User userToSave = new User("myEmail@mail.com", "password");


    @Test
    void singUpTest() {
        UserService userService = new UserService();
        UserDao userDao = new UserDao();
        try {
            User returned = userService.signUp(userToSave.getLogin(), userToSave.getPassword());
            Optional<User> searched = userDao.search(userToSave);
            assert searched.isPresent();
            User userInDB = searched.get();

            assert returned.getId().equals(userInDB.getId());
            assert returned.getLogin().equals(userInDB.getLogin());
            assert returned.getPassword().equals(userInDB.getPassword());
        } catch (UserAlreadyRegisteredException e) {
            assert false;
        }
    }

    @Test
    void signInTest() {

    }

}
