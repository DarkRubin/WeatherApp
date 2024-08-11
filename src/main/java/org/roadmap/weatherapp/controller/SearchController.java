package org.roadmap.weatherapp.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.roadmap.weatherapp.dto.LocationDTO;
import org.roadmap.weatherapp.exception.ApiNotResponseException;
import org.roadmap.weatherapp.exception.LocationAlreadyAddedException;
import org.roadmap.weatherapp.model.User;
import org.roadmap.weatherapp.service.LocationService;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/search")
public class SearchController extends HttpServlet {

    private final LocationService service = new LocationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext context = ApplicationController.getWebContext(req, resp);
        Optional.ofNullable((User) req.getAttribute("user"))
                .ifPresent(user -> setUserLogin(user, context));
        String cityName = req.getParameter("cityName").replace(' ', '+');
        search(cityName, context);
        ApplicationController.process("Search", resp.getWriter(), context);
    }

    private void setUserLogin(User user, WebContext context) {
        String[] split = user.getLogin().split("@");
        String login = split[0];
        context.setVariable("login", login);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var context = ApplicationController.getWebContext(req, resp);
        var writer = resp.getWriter();
        User user = (User) req.getAttribute("user");
        if (user == null) {
            ApplicationController.process("Authorization", writer, context);
        } else {
            try {
                addLocation(req, user);
                resp.sendRedirect(req.getContextPath() + "/main");
            } catch (ApiNotResponseException e) {
                context.setVariable("error", "Something went wrong!");
                ApplicationController.process("Search", writer, context);
            } catch (LocationAlreadyAddedException e) {
                context.setVariable("error", "This location already exists!");
                ApplicationController.process("Search", writer, context);
            }
        }
    }

    private void addLocation(HttpServletRequest req, User user) throws ApiNotResponseException {
        String name = req.getParameter("cityName");
        String latitude = req.getParameter("latitude");
        String longitude = req.getParameter("longitude");
        LocationDTO locationToAdd = service.searchByCoordinates(latitude, longitude, name);
        service.addLocationToUser(user, locationToAdd);
    }

    private void search(String city, WebContext context) {
        try {
            List<LocationDTO> locations = service.searchAllByName(city);
            if (locations == null) {
                context.setVariable("error", '\"' + city + '\"' + "not found");
            } else {
                context.setVariable("foundLocations", locations);
            }
        } catch (ApiNotResponseException e) {
            context.setVariable("error", "Something went wrong!");
        }
    }
}
