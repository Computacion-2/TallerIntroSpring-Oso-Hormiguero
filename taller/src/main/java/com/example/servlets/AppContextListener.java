package com.example.servlets;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    public static final String SPRING_CONTEXT_KEY = "SPRING_APPLICATION_CONTEXT";

    private ClassPathXmlApplicationContext springContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        springContext = new ClassPathXmlApplicationContext("applicationContext.xml");
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
