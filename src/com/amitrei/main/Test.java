package com.amitrei.main;

import com.amitrei.beans.Company;
import com.amitrei.beans.Customer;
import com.amitrei.db.ConnectionPool;

import java.sql.SQLException;

import com.amitrei.dbdao.CategoriesDBDAO;
import com.amitrei.dbdao.CompaniesDBDAO;
import com.amitrei.dbdao.CouponsDBDAO;
import com.amitrei.dbdao.CustomersDBDAO;
import com.amitrei.exceptions.CompanyExceptions.CompanyAlreadyExistsException;

public class Test {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        ConnectionPool connectionPool = ConnectionPool.getInstance();
//        DBManager.createTable(DBCreateQueries.CREATE_CUSTOMERS_TABLE);
//        DBManager.createTable(DBCreateQueries.CREATE_COMPANIES_TABLE);
//        DBManager.createTable(DBCreateQueries.CREATE_CATEGORIES_TABLE);
//        DBManager.createTable(DBCreateQueries.CREATE_COUPONS_TABLE);
//        DBManager.createTable(DBCreateQueries.CREATE_CUSTOMERS_VS_COUPONS_TABLE);
        CouponsDBDAO cd = new CouponsDBDAO();
        CompaniesDBDAO cd1 = new CompaniesDBDAO();
        CategoriesDBDAO cg = new CategoriesDBDAO();
        CustomersDBDAO cus = new CustomersDBDAO();
        Customer c1 = new Customer("Amixt", "Reix", "AmitRei@xgmail.com", "123x4");
        Customer c2 = new Customer("Amixaasaat", "Reaaix", "AmitReafsi@aaxgmail.com", "123asfasxaa4");
        Customer c3 = new Customer("Amixaasaat", "Reaaix", "xxxx@aaxgmail.com", "123asfasxaa4");
        Company company = new Company("amixtcomp", "Amiaat1@gxmail.com", "1234");
        company.setId(13);

        try {
            connectionPool.closeAllConnections();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }


    }
}
