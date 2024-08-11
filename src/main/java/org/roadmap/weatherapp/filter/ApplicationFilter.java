package org.roadmap.weatherapp.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.roadmap.weatherapp.exception.UserNotAuthorizedException;
import org.roadmap.weatherapp.model.User;
import org.roadmap.weatherapp.service.SessionService;

import java.io.IOException;
import java.util.Arrays;

@WebFilter(urlPatterns = "/*")
public class ApplicationFilter extends HttpFilter {

    private final SessionService sessionService = new SessionService();

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        if (cookies != null && req.getAttribute("user") == null) {
            Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("SESSION_ID"))
                    .findFirst()
                    .ifPresent(cookie -> setUser(req, cookie.getValue()));
        }

        chain.doFilter(req, resp);
    }

    private void setUser(HttpServletRequest request, String uuid) {
        User user;
        try {
            user = sessionService.findUser(uuid);
        } catch (UserNotAuthorizedException ignored) {
            return;
        }
        request.getSession().setAttribute("user", user);
        request.setAttribute("user", user);

    }
}
