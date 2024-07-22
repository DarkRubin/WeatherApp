package org.roadmap.weatherapp.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.roadmap.weatherapp.dao.UserDao;
import org.roadmap.weatherapp.dto.LocationForecast;
import org.roadmap.weatherapp.model.Location;
import org.roadmap.weatherapp.model.User;
import org.roadmap.weatherapp.service.LocationService;
import org.roadmap.weatherapp.service.WeatherService;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "MainPageController", urlPatterns = "/main")
public class MainPageController extends HttpServlet {

    private final ApplicationController controller = new ApplicationController();
    private final UserDao dao = new UserDao();
    private final WeatherService service = new WeatherService();
    private final LocationService locationService = new LocationService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = controller.getWebContext(request, response, getServletContext());
        Optional<User> optionalUser = Optional.ofNullable((User) request.getSession().getAttribute("user"));
        optionalUser.or(() -> controller.getUserFromCookies(request));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Location> userLocations = dao.getLocations(user);
            List<LocationForecast> locations = getUserLocationsWithWeatherInfo(userLocations, context);
            context.setVariable("login", user.getLogin());
            context.setVariable("locationsToShow", locations);
            controller.process("Main", response.getWriter(), context);
        } else {
            controller.process("MainForNotAuthorized", response.getWriter(), context);
        }
    }


    private List<LocationForecast> getUserLocationsWithWeatherInfo(List<Location> locations, WebContext context) throws IOException {
        List<LocationForecast> result = new ArrayList<>();
        try {
            for (Location location : locations) {
                result.add(service.getForecastForNow(location));
            }
        } catch (URISyntaxException | InterruptedException e) {
            context.setVariable("error", "Something went wrong!");
        }
        return result;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String latitude = req.getParameter("latitude");
        String longitude = req.getParameter("longitude");
        User user = (User) req.getSession().getAttribute("user");
        locationService.deleteLocation(latitude, longitude, user);
        doGet(req, resp);
    }
}
