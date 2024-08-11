package org.roadmap.weatherapp.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/searchAllByName")
public class SearchFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String cityName = req.getParameter("cityName");
        if (validate(cityName)) {
            chain.doFilter(req, res);
        } else {
            res.sendRedirect("/main");
        }
    }

    private boolean validate(String cityName) {
        return cityName != null && cityName.length() <= 20 && cityName.length() >= 2;
    }
}
