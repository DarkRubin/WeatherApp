package org.roadmap.weatherapp.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.roadmap.weatherapp.exception.UserAlreadyRegisteredException;
import org.roadmap.weatherapp.model.User;
import org.roadmap.weatherapp.service.SessionService;
import org.roadmap.weatherapp.service.UserService;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@WebServlet(name = "Registration", urlPatterns = "/authentication/sign-up")
public class RegistrationController extends HttpServlet {

    private final UserService userService = new UserService();
    private final SessionService sessionService = new SessionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext context = ApplicationController.getWebContext(req, resp);
        ApplicationController.process("Registration", resp.getWriter(), context);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext context = ApplicationController.getWebContext(req, resp);
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            User user = userService.signUp(email, password);
            resp.addCookie(sessionService.startSession(user));
            req.getSession().setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/main");
        } catch (UserAlreadyRegisteredException e) {
            context.setVariable("error", e.getMessage());
            ApplicationController.process("Registration", resp.getWriter(), context);
        }
    }
}
