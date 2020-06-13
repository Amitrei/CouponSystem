package com.amitrei.main;

import com.amitrei.beans.Category;
import com.amitrei.beans.Company;
import com.amitrei.beans.Coupon;
import com.amitrei.beans.Customer;
import com.amitrei.db.ConnectionPool;

import java.sql.SQLException;

import com.amitrei.dbdao.CategoriesDBDAO;
import com.amitrei.dbdao.CompaniesDBDAO;
import com.amitrei.dbdao.CouponsDBDAO;
import com.amitrei.dbdao.CustomersDBDAO;
import com.amitrei.exceptions.CompanyExceptions.CompanyAlreadyExistsException;
import com.amitrei.exceptions.CompanyExceptions.CompanyDoesNotExistsException;
import com.amitrei.exceptions.CouponsExceptions.CouponDateExpiredException;
import com.amitrei.exceptions.CouponsExceptions.CouponNotFoundException;
import com.amitrei.utils.MyDateUtil;

public class Test {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException, CompanyAlreadyExistsException {
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
        Company company = new Company("amixtcomp", "Amiaat1@gxmail.com", "1234");
        MyDateUtil myd = new MyDateUtil();
        Coupon coupon = new Coupon(21, Category.Electricity,"BESTxxxxxx","myBSTxx",myd.currentDate(),myd.expiredDate(10),100,99.9,"IMAGE.png");
        Customer customer = new Customer("Amit","rei","a@xx0gamil.com","1234");
        cus.deleteCustomer(56);
        try {
            connectionPool.closeAllConnections();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }


    }
}
