package org.roadmap.weatherapp.service;

import jakarta.servlet.http.Cookie;
import org.roadmap.weatherapp.dao.SessionDao;
import org.roadmap.weatherapp.exception.PropertiesFileNotFoundException;
import org.roadmap.weatherapp.exception.SessionExpiresException;
import org.roadmap.weatherapp.exception.UserNotAuthorizedException;
import org.roadmap.weatherapp.model.User;
import org.roadmap.weatherapp.model.UserSession;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;


public class SessionService {

    public final int sessionLifetimeInHours;
    private final SessionDao dao = new SessionDao();

    public SessionService() {
        InputStream propertiesFile = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("session.properties");
        try (propertiesFile) {
            Properties properties = new Properties();
            properties.load(propertiesFile);
            sessionLifetimeInHours = Integer.parseInt(properties.getProperty("life_time"));
        } catch (IOException e) {
            throw new PropertiesFileNotFoundException();
        }
    }

    public Cookie startSession(User user) {
        String uuid = dao.save(new UserSession(user, sessionLifetimeInHours)).getId();
        Cookie cookie = new Cookie("SESSION_ID", uuid);
        cookie.setMaxAge(3600 * sessionLifetimeInHours);
        cookie.setPath("/WeatherApp");
        return cookie;
    }

    public void delete(String uuid) {
        UserSession session = new UserSession();
        session.setId(uuid);
        dao.delete(session);
    }

    public User findUser(String uuid) throws UserNotAuthorizedException {
        UserSession session = dao.search(uuid).orElseThrow(UserNotAuthorizedException::new);
        Instant instant = new Date().toInstant();
        Instant expiresAt = session.getExpiresAt();
        if (instant.isAfter(expiresAt)) {
            dao.delete(session);
            throw new SessionExpiresException();
        } else {
            return session.getUser();
        }
    }
}
