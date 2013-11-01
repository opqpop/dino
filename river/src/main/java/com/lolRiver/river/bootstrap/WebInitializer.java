package com.lolRiver.river.bootstrap;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.Set;

public class WebInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) {
        // Create the dispatcher servlet's Spring application context
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.scan("com.lolRiver.river.bootstrap");

        // Manage the lifecycle of the root application context
        container.addListener(new ContextLoaderListener(dispatcherContext));

        //Register and map the dispatcher servlet
        ServletRegistration.Dynamic dispatcher =
        container.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));

        dispatcher.setLoadOnStartup(1);
        Set<String> mappingConflicts = dispatcher.addMapping("/views/*");
        if (!mappingConflicts.isEmpty()) {
            throw new IllegalStateException("'dispatcher' could not be mapped to '/' due " +
                                            "to an existing mapping. This is a known issue under Tomcat versions " +
                                            "<= 7.0.14; see <a href=\"https://issues.apache.org/bugzilla/show_bug.cgi?id=51278\">" +
                                            "https://issues.apache.org/bugzilla/show_bug.cgi?id=51278</a>");
        }
    }
}