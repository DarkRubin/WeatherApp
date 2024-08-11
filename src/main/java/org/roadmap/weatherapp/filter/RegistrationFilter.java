package org.roadmap.weatherapp.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.roadmap.weatherapp.controller.ApplicationController;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@WebFilter(urlPatterns = {"/authentication/sign-up"})
public class RegistrationFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        WebContext ctx = ApplicationController.getWebContext(req, res);
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String method = req.getParameter("method");

        if (method != null && method.equals("post")) {
            validateEmail(email, ctx);
            validatePassword(password, ctx);
            if (ctx.getVariable("error") != null)  {
                ApplicationController.process("Registration", res.getWriter(), ctx);
            } else {
                chain.doFilter(req, res);
            }
        } else {
            chain.doFilter(req, res);
        }

    }

    private void validateEmail(String email, WebContext ctx) {
        if (email == null || email.length() < 5) {
            ctx.setVariable("error", "Email must be at least 5 characters");
        }
    }

    private void validatePassword(String password, WebContext ctx) {
        if (password == null || password.length() < 8) {
            ctx.setVariable("error", "Password must be at least 8 characters");
        } else {
            int letters = 0;
            int numbers = 0;
            for (char c : password.toCharArray()) {
                if (Character.isDigit(c)) {
                    numbers++;
                } else {
                    letters++;
                }
            }
            if (numbers < 1) {
                ctx.setVariable("error", "Password must be at least 1 number");
            } else if (letters < 1) {
                ctx.setVariable("error", "Password must be at least 1 character letter");
            }
        }
    }

}
