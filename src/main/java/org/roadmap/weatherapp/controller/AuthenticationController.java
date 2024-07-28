package org.roadmap.weatherapp.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.roadmap.weatherapp.exception.IncorrectEmailOrPasswordException;
import org.roadmap.weatherapp.exception.UserAlreadyRegistredException;
import org.roadmap.weatherapp.model.User;
import org.roadmap.weatherapp.service.SessionService;
import org.roadmap.weatherapp.service.UserService;
import org.thymeleaf.context.WebContext;

import java.io.IOException;


@WebServlet(name = "AuthenticationController", value = "/authentication/*")
public class AuthenticationController extends HttpServlet {

    private final ApplicationController controller = new ApplicationController();
    private final SessionService sessionService = new SessionService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String templateName = switch (req.getPathInfo()) {
            case "/log-out" -> logOut(req);
            case "/sign-up" -> "Registration";
            case "/sign-in" -> "Authorization";
            default -> throw new IllegalStateException("Unexpected value: " + req.getPathInfo());
        };
        WebContext context = controller.getWebContext(req, resp, getServletContext());
        context.setVariable("error", req.getAttribute("message"));
        controller.process(templateName, resp.getWriter(), context);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try {
//            validate(email, password);
            User user = request.getPathInfo().equals("/sign-up") ?
                    userService.signUp(email, password) : userService.signIn(email, password);
            response.addCookie(sessionService.startSession(user));
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/main");
        } catch (UserAlreadyRegistredException | IncorrectEmailOrPasswordException e) {
            request.setAttribute("message", e.getMessage());
            doGet(request, response);
        }
    }

    private String logOut(HttpServletRequest req) {
        req.getSession().removeAttribute("user");
        for (Cookie cookie : req.getCookies()) {
            if (cookie.getName().equals("SESSION_ID")) {
                sessionService.delete(cookie.getValue());
            }
        }
        return "MainForNotAuthorized";
    }

    private void validate(String email, String password) throws IncorrectEmailOrPasswordException {
        if (email == null || password == null) {

            throw new IncorrectEmailOrPasswordException();
        }
    }

}
