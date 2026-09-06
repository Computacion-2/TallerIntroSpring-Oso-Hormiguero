package com.example.servlets;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@WebListener
public class AppContextListener implements ServletContextListener {

    public static final String SPRING_CONTEXT_KEY = "SPRING_APPLICATION_CONTEXT";
    private ApplicationContext springContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ApplicationContext existing = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
        if (existing != null) {
            springContext = existing;
        } else {
            springContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        }
        sce.getServletContext().setAttribute(SPRING_CONTEXT_KEY, springContext);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (springContext instanceof ConfigurableApplicationContext cac && !(springContext instanceof org.springframework.web.context.WebApplicationContext)) {
            cac.close();
        }
    }

    public static ApplicationContext getSpringContext(jakarta.servlet.ServletContext servletContext) {
        ApplicationContext ctx = (ApplicationContext) servletContext.getAttribute(SPRING_CONTEXT_KEY);
        if (ctx == null) {
            ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        }
        return ctx;
    }
}
