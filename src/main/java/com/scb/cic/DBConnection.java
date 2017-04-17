package com.scb.cic;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by saligh on 17/4/17.
 */
public class DBConnection {

    public static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));
        System.out.println("DATABASE_URL: " + dbUri.getHost() + "|" + dbUri.getPort());
        System.out.println("DATABASE_URL from System env: " + System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        System.out.println("username: " + username);
        String password = dbUri.getUserInfo().split(":")[1];
        System.out.println("password: " + password);
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        System.out.println("postgre dbUrl: " + dbUrl);

        return DriverManager.getConnection(dbUrl, username, password);
    }

}