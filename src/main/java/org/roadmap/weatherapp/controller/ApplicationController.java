package org.roadmap.weatherapp.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.roadmap.weatherapp.model.User;
import org.roadmap.weatherapp.service.SessionService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.Writer;
import java.util.Optional;

public class ApplicationController {

    private static final TemplateEngine templateEngine;
    private final SessionService sessionService = new SessionService();
    private JakartaServletWebApplication application;

    static {
        var resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setPrefix("/templates/");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setSuffix(".html");

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
    }

    public WebContext getWebContext(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) {
        if (application == null) {
            application = JakartaServletWebApplication.buildApplication(servletContext);
        }
        return new WebContext(application.buildExchange(request, response));
    }

    public void process(String templateName, Writer writer, WebContext context) {
        templateEngine.process(templateName, context, writer);
    }

    protected Optional<User> getUserFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("SESSION_ID")) {
                String uuid = cookie.getValue();
                User user = sessionService.findUser(uuid);
                request.getSession().setAttribute("user", user);
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

}
