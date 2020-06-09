package com.amitrei.dbdao;

import com.amitrei.beans.Company;
import com.amitrei.dao.CompaniesDAO;
import com.amitrei.db.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CompaniesDBDAO implements CompaniesDAO {

    private Connection connection = null;

    @Override
    public void isCompanyExists(String email, String password) {

    }

    @Override
    public void addCompany(Company company) {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "INSERT INTO `couponsystem`.`companies` (`NAME`, `EMAIL`,`PASSWORD`) VALUES (?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setString(3, company.getPassword());
            preparedStatement.executeUpdate();

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());

        } catch (SQLException e) {
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
    public void updateCompany(Company company) {

    }

    @Override
    public void updateCompany(int companyID) {

    }

    @Override
    public void deleteCompany(int companyID) {

    }

    @Override
    public List<Company> getAllCompanies() {
        return null;
    }

    @Override
    public Company getOneCompany(int companyID) {
        return null;
    }
}
