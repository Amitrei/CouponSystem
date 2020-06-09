package com.amitrei.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBManager {
    private static final String url="jdbc:mysql://localhost:3306/couponsystem?createDatabaseIfNotExist=TRUE&useTimezone=TRUE&serverTimezone=UTC";
    private static final String user = "root";
    private static final String password="1234";
    private static Connection connection = null;



    public static String getUrl() {
        return url;
    }

    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }


    public static void createTable(String query) {

        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = query;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().restoreConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();

            }
            connection=null;
        }


        }

}
