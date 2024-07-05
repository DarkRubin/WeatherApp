package org.roadmap.weatherapp.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.roadmap.weatherapp.exceptions.IncorrectEmailOrPasswordException;
import org.roadmap.weatherapp.exceptions.UserAlreadyExistException;
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
        String templateName = req.getPathInfo().equals("/sign-in") ? "Authorization" : "Registration";
        WebContext context = controller.getWebContext(req, resp, getServletContext());
        controller.process(templateName, resp.getWriter(), context);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = new User();
        user.setLogin(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        try {
            user = request.getPathInfo().equals("/sign-up") ? userService.signUp(user) : userService.signIn(user);
            session.setAttribute("uuid", sessionService.startSession(user));
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/main");
        } catch (UserAlreadyExistException | IncorrectEmailOrPasswordException e) {
            request.setAttribute("message", e.getMessage());
            doGet(request, response);
        }
    }
}
