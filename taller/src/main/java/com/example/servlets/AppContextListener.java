package com.example.servlets;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.config.AppConfig;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    public static final String SPRING_CONTEXT_KEY = "SPRING_APPLICATION_CONTEXT";

    private AnnotationConfigApplicationContext springContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        springContext = new AnnotationConfigApplicationContext(AppConfig.class);
        sce.getServletContext().setAttribute(SPRING_CONTEXT_KEY, springContext);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (springContext != null) {
            springContext.close();
        }
    }

    public static ApplicationContext getSpringContext(ServletContext servletContext) {
        return (ApplicationContext) servletContext.getAttribute(SPRING_CONTEXT_KEY);
    }
}
