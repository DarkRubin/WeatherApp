package org.roadmap.weatherapp.controller;

import jakarta.servlet.ServletException;
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
        String templateName = req.getContextPath().equals("sing-in") ? "Authorization" : "Registration";
        WebContext context = controller.getWebContext(req, resp, getServletContext());
        controller.process(templateName, resp.getWriter(), context);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        boolean isRegistration = Boolean.parseBoolean(request.getParameter("isRegistration"));

        User user = new User(email, password);
        try {
            user = isRegistration ? userService.singUp(user) : userService.singIn(user);
        } catch (UserAlreadyExistException | IncorrectEmailOrPasswordException e) {
            request.setAttribute("message", e.getMessage());
            getServletContext().getRequestDispatcher(isRegistration ? "/Registration.html" : "/Authorization.html")
                    .forward(request, response);
        }
        session.setAttribute("uuid", sessionService.startSession(user));
        response.sendRedirect("/main");
    }
}
