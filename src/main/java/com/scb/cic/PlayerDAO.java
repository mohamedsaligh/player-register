package com.scb.cic;

import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by saligh on 17/4/17.
 */
public class PlayerDAO {

    public void createPlayerTable() {

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DBConnection.getConnection();
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE PLAYER_INFO " +
                    "(NAME TEXT PRIMARY KEY     NOT NULL," +
                    " SKILL            TEXT     NOT NULL, " +
                    " TEAM        CHAR(50))";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        } finally {
            DBConnection.closeResources(null, stmt, c);
        }
        System.out.println("PLAYER_INFO Table created successfully");

        checkAllTables(null);
        System.out.println("Player_Info table checked");
    }

    public List<Map<String, String>> getAllPlayers() {

        List<Map<String, String>> playersList = new ArrayList<>();
        Map<String, String> playerDetailsMap = null;

        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String getPlayersSql = "Select * From PLAYER_INFO ";
        try {

            con = DBConnection.getConnection();
            pstmt = con.prepareStatement(getPlayersSql);
            rs = pstmt.executeQuery();

            while (rs.next()) {

                playerDetailsMap = new HashMap<>();
                playerDetailsMap.put("NAME", rs.getString("NAME"));
                playerDetailsMap.put("SKILL", rs.getString("SKILL"));
                playerDetailsMap.put("TEAM", rs.getString("TEAM"));

                playersList.add(playerDetailsMap);
            }
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        } finally {
            DBConnection.closeResources(rs, pstmt, con);
        }
        System.out.println("playersList: \n" + playersList);
        return playersList;
    }

    public void insertPlayer(String name, String skill, String team) {

        Connection con = null;
        PreparedStatement pstmt = null;
        String playerInsSql = "Insert into PLAYER_INFO (name, skill, team) values (?, ?, ?)";
        try {

            con = DBConnection.getConnection();
            pstmt = con.prepareStatement(playerInsSql);
            int pstmtIdx = 0;
            pstmt.setString(++pstmtIdx, name);
            pstmt.setString(++pstmtIdx, skill);
            pstmt.setString(++pstmtIdx, team);
            int insStatus = pstmt.executeUpdate();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        } finally {
            DBConnection.closeResources(null, pstmt, con);
        }
    }

    public void checkAllTables(String tableName) {

        String query = "select * from pg_tables ";
        if (tableName != null) {

            query = query + " WHERE TABLENAME = ?";
        }
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            pstmt = con.prepareStatement(query);
            if (tableName != null) {

                int pstmtIdx = 0;
                pstmt.setString(++pstmtIdx, tableName);
            }
            rs = pstmt.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int totalCols = rsmd.getColumnCount();
            System.out.println("Printing Column Names...");
            for (int col=1;col<totalCols;col++) {
                System.out.print(rsmd.getColumnLabel(col) + "\t");
            }
            System.out.println();
            System.out.println("TotalColumns: " + totalCols);
            while (rs.next()) {

                for (int colIdx=1 ; colIdx<=totalCols ; colIdx++) {

                    System.out.print("[" + rsmd.getColumnLabel(colIdx) + "]: " + rs.getString(colIdx) + "|");
                }
                System.out.println();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            DBConnection.closeResources(rs, pstmt, con);
        }

    }
}
