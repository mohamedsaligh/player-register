package com.scb.cic;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;
import sun.applet.Main;

import java.net.URI;
import java.net.URL;
import java.sql.Connection;

/**
 * Hello world!
 *
 */
public class App {

    public static void main( String[] args ) throws Exception {
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }

        final Server server = new Server(Integer.valueOf(webPort));

        ResourceConfig config = new ResourceConfig();
        config.packages("com.scb.cic");
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        ServletContextHandler context = new ServletContextHandler(server, "/api/*");
        context.addServlet(servlet, "/*");
        server.setHandler(context);

        // Web
        ResourceHandler webHandler = new ResourceHandler();
        webHandler.setResourceBase("./target/classes/");
        webHandler.setWelcomeFiles(new String[]{"index.html"});

        // Server
        HandlerCollection handlers = new HandlerCollection();
        handlers.addHandler(context);
        handlers.addHandler(webHandler);

        server.setHandler(handlers);
        server.start();

        //DBConnection
        Connection dbConn = null;
        try {
            System.out.println("Going to get DB Connection...");
            dbConn = DBConnection.getConnection();
        } finally {
            DBConnection.closeResources(null, null, dbConn);
        }

        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }
}
