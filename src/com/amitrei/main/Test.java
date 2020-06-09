package com.amitrei.main;

import com.amitrei.beans.Company;
import com.amitrei.db.ConnectionPool;
import com.amitrei.db.DBManager;

import java.sql.SQLException;

import com.amitrei.db.DBCreateQueries;
import com.amitrei.dbdao.CompaniesDBDAO;
import com.amitrei.exceptions.CompanyAlreadyExistsException;
import com.amitrei.exceptions.CompanyDoesnotExistsException;
import com.amitrei.utils.getIdFromDB;

public class Test {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException, CompanyDoesnotExistsException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        ConnectionPool connectionPool = ConnectionPool.getInstance();
//        DBManager.createTable(DBCreateQueries.CREATE_CUSTOMERS_TABLE);
//        DBManager.createTable(DBCreateQueries.CREATE_COMPANIES_TABLE);
//        DBManager.createTable(DBCreateQueries.CREATE_CATEGORIES_TABLE);
//        DBManager.createTable(DBCreateQueries.CREATE_COUPONS_TABLE);
//        DBManager.createTable(DBCreateQueries.CREATE_CUSTOMERS_VS_COUPONS_TABLE);
        CompaniesDBDAO companiesDBDAO = new CompaniesDBDAO();


        try {
            connectionPool.closeAllConnections();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }


    }
}
