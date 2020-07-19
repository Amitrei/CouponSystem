package com.amitrei.dbdao;

import com.amitrei.beans.Category;
import com.amitrei.beans.Coupon;
import com.amitrei.beans.Customer;
import com.amitrei.dao.CustomersDAO;
import com.amitrei.db.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomersDBDAO implements CustomersDAO {

    public static final String IS_CUSTOMER_EXISTS_BY_EMAIL = "SELECT EXISTS(SELECT 1 FROM `couponsystem`.`customers` WHERE `EMAIL` =?  LIMIT 1)";
    public static final String IS_CUSTOMER_EXISTS_BY_EMAIL_AND_PASS = "SELECT EXISTS(SELECT 1 FROM `couponsystem`.`customers` WHERE `EMAIL` =? AND `PASSWORD`=?  LIMIT 1)";
    public static final String IS_CUSTOMER_EXISTS_BY_ID = "SELECT EXISTS(SELECT 1 FROM `couponsystem`.`customers` WHERE `ID`=? LIMIT 1)";
    public static final String ADD_CUSTOMER = "INSERT INTO `couponsystem`.`customers` (`FIRST_NAME`, `LAST_NAME`,`EMAIL`,`PASSWORD`) VALUES (?,?,?,?);";
    public static final String GET_CUSTOMER_ID = "SELECT * FROM `couponsystem`.`customers` WHERE `EMAIL`=?";
    public static final String UPDATE_CUSTOMER = "UPDATE `couponsystem`.`customers` SET `FIRST_NAME` = ?, `LAST_NAME` = ?, `EMAIL` = ?,`PASSWORD` = ?  WHERE (`ID` = ?)";
    public static final String DELETE_CUSTOMER = "DELETE FROM `couponsystem`.`customers` WHERE ID=?";
    public static final String DELETE_CUSTOMER_PURCHASES = "DELETE FROM `couponsystem`.`customers_vs_coupons` WHERE (`CUSTOMER_ID` = ?)";
    public static final String GET_ALL_CUSTOMERS = "SELECT * FROM `couponsystem`.`customers`";
    public static final String GET_ONE_CUSTOMER_BY_ID = "SELECT * FROM `couponsystem`.`customers` WHERE ID=?";
    public static final String GET_ONE_CUSTOMER_BY_EMAIL = "SELECT * FROM `couponsystem`.`customers` WHERE EMAIL=?";
    public static final String GET_CUSTOMER_COUPONS = "SELECT * FROM `couponsystem`.`coupons` WHERE `ID`=?";
    public static final String IS_CUSTOMER_ALREADY_HAVE_COUPON = "SELECT EXISTS(SELECT 1 FROM `couponsystem`.`customers_vs_coupons` WHERE `CUSTOMER_ID` =? AND `COUPON_ID`=?  LIMIT 1)";
    public static final String GET_COUPONS_OF_CUSTOMER = "SELECT * FROM `couponsystem`.`customers_vs_coupons` WHERE `CUSTOMER_ID`=?";
    private Connection connection = null;


    /**
     * Using SELECT EXISTS LIMIT 1 in order to get the results:
     * 1 for exists company in the Database
     * 0 for non-exists company in the Database
     */


    public Boolean isCustomerExists(String email) {

        try {
            int isExists = -99;
            connection = ConnectionPool.getInstance().getConnection();
            String sql = IS_CUSTOMER_EXISTS_BY_EMAIL;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
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

    @Override
    public Boolean isCustomerExists(String email, String password) {

        try {
            int isExists = -99;
            connection = ConnectionPool.getInstance().getConnection();
            String sql = IS_CUSTOMER_EXISTS_BY_EMAIL_AND_PASS;
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
            String sql = IS_CUSTOMER_EXISTS_BY_ID;
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

    public void addCustomer(Customer customer) {

        Connection connection2 = null;
        try {
            connection2 = ConnectionPool.getInstance().getConnection();
            String sql = ADD_CUSTOMER;
            PreparedStatement preparedStatement = connection2.prepareStatement(sql);
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getPassword());
            preparedStatement.executeUpdate();


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


    // Getting the id by searching email
    public int getCustomerIDFromDB(Customer customer) {
        Connection connection2 = null;
        int result = -1;
        try {
            connection2 = ConnectionPool.getInstance().getConnection();
            String sql = GET_CUSTOMER_ID;
            PreparedStatement preparedStatement = connection2.prepareStatement(sql);
            preparedStatement.setString(1, customer.getEmail());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result = resultSet.getInt(1);
            }
            return result;

        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();

        } finally {
            try {
                ConnectionPool.getInstance().restoreConnection(connection2);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }

    public void updateCustomer(int customerID, Customer customer) {
        try {

            connection = ConnectionPool.getInstance().getConnection();
            String sql = UPDATE_CUSTOMER;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getPassword());
            preparedStatement.setInt(5, customerID);
            preparedStatement.executeUpdate();
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


    public void deleteCustomer(int customerID) {
        Connection connection2 = null;
        try {
            connection2 = ConnectionPool.getInstance().getConnection();
            String sql = DELETE_CUSTOMER;

            PreparedStatement preparedStatement = connection2.prepareStatement(sql);
            preparedStatement.setInt(1, customerID);
            preparedStatement.executeUpdate();

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

    public void deleteCustomerPurchaseHistory(int customerID) {
        Connection connection2 = null;

        try {
            connection2 = ConnectionPool.getInstance().getConnection();
            String sql = DELETE_CUSTOMER_PURCHASES;
            PreparedStatement preparedStatement = connection2.prepareStatement(sql);
            preparedStatement.setInt(1, customerID);
            preparedStatement.executeUpdate();
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
            String sql = GET_ALL_CUSTOMERS;
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
            String sql = GET_ONE_CUSTOMER_BY_ID;
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

    public Customer getOneCustomer(String email) {
        Customer customer = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = GET_ONE_CUSTOMER_BY_EMAIL;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
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

    @Override
    public List<Coupon> getCustomerCoupons(int customerID) {

        List<Coupon> customerAllCoupons = new ArrayList<>();
        Connection connection2 = null;
        try {
            connection2 = ConnectionPool.getInstance().getConnection();

            for (int couponID : GetCustomerCouponPurchases(customerID)) {
                String sql = GET_CUSTOMER_COUPONS;
                PreparedStatement preparedStatement = connection2.prepareStatement(sql);
                preparedStatement.setInt(1, couponID);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int getID = resultSet.getInt(1);
                    int getCompanyID = resultSet.getInt(2);
                    int getCategoryID = resultSet.getInt(3);
                    String getTitle = resultSet.getString(4);
                    String getDescription = resultSet.getString(5);
                    java.sql.Date getStartDate = resultSet.getDate(6);
                    java.sql.Date getEndDate = resultSet.getDate(7);
                    int getAmount = resultSet.getInt(8);
                    Double getPrice = resultSet.getDouble(9);
                    String getImage = resultSet.getString(10);
                    Coupon customerCoupon = new Coupon(getID, getCompanyID, getCategoryID, getTitle, getDescription, getStartDate, getEndDate, getAmount, getPrice, getImage);
                    customerAllCoupons.add(customerCoupon);
                }


            }
            return customerAllCoupons;

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
        return null;


    }

    @Override
    public List<Coupon> getCustomerCoupons(int customerID, Category category) {
        List<Coupon> customerCoupons = new ArrayList<>();
        for (Coupon coupon : getCustomerCoupons(customerID)) {
            if (coupon.getCategory().equals(category))
                customerCoupons.add(coupon);
        }

        return customerCoupons;
    }

    @Override
    public List<Coupon> getCustomerCoupons(int customerID, double maxPrice) {
        List<Coupon> customerCoupons = new ArrayList<>();
        for (Coupon coupon : getCustomerCoupons(customerID)) {
            if (coupon.getPrice() <= maxPrice)
                customerCoupons.add(coupon);
        }

        return customerCoupons;
    }

    @Override
    public boolean isCustomerAlreadyHaveCoupon(int customerID, int couponID) {
        try {
            int isExists = -99;
            connection = ConnectionPool.getInstance().getConnection();
            String sql = IS_CUSTOMER_ALREADY_HAVE_COUPON;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerID);
            preparedStatement.setInt(2, couponID);
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

    @Override
    public List<Integer> GetCustomerCouponPurchases(int customerID) {
        List<Integer> customerPurchasesList = new ArrayList<>();
        try {

            connection = ConnectionPool.getInstance().getConnection();
            String sql = GET_COUPONS_OF_CUSTOMER;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                customerPurchasesList.add(resultSet.getInt(2));

            }

            return customerPurchasesList;


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

}


