package org.roadmap.weatherapp.service;

import org.roadmap.weatherapp.dao.DAO;
import org.roadmap.weatherapp.dao.SessionDao;
import org.roadmap.weatherapp.dao.UserDao;
import org.roadmap.weatherapp.exceptions.UserNotAuthorized;
import org.roadmap.weatherapp.model.User;
import org.roadmap.weatherapp.model.UserSession;

import java.io.IOException;
import java.net.URISyntaxException;


public class SessionService {

    public static final int SESSION_LIFE_TIME_IN_HOURS = 6;
    private final SessionDao dao = new SessionDao();

    public String startSession(User user) {
        return dao.save(new UserSession(user, SESSION_LIFE_TIME_IN_HOURS)).getId();
    }

    public User getSession(String uuid) {
        return dao.search(uuid).orElseThrow(UserNotAuthorized::new).getUser();
    }

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        DAO<User> dao = new UserDao();
        User user = dao.search(new User("admin", "hpWuh3sdh781")).get();
        new SessionService().startSession(user);
        System.out.println(new LocationService().searchLocation("New-York"));
        System.out.println(user);
    }
}
