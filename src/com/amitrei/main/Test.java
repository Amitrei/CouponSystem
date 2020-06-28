package com.amitrei.main;

import com.amitrei.dailyjob.CouponExpirationDailyJob;
import com.amitrei.login.ClientType;
import com.amitrei.login.LoginManager;
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
import com.amitrei.exceptions.CouponsExceptions.CouponAlreadyExistsException;
import com.amitrei.exceptions.CouponsExceptions.CouponDateExpiredException;
import com.amitrei.exceptions.CouponsExceptions.CouponNotFoundException;
import com.amitrei.exceptions.CouponsExceptions.CouponOutOfStockException;
import com.amitrei.exceptions.CustomerExceptions.CustomerAlreadyExistsException;
import com.amitrei.exceptions.CustomerExceptions.CustomerAlreadyPurchasedCouponException;
import com.amitrei.exceptions.CustomerExceptions.CustomerDoesNotExists;
import com.amitrei.facade.AdminFacade;
import com.amitrei.facade.CustomerFacade;
import com.amitrei.utils.MyDateUtil;

public class Test {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException, CompanyAlreadyExistsException, CompanyDoesNotExistsException, CouponDateExpiredException, CouponNotFoundException, CustomerAlreadyExistsException, CustomerDoesNotExists, CouponAlreadyExistsException, CustomerAlreadyPurchasedCouponException, CouponOutOfStockException {
        Class.forName("com.mysql.cj.jdbc.Driver");
//        ConnectionPool connectionPool = ConnectionPool.getInstance();
//////        DBManager.createTable(DBCreateQueries.CREATE_CUSTOMERS_TABLE);
//////        DBManager.createTable(DBCreateQueries.CREATE_COMPANIES_TABLE);
//////        DBManager.createTable(DBCreateQueries.CREATE_CATEGORIES_TABLE);
//////        DBManager.createTable(DBCreateQueries.CREATE_COUPONS_TABLE);
//////        DBManager.createTable(DBCreateQueries.CREATE_CUSTOMERS_VS_COUPONS_TABLE);
//        CouponsDBDAO cd = new CouponsDBDAO();
//        CompaniesDBDAO cd1 = new CompaniesDBDAO();
//        CategoriesDBDAO cg = new CategoriesDBDAO();
//        CustomersDBDAO cus = new CustomersDBDAO();
//        Company company = new Company("xxxx", "45555", "555555335");
//        Company company2 = new Company("bbbb", "45555", "555555335");
//        MyDateUtil myd = new MyDateUtil();
//        Coupon coupon = new Coupon(39, Category.Electricity, "xxcvzxv", "zxczx", myd.currentDate(), myd.expiredDate(20), 100, 99.9, "IMAGE.png");
        FullTest fullTest = new FullTest();
        fullTest.TestAll();

//            try {
//                connectionPool.closeAllConnections();
//            } catch (InterruptedException e) {
//                System.out.println(e.getMessage());
//            }



    }
}
