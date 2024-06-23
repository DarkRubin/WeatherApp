package org.roadmap.weatherapp.service;

import org.roadmap.weatherapp.dao.DAO;
import org.roadmap.weatherapp.dao.LocationDao;
import org.roadmap.weatherapp.dao.UserDao;
import org.roadmap.weatherapp.model.Location;
import org.roadmap.weatherapp.model.User;

import java.math.BigDecimal;

public class SessionService {

    public void startSession(String email, String password) {

    }

    public static void main(String[] args) {
        DAO<User> dao = new UserDao();
        User user = dao.save(new User("login", "password"));
        DAO<Location> locationDao = new LocationDao();
        Location location = locationDao.save(new Location("London", user, BigDecimal.valueOf(51.5073219), BigDecimal.valueOf(-0.1276474)));
        System.out.println(user);
        System.out.println(location);
    }
}
