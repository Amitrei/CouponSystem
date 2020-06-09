package com.amitrei.utils;

import com.amitrei.db.ConnectionPool;

import java.sql.*;

public class getIdFromDB {
    private static Connection connection = null;

    /**
     * Getting the ID from the database to the company bean
     */
    public static int getCompanyID(String name, String email) throws SQLException, InterruptedException {
        int companyID = -1;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT `ID` FROM couponsystem.companies WHERE NAME='" + name + "' AND EMAIL='" + email + "';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                companyID=resultSet.getInt(1);
            }
        } finally {
        ConnectionPool.getInstance().restoreConnection(connection);
        connection=null;
        }

        return companyID;
    }
}
