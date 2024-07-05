package org.roadmap.weatherapp.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.roadmap.weatherapp.dto.LocationDTO;
import org.roadmap.weatherapp.exceptions.LocationNotFoundException;
import org.roadmap.weatherapp.model.User;
import org.roadmap.weatherapp.service.LocationService;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@WebServlet(name = "SearchController", value = "/search")
public class SearchController extends HttpServlet {

    private final ApplicationController controller = new ApplicationController();
    private final LocationService service = new LocationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext context = controller.getWebContext(req, resp, getServletContext());
        String cityName = req.getParameter("city");
        if (isValid(cityName)) {
            search(cityName, context);
            controller.process("Search", resp.getWriter(), context);
        } else {
            context.setVariable("error", "Enter city name!");
            controller.process("Main", resp.getWriter(), context);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO
        WebContext context = controller.getWebContext(req, resp, getServletContext());
        User user = (User) req.getSession().getAttribute("user");
        String locationToAdd = req.getParameter("locationToAdd");
//        service.addLocationToUser(user, locationToAdd);

    }

    private void search(String city, WebContext context) throws IOException {
        try {
            List<LocationDTO> locations = service.searchLocation(city);
            if (locations == null) {
                throw new LocationNotFoundException();
            }
            context.setVariable("locationsToShow", locations);
        } catch (URISyntaxException | InterruptedException e) {
            context.setVariable("error", "Something went wrong!");
        } catch (LocationNotFoundException e) {
            context.setVariable("error", '\"' + city + '\"' + "not found");
        }
    }

    private boolean isValid(String cityName) {
        return cityName != null && !cityName.isEmpty();
    }
}
