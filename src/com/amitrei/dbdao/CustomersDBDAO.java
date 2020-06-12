package com.amitrei.dbdao;

import com.amitrei.beans.Customer;
import com.amitrei.dao.CustomersDAO;
import com.amitrei.db.ConnectionPool;
import com.amitrei.exceptions.CustomerExceptions.CustomerAlreadyExistsException;
import com.amitrei.exceptions.CustomerExceptions.CustomerDoesNotExists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomersDBDAO implements CustomersDAO {

    private Connection connection = null;


    /**
     * Using SELECT EXISTS LIMIT 1 in order to get the results:
     * 1 for exists company in the Database
     * 0 for non-exists company in the Database
     */


    public Boolean isCustomerExists(String email, String password) {

        try {
            int isExists = -99;
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT EXISTS(SELECT 1 FROM `couponsystem`.`customers` WHERE `EMAIL` =? AND `PASSWORD`=? LIMIT 1)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
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


    public Boolean isCustomerExists(int customerID) {

        try {
            int isExists = -99;
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT EXISTS(SELECT 1 FROM `couponsystem`.`customers` WHERE `ID`=? LIMIT 1)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerID);
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

    public void addCustomer(Customer... customers) {
        /**
         * Making a local connection to prevent NullPointerException because of the isCustomerExists method and the finally block.
         */
        Connection connection2 = null;
        try {
            connection2 = ConnectionPool.getInstance().getConnection();
            String sql = "INSERT INTO `couponsystem`.`customers` (`FIRST_NAME`, `LAST_NAME`,`EMAIL`,`PASSWORD`) VALUES (?,?,?,?);";
            PreparedStatement preparedStatement = connection2.prepareStatement(sql);

            // Iterate over all the added customers
                    for(Customer customer : customers) {
                    if (isCustomerExists(customer.getEmail(), customer.getPassword())) {
                        try {
                        throw new CustomerAlreadyExistsException(customer.getEmail());
                        }
                        catch (CustomerAlreadyExistsException e) {
                            System.out.println(e.getMessage());
                            continue;
                        }
                    }


                preparedStatement.setString(1, customer.getFirstName());
                preparedStatement.setString(2, customer.getLastName());
                preparedStatement.setString(3, customer.getEmail());
                preparedStatement.setString(4, customer.getPassword());
                preparedStatement.executeUpdate();
                System.out.println("Customer " + customer.getEmail() + " created successfully");


            }


        } catch (InterruptedException | SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().restoreConnection(connection2);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }


        }
    }

    public void updateCustomer(int customerID, Customer customer) {
        try {

            connection = ConnectionPool.getInstance().getConnection();
            String sql = "UPDATE `couponsystem`.`customers` SET `FIRST_NAME` = ?, `LAST_NAME` = ?, `EMAIL` = ?,`PASSWORD` = ?  WHERE (`ID` = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getPassword());
            preparedStatement.setInt(5, customerID);
            preparedStatement.executeUpdate();
            System.out.println("The update completed successfully.");
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
    }

    public void deleteCustomer(int... customerID) {
        Connection connection2 = null;
        try {
            connection2 = ConnectionPool.getInstance().getConnection();
            String sql = "DELETE FROM `couponsystem`.`customers` WHERE ID=?";

            PreparedStatement preparedStatement = connection2.prepareStatement(sql);
           for(int customerId : customerID) {

                    if (!isCustomerExists(customerId)) {
                        try {
                        throw new CustomerDoesNotExists(customerId);
                        }
                        catch (CustomerDoesNotExists e) {
                            System.out.println(e.getMessage());
                            continue;
                        }
                    }

                preparedStatement.setInt(1, customerId);
                preparedStatement.executeUpdate();
                System.out.println("The customer with the id: " + customerId + " deleted successfully.");
            }
        } catch (InterruptedException | SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().restoreConnection(connection2);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            connection2 = null;
        }
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customersList = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT * FROM `couponsystem`.`customers`";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int getID = resultSet.getInt(1);
                String getFirstName = resultSet.getString(2);
                String getLastName = resultSet.getString(3);
                String getEmail = resultSet.getString(4);
                String getPassword = resultSet.getString(5);
                customersList.add(new Customer(getID, getFirstName, getLastName, getEmail, getPassword));

            }
            return customersList;
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


        return null;

    }

    public Customer getOneCustomer(int customerID) {
        Customer customer = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT * FROM `couponsystem`.`customers` WHERE ID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int getID = resultSet.getInt(1);
                String getFirstName = resultSet.getString(2);
                String getLastName = resultSet.getString(3);
                String getEmail = resultSet.getString(4);
                String getPassword = resultSet.getString(5);
                customer = new Customer(getID, getFirstName, getLastName, getEmail, getPassword);

            }
            return customer;
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
        return customer;

    }
}
