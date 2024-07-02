package org.roadmap.weatherapp.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@WebServlet(name = "MainPageController", urlPatterns = "/main", loadOnStartup = 1)
public class MainPageController extends HttpServlet {

    private final ApplicationController controller = new ApplicationController();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = controller.getWebContext(request, response, getServletContext());
        controller.process("Main", response.getWriter(), context);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

    }
}
