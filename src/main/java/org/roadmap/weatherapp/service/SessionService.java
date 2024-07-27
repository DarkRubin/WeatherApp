package org.roadmap.weatherapp.service;

import jakarta.servlet.http.Cookie;
import org.roadmap.weatherapp.dao.SessionDao;
import org.roadmap.weatherapp.exception.SessionExpiresException;
import org.roadmap.weatherapp.exception.UserNotAuthorizedException;
import org.roadmap.weatherapp.model.User;
import org.roadmap.weatherapp.model.UserSession;

import java.time.Instant;
import java.util.Date;


public class SessionService {

    public static final int SESSION_LIFE_TIME_IN_HOURS = 6;
    private final SessionDao dao = new SessionDao();

    public Cookie startSession(User user) {
        String uuid = dao.save(new UserSession(user, SESSION_LIFE_TIME_IN_HOURS)).getId();
        Cookie cookie = new Cookie("SESSION_ID", uuid);
        cookie.setMaxAge(3600 * SESSION_LIFE_TIME_IN_HOURS);
        return cookie;
    }

    public void delete(String uuid) {
        UserSession session = new UserSession();
        session.setId(uuid);
        dao.delete(session);
    }

    public User findUser(String uuid) {
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
