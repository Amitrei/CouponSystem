package com.amitrei.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

public class ConnectionPool {
    private Stack<Connection> connections = new Stack<>();


    private static ConnectionPool instance = null;

    private ConnectionPool() throws SQLException {
        Connection connection = null;
        for (int i = 1; i <= 10; i++) {
            connection = DriverManager.getConnection(DBManager.getUrl(), DBManager.getUser(), DBManager.getPassword());
            connections.push(connection);
            System.out.println("Connection number #" + i + " created successfuly");

        }

    }

    public static ConnectionPool getInstance() throws SQLException {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws InterruptedException {

        synchronized (connections) {
            while (connections.isEmpty()) {
                connections.wait();
            }

            return connections.pop();
        }
    }

    public void restoreConnection(Connection connection) {
        synchronized (connections) {
            connections.push(connection);
            connections.notify();
        }
    }


    public void closeAllConnections() throws InterruptedException {
        synchronized (connections) {
            if (connections.size() < 10) {
                connections.wait();
            }

            for (Connection conn : connections) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
