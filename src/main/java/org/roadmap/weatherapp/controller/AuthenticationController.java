package org.roadmap.weatherapp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.roadmap.weatherapp.service.SessionService;

import java.io.IOException;

@WebServlet(name = "AuthenticationController", value = "/authentication/*")
public class AuthenticationController extends HttpServlet {

    private final SessionService service = new SessionService();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        request.getParameter("remember");

        String message = validate(email, password);
        if (message.equals("valid")) {
            service.startSession();
        } else {
            request.setAttribute("message", message);
            getServletContext().getRequestDispatcher("/authorization").forward(request, response);
        }
    }

    private String validate(String email, String password) {
        if (!email.contains("@")) {
            return "Email must contain: '@'";
        }
        String[] emailParts = email.split("@");
        if (emailParts.length > 2) {
            return "Email must contain only one: '@'";
        }
        return "valid";
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
