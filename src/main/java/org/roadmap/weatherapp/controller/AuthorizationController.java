package org.roadmap.weatherapp.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.roadmap.weatherapp.exception.IncorrectEmailOrPasswordException;
import org.roadmap.weatherapp.model.User;
import org.roadmap.weatherapp.service.SessionService;
import org.roadmap.weatherapp.service.UserService;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@WebServlet(name = "Authorization", urlPatterns = "/authentication/sign-in")
public class AuthorizationController extends HttpServlet {

    private final UserService userService = new UserService();
    private final SessionService sessionService = new SessionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext context = ApplicationController.getWebContext(req, resp);
        context.setVariable("error", req.getAttribute("message"));
        ApplicationController.process("Authorization", resp.getWriter(), context);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext context = ApplicationController.getWebContext(req, resp);
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            User user = userService.signIn(email, password);
            String contextPath = req.getContextPath();
            resp.addCookie(sessionService.startSession(user, contextPath));
            req.getSession().setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/main");
        } catch (IncorrectEmailOrPasswordException e) {
            context.setVariable("error", "Email or password is incorrect");
            ApplicationController.process("Authorization", resp.getWriter(), context);
        }
    }
}
