package org.roadmap.weatherapp.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.roadmap.weatherapp.service.SessionService;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.Arrays;


@WebServlet(name = "LogOutController", value = "/authentication/log-out")
public class LogOutController extends HttpServlet {

    private final SessionService sessionService = new SessionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().removeAttribute("user");
        req.removeAttribute("user");
        Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals("SESSION_ID"))
                .findFirst()
                .ifPresent(cookie -> sessionService.delete(cookie.getValue()));
        WebContext ctx = ApplicationController.getWebContext(req, resp);
        ApplicationController.process("Main", resp.getWriter(), ctx);
    }

}
