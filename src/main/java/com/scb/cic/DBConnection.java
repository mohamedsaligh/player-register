package com.scb.cic;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

/**
 * Created by saligh on 17/4/17.
 *
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

    public static void closeResources(ResultSet rs, Statement stmt, Connection con) {

        try {
            if (rs != null) rs.close();
        } catch(Exception ex) {
            System.err.println("Exception in closing ResultSet: " + ex);
        }
        try {
            if (stmt != null) stmt.close();
        } catch(Exception ex) {
            System.err.println("Exception in closing Statement: " + ex);
        }
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch(Exception ex) {
            System.err.println("Exception in closing connection: " + ex);
        }
    }
}