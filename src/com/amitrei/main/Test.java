package com.amitrei.main;

import com.amitrei.beans.Customer;
import com.amitrei.db.ConnectionPool;

import java.sql.SQLException;

import com.amitrei.dbdao.CustomersDBDAO;
import com.amitrei.exceptions.CompanyDoesNotExistsException;
import com.amitrei.exceptions.CustomerAlreadyExistsException;

public class Test {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException, CompanyDoesNotExistsException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        ConnectionPool connectionPool = ConnectionPool.getInstance();
//        DBManager.createTable(DBCreateQueries.CREATE_CUSTOMERS_TABLE);
//        DBManager.createTable(DBCreateQueries.CREATE_COMPANIES_TABLE);
//        DBManager.createTable(DBCreateQueries.CREATE_CATEGORIES_TABLE);
//        DBManager.createTable(DBCreateQueries.CREATE_COUPONS_TABLE);
//        DBManager.createTable(DBCreateQueries.CREATE_CUSTOMERS_VS_COUPONS_TABLE);
        CustomersDBDAO cd = new CustomersDBDAO();
        Customer c1 = new Customer("Amixt","Reix","AmitRei@xgmail.com","123x4");
        Customer c2 = new Customer("Amixaasaat","Reaaix","AmitReafsi@aaxgmail.com","123asfasxaa4");
        Customer c3 = new Customer("Amixaasaat","Reaaix","xxxx@aaxgmail.com","123asfasxaa4");
        cd.addCustomer(c1,c2,c3);



        try {
            connectionPool.closeAllConnections();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }


    }
}
