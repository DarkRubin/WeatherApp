package org.roadmap.weatherapp.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.roadmap.weatherapp.dto.LocationDTO;
import org.roadmap.weatherapp.model.User;
import org.roadmap.weatherapp.service.LocationService;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "SearchController", value = "/search")
public class SearchController extends HttpServlet {

    private final ApplicationController controller = new ApplicationController();
    private final LocationService service = new LocationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext context = controller.getWebContext(req, resp, getServletContext());
        Optional<User> optionalUser = Optional.ofNullable((User) req.getSession().getAttribute("user"));
        optionalUser.or(() -> controller.getUserFromCookies(req));
        optionalUser.ifPresent(user -> context.setVariable("login", user.getLogin()));
        String cityName = req.getParameter("city").replace(' ', '+');
        if (isValid(cityName)) {
            search(cityName, context);
            controller.process("Search", resp.getWriter(), context);
        } else {
            context.setVariable("error", "Enter city name!");
            controller.process("Main", resp.getWriter(), context);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext context = controller.getWebContext(req, resp, getServletContext());
        PrintWriter writer = resp.getWriter();
        Optional<User> optionalUser = Optional.ofNullable((User) req.getSession().getAttribute("user"));
        optionalUser.or(() -> controller.getUserFromCookies(req));
        optionalUser.ifPresentOrElse(
                user -> addLocation(req, resp, user, context),
                () -> controller.process("Authorization", writer, context));

    }

    private void addLocation(HttpServletRequest req, HttpServletResponse resp, User user, WebContext context) {
        try {
            String name = req.getParameter("name");
            String latitude = req.getParameter("latitude");
            String longitude = req.getParameter("longitude");

            List<LocationDTO> searched = service.searchByCoordinates(latitude, longitude);
            LocationDTO locationToAdd = service.chooseLocationToAdd(searched, name);
            service.addLocationToUser(user, locationToAdd);
            resp.sendRedirect(req.getContextPath() + "/main");
        } catch (InterruptedException | URISyntaxException | IOException e) {
            context.setVariable("error", "Something went wrong!");
        }
    }

    private void search(String city, WebContext context) throws IOException {
        try {
            List<LocationDTO> locations = service.searchByName(city);
            if (locations == null) {
                context.setVariable("error", '\"' + city + '\"' + "not found");
            } else {
                context.setVariable("foundLocations", locations);
            }
        } catch (URISyntaxException | InterruptedException e) {
            context.setVariable("error", "Something went wrong!");
        }
    }

    private boolean isValid(String cityName) {
        return cityName != null && !cityName.isEmpty();
    }
}
