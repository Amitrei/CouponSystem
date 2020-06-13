package com.amitrei.dbdao;

import com.amitrei.beans.Company;
import com.amitrei.beans.Coupon;
import com.amitrei.dao.CompaniesDAO;
import com.amitrei.db.ConnectionPool;
import com.amitrei.exceptions.CompanyExceptions.CompanyAlreadyExistsException;
import com.amitrei.exceptions.CompanyExceptions.CompanyDoesNotExistsException;
import com.amitrei.exceptions.CouponsExceptions.CouponNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompaniesDBDAO implements CompaniesDAO {

    private Connection connection = null;

    /**
     * Using SELECT EXISTS LIMIT 1 in order to get the results:
     * 1 for exists company in the Database
     * 0 for non-exists company in the Database
     */

    @Override
    public Boolean isCompanyExistsByEmail(String email) {
        try {
            int isExists = -99;
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT EXISTS(SELECT 1 FROM `couponsystem`.`companies` WHERE `EMAIL` =? LIMIT 1)";
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

    public Boolean isCompanyExistsByName(String name) {
        try {
            int isExists = -99;
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT EXISTS(SELECT 1 FROM `couponsystem`.`companies` WHERE `NAME` =? LIMIT 1)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
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

    public Boolean isCompanyExists(int companyID) {
        try {
            int isExists = -99;
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT EXISTS(SELECT 1 FROM `couponsystem`.`companies` WHERE `ID`=? LIMIT 1)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, companyID);
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
    public void addCompany(Company company) throws CompanyAlreadyExistsException {

        if (isCompanyExistsByEmail(company.getEmail()) || isCompanyExistsByName(company.getName()))
            throw new CompanyAlreadyExistsException();

        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "INSERT INTO `couponsystem`.`companies` (`NAME`, `EMAIL`,`PASSWORD`) VALUES (?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setString(3, company.getPassword());
            preparedStatement.executeUpdate();
            System.out.println("The company was added successfully.");

        } catch (InterruptedException | SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().restoreConnection(connection);
                connection = null;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }


        }
    }


    public int getCompanyIDFromDB(Company company) {
        Connection connection2 = null;
        int result = -1;
        try {
            connection2 = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT * FROM `couponsystem`.`companies` WHERE `EMAIL`=?";
            PreparedStatement preparedStatement = connection2.prepareStatement(sql);
            preparedStatement.setString(1, company.getEmail());
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


    @Override
    public void updateCompany(int companyID, Company company) throws CompanyDoesNotExistsException {

        if (!isCompanyExists(companyID)) throw new CompanyDoesNotExistsException();


        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "UPDATE `couponsystem`.`companies` SET  `EMAIL` = ?, `PASSWORD` = ? WHERE (`ID` = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, company.getEmail());
            preparedStatement.setString(2, company.getPassword());
            preparedStatement.setInt(3, companyID);
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

    @Override
    public void deleteCompany(int companyID) throws CompanyDoesNotExistsException {
        if (!isCompanyExists(companyID)) throw new CompanyDoesNotExistsException();

        try {
            deleteCouponsOfCompany(companyID);
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "DELETE FROM `couponsystem`.`companies` WHERE ID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, companyID);
            preparedStatement.executeUpdate();
            System.out.println("The company with the id: " + companyID + " deleted successfully.");
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


    private void deleteCouponsOfCompany(int companyID) {
        Connection connection2 = null;

        try {
            deleteCustomerCouponHistory(companyID);
            connection2 = ConnectionPool.getInstance().getConnection();
            String sql = "DELETE FROM `couponsystem`.`coupons` WHERE (`COMPANY_ID` = ?)";
            PreparedStatement preparedStatement = connection2.prepareStatement(sql);
            preparedStatement.setInt(1, companyID);
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

    private void deleteCustomerCouponHistory(int couponCompanyID) {
        Connection connection2 = null;

        // Looping over an ArrayList of all coupons of the same company
        try {
            for (Coupon coupon : getAllCompaniesCoupons(couponCompanyID)) {
                connection2 = ConnectionPool.getInstance().getConnection();
                String sql = "DELETE FROM `couponsystem`.`customers_vs_coupons` WHERE (`COUPON_ID` = ?)";
                PreparedStatement preparedStatement = connection2.prepareStatement(sql);
                preparedStatement.setInt(1, coupon.getId());
                preparedStatement.executeUpdate();
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


    public List<Coupon> getAllCompaniesCoupons(int companyID) {
        List<Coupon> couponsList = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT * FROM `couponsystem`.`coupons` WHERE (`COMPANY_ID`=?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, companyID);
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
                couponsList.add(new Coupon(getID, getCompanyID, getCategoryID, getTitle, getDescription, getStartDate, getEndDate, getAmount, getPrice, getImage));
            }
            return couponsList;

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


    @Override
    public List<Company> getAllCompanies() {
        List<Company> companiesList = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT * FROM `couponsystem`.`companies`";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int getID = resultSet.getInt(1);
                String getName = resultSet.getString(2);
                String getEmail = resultSet.getString(3);
                String getPassword = resultSet.getString(4);
                companiesList.add(new Company(getID, getName, getEmail, getPassword));

            }
            return companiesList;
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

    @Override
    public Company getOneCompany(int companyID) throws CompanyDoesNotExistsException {
        if (!isCompanyExists(companyID)) throw new CompanyDoesNotExistsException();

        Company company = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT * FROM `couponsystem`.`companies` WHERE ID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, companyID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int getID = resultSet.getInt(1);
                String getName = resultSet.getString(2);
                String getEmail = resultSet.getString(3);
                String getPassword = resultSet.getString(4);
                company = new Company(getID, getName, getEmail, getPassword);

            }
            return company;
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
        return company;
    }
}
