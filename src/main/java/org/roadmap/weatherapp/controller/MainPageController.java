package org.roadmap.weatherapp.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.roadmap.weatherapp.dao.DAO;
import org.roadmap.weatherapp.dao.LocationDao;
import org.roadmap.weatherapp.dao.UserDao;
import org.roadmap.weatherapp.dto.LocationForecast;
import org.roadmap.weatherapp.exception.ApiNotResponseException;
import org.roadmap.weatherapp.model.Location;
import org.roadmap.weatherapp.model.User;
import org.roadmap.weatherapp.service.WeatherService;
import org.roadmap.weatherapp.util.PaginationResult;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "MainPageController", urlPatterns = "/main")
public class MainPageController extends HttpServlet {

    private final UserDao dao = new UserDao();
    private final WeatherService service = new WeatherService();
    private final DAO<Location> locationDao = new LocationDao();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext ctx = ApplicationController.getWebContext(req, resp);
        User user = (User) req.getAttribute("user");
        if (user == null) {
            ApplicationController.process("Main", resp.getWriter(), ctx);
            return;
        }
        String pageParameter = req.getParameter("page");
        int page = pageParameter == null ? 1 : Integer.parseInt(pageParameter);
        setUserLogin(user, ctx);

        PaginationResult<Location> userLocations = dao.getLocations(user, page);
        List<LocationForecast> locations = getForecast(userLocations.getPageContent(), ctx);

        ctx.setVariable("currentPage", page);
        ctx.setVariable("totalPages", (userLocations.getTotalPageCount()));
        ctx.setVariable("locationsToShow", locations);

        ApplicationController.process("Main", resp.getWriter(), ctx);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String latitude = req.getParameter("latitude");
        String longitude = req.getParameter("longitude");
        int id = Integer.parseInt(req.getParameter("id"));
        User user = (User) req.getAttribute("user");
        Location location = new Location();
        location.setId(id);
        location.setUser(user);
        location.setLatitude(new BigDecimal(latitude));
        location.setLongitude(new BigDecimal(longitude));
        locationDao.delete(location);

        doGet(req, resp);
    }

    private void setUserLogin(User user, WebContext context) {
        String[] split = user.getLogin().split("@");
        String login = split[0];
        context.setVariable("login", login);
    }

    private List<LocationForecast> getForecast(List<Location> locations, WebContext context) {
        try {
            return service.getForecasts(locations);
        } catch (ApiNotResponseException e) {
            context.setVariable("error", "Something went wrong!");
            return new ArrayList<>();
        }
    }
}
