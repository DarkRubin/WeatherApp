package org.roadmap.weatherapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.Writer;

@UtilityClass
public class ApplicationController {

    private static final TemplateEngine templateEngine;
    private static JakartaServletWebApplication application;

    static {
        var resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setPrefix("/templates/");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setSuffix(".html");

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
    }

    public static WebContext getWebContext(HttpServletRequest request, HttpServletResponse response) {
        if (application == null) {
            application = JakartaServletWebApplication.buildApplication(request.getServletContext());
        }
        return new WebContext(application.buildExchange(request, response));
    }

    public void process(String templateName, Writer writer, WebContext context) {
        templateEngine.process(templateName, context, writer);
    }
}
