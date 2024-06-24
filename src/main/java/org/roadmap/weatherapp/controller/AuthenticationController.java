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

import java.io.IOException;

@WebServlet(name = "AuthenticationController", value = "/authentication/*")
public class AuthenticationController extends HttpServlet {

    private final SessionService sessionService = new SessionService();
    private final UserService userService = new UserService();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = null;
        try {
            user = userService.singIn(email, password);
        } catch (IncorrectEmailOrPasswordException e) {
            request.setAttribute("message", e.getMessage());
            getServletContext().getRequestDispatcher("/Authorization").forward(request, response);
        }
        HttpSession session = request.getSession();
        session.setAttribute("uuid", sessionService.startSession(user).toString());
        response.sendRedirect("/main");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = null;
        try {
            user = userService.singUp(email, password);
        } catch (UserAlreadyExistException e) {
            request.setAttribute("message", e.getMessage());
            getServletContext().getRequestDispatcher("/Registration").forward(request, response);
        }
        HttpSession session = request.getSession();
        session.setAttribute("uuid", sessionService.startSession(user));
        response.sendRedirect("/main");

    }
}
