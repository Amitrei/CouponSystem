package com.amitrei.dbdao;

import com.amitrei.beans.Company;
import com.amitrei.dao.CompaniesDAO;
import com.amitrei.db.ConnectionPool;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompaniesDBDAO implements CompaniesDAO {

    public static final String IF_EXISTS_BY_EMAIL_AND_PASS = "SELECT EXISTS(SELECT 1 FROM `couponsystem`.`companies` WHERE `EMAIL`=? AND `PASSWORD`=? LIMIT 1)";
    public static final String IS_COMPANY_EXISTS_BY_ID = "SELECT EXISTS(SELECT 1 FROM `couponsystem`.`companies` WHERE `ID`=? LIMIT 1)";
    public static final String ADD_COMPANY = "INSERT INTO `couponsystem`.`companies` (`NAME`, `EMAIL`,`PASSWORD`) VALUES (?,?,?);";
    public static final String GET_ID_OF_COMPANY = "SELECT * FROM `couponsystem`.`companies` WHERE `NAME`=?";
    public static final String UPDATE_COMPANY = "UPDATE `couponsystem`.`companies` SET  `EMAIL` = ?, `PASSWORD` = ? WHERE (`ID` = ?)";
    public static final String DELETE_COMPANY = "DELETE FROM `couponsystem`.`companies` WHERE ID=?";
    public static final String GET_ALL_COMPANIES = "SELECT * FROM `couponsystem`.`companies`";
    public static final String GET_ONE_COMPANY_BY_ID = "SELECT * FROM `couponsystem`.`companies` WHERE ID=?";
    public static final String GET_ONE_COMPANY_BY_EMAIL = "SELECT * FROM `couponsystem`.`companies` WHERE `EMAIL`=?";
    private Connection connection = null;


    @Override
    public Boolean isCompanyExists(String companyEmail, String companyPassword) {
        try {
            int isExists = -99;
            connection = ConnectionPool.getInstance().getConnection();
            String sql = IF_EXISTS_BY_EMAIL_AND_PASS;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, companyEmail);
            preparedStatement.setString(2, companyPassword);
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

    public Boolean isCompanyExistsById(int companyID) {
        try {
            int isExists = -99;
            connection = ConnectionPool.getInstance().getConnection();
            String sql = IS_COMPANY_EXISTS_BY_ID;
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
    public void addCompany(Company company) {


        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = ADD_COMPANY;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setString(3, company.getPassword());
            preparedStatement.executeUpdate();

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
            String sql = GET_ID_OF_COMPANY;
            PreparedStatement preparedStatement = connection2.prepareStatement(sql);
            preparedStatement.setString(1, company.getName());
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
    public void updateCompany(Company company) {


        try {

            connection = ConnectionPool.getInstance().getConnection();
            String sql = UPDATE_COMPANY;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, company.getEmail());
            preparedStatement.setString(2, company.getPassword());
            preparedStatement.setInt(3, company.getId());
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

    @Override
    public void deleteCompany(int companyID) {


        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = DELETE_COMPANY;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, companyID);
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


    @Override
    public List<Company> getAllCompanies() {
        List<Company> companiesList = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = GET_ALL_COMPANIES;
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
    public Company getOneCompany(int companyID)  {

        Company company = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = GET_ONE_COMPANY_BY_ID;
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

    @Override
    public Company getOneCompany(String companyEmail)  {
        Company company = null;

        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = GET_ONE_COMPANY_BY_EMAIL;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, companyEmail);
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
    }    }

