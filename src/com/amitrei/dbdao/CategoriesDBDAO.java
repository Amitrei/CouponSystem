package com.amitrei.dbdao;

import com.amitrei.beans.Category;
import com.amitrei.dao.CategoriesDAO;
import com.amitrei.db.ConnectionPool;
import com.amitrei.exceptions.AlreadyExistsException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoriesDBDAO implements CategoriesDAO {


    @Override
    public void loadOneCategories(Category category) {

        if (isCategoryExists(category)) try {
            throw new AlreadyExistsException(category.toString());


        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
            return;

        }

        Connection connection = null;
        try {

            connection = ConnectionPool.getInstance().getConnection();
            String sql = "INSERT INTO `couponsystem`.`categories` (`ID`, `NAME`) VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, category.ordinal()+1);
            preparedStatement.setString(2, category.toString());
            preparedStatement.executeUpdate();

        } catch (InterruptedException | SQLException e) {
            System.out.println(e.getMessage());

        }finally {
            try {
                ConnectionPool.getInstance().restoreConnection(connection);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            connection = null;
        }
    }

    @Override
    public void loadAllCategories() {
        Connection connection = null;
        try {

            connection = ConnectionPool.getInstance().getConnection();
            String sql = "INSERT INTO `couponsystem`.`categories` (`ID`, `NAME`) VALUES (?, ?);";
            for (Category category : Category.values()) {
                try {
                    if (isCategoryExists(category)) throw new AlreadyExistsException(category.toString());
                }
                catch (AlreadyExistsException e) {
                    System.out.println(e.getMessage());
                    continue;
                }
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, category.ordinal() + 1);
                preparedStatement.setString(2, category.toString());
                preparedStatement.executeUpdate();
                System.out.println("Category: " + category.toString() + " loaded successfully");
            }
        } catch (InterruptedException | SQLException e) {
            System.out.println(e.getMessage());
        }
     finally {
            try {
                ConnectionPool.getInstance().restoreConnection(connection);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            connection = null;
        }
    }

    @Override
    public Boolean isCategoryExists(Category category) {
        Connection connection = null;
        try {
            int isExists = -99;
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT EXISTS(SELECT 1 FROM `couponsystem`.`categories` WHERE `NAME`=? LIMIT 1)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, category.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                isExists = resultSet.getInt(1);
            }
            return isExists > 0;

        } catch (InterruptedException | SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().restoreConnection(connection);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            connection = null;
        }
        return false;

    }


}

