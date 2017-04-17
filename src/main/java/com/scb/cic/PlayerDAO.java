package com.scb.cic;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by saligh on 17/4/17.
 */
public class PlayerDAO {

    public void checkAllTables() {

        String query = "select * from pg_tables";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();

            int totalCols = rs.getMetaData().getColumnCount();
            System.out.println("TotalColumns: " + totalCols);
            while (rs.next()) {

                for (int colIdx=1 ; colIdx<=totalCols ; colIdx++) {

                    System.out.println("Column [" + colIdx + "] Value: " + rs.getString(colIdx));
                }
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
