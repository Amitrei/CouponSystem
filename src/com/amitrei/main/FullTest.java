package com.amitrei.main;


import com.amitrei.beans.Category;
import com.amitrei.beans.Company;
import com.amitrei.beans.Coupon;
import com.amitrei.beans.Customer;
import com.amitrei.dao.CouponsDAO;
import com.amitrei.db.ConnectionPool;
import com.amitrei.dbdao.CouponsDBDAO;
import com.amitrei.exceptions.CompanyExceptions.CannotChangeCompanyNameException;
import com.amitrei.exceptions.CompanyExceptions.CompanyAlreadyExistsException;
import com.amitrei.exceptions.CompanyExceptions.CompanyDoesNotExistsException;
import com.amitrei.exceptions.CouponsExceptions.CouponDateExpiredException;
import com.amitrei.exceptions.CouponsExceptions.CouponNotFoundException;
import com.amitrei.exceptions.CustomerExceptions.CustomerAlreadyExistsException;
import com.amitrei.exceptions.CustomerExceptions.CustomerDoesNotExists;
import com.amitrei.facade.AdminFacade;
import com.amitrei.login.ClientType;
import com.amitrei.login.LoginManager;
import com.amitrei.utils.MyDateUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class FullTest {
    MyDateUtil myDateUtil = new MyDateUtil();
    CouponsDAO couponsDAO = new CouponsDBDAO();
    AdminFacade adminFacade = new AdminFacade();

    public void TestAll() {
        try {

            Connection connection = ConnectionPool.getInstance().getConnection();

            printTitle("ADMINISTRATOR");
            var LoggedInAsAdmin = LoginManager.getInstance().login("admin@admin.com", "1234", ClientType.Administrator);
            System.out.print("ADMIN LOGIN WITH WRONG PASSWORD AND USERNAME AND GETTING THE CURRECT FACADE:  ");
            System.out.println(LoggedInAsAdmin);
            LoggedInAsAdmin = LoginManager.getInstance().login("admin@admin.com", "admin", ClientType.Administrator);
            System.out.print("ADMIN LOGIN WITH  PASSWORD AND USERNAME AND GETTING THE CURRECT FACADE: ");
            System.out.println(LoggedInAsAdmin.getClass());


            printTitle("ADDING COMPANY");
            Company company = new Company("Couponim", "couponim@couponim.com", "password");
            System.out.println("ADDING COMPANY:     " + company.toString());
            try {
                ((AdminFacade) LoggedInAsAdmin).addCompany(company);
            } catch (CompanyAlreadyExistsException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("MAKING SURE COMPANY WAS ADDED BY GETTING COMPANIES FROM DB     " + ((AdminFacade) LoggedInAsAdmin).getAllCompanies());
            System.out.println("ADDING THE SAME COMPANY AGAIN:     " + company.toString());
            try {
                ((AdminFacade) LoggedInAsAdmin).addCompany(company);
            } catch (CompanyAlreadyExistsException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("MAKING SURE COMPANY WASNT ADDED AFTER EXCEPTION     " + ((AdminFacade) LoggedInAsAdmin).getAllCompanies());


            printTitle("UPDATE COMPANY");
            System.out.println("COMPANY DETAILS BEFORE UPDATE FROM THE DB     " + ((AdminFacade) LoggedInAsAdmin).getAllCompanies());

            company.setEmail("Changedemail@gmail.com");
            company.setPassword("12345");
            System.out.println("UPDATING COMPANY DETAILS:     " + company.toString());
            try {
                ((AdminFacade) LoggedInAsAdmin).updateCompany(company);
            } catch (CannotChangeCompanyNameException e) {
                System.out.println(e.getMessage());
            }


            System.out.println("COMPANY DETAILS FROM DB AFTER UPDATE     " + ((AdminFacade) LoggedInAsAdmin).getAllCompanies());
            company.setName("Blala");

            System.out.println("TRYING TO CHANGE COMPANY NAME:");
            try {
                ((AdminFacade) LoggedInAsAdmin).updateCompany(company);
            } catch (CannotChangeCompanyNameException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("COMPANY DETAILS FROM DB AFTER UPDATE     " + ((AdminFacade) LoggedInAsAdmin).getAllCompanies());
            System.out.println("TRYING CHANGING ID:");
            company.setId(123);


            printTitle("DELETE COMPANY");
            System.out.println("** ADDING COUPONS AND PURCHASES TO THE COMPANY ** ");
            Coupon coupon = new Coupon(company.getId(), Category.WINTER, "Title", "Descreption", myDateUtil.currentDate(), myDateUtil.expiredDate(10), 100, 99.99, "Image.png");
            couponsDAO.addCoupon(coupon);
            try {
                couponsDAO.addCouponPurchase(62, couponsDAO.getCouponIDFromDB(coupon));
            } catch (CouponNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (CouponDateExpiredException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("DELETING COMPANY BY COMPANY ID:" + company.getId());
            try {
                ((AdminFacade) LoggedInAsAdmin).deleteCompany(company.getId());
            } catch (CompanyDoesNotExistsException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("MAKING SURE COMPANY WAS DELETED BY GETTING COMPANIES FROM DB     " + ((AdminFacade) LoggedInAsAdmin).getAllCompanies());

            printTitle("GETTING ALL COMPANIES");
            System.out.println("ADDING DUMMY COMPANIES");
            Company company0 = new Company("Couponim1", "111@gmail.com", "1111");
            Company company1 = new Company("Couponim2", "2222@gmail.com", "22222");
            Company company2 = new Company("Couponim3", "3333@gmail.com", "3333333");
            addDummyCompanies(company0, company1, company2);
            System.out.println(((AdminFacade) LoggedInAsAdmin).getAllCompanies());
            printTitle("GETTING SINGLE COMPANY BY ID");
            System.out.println("GETTING COMPANY BY A SINGLE ID: " + company0.getId());
            System.out.println(((AdminFacade) LoggedInAsAdmin).getOneCompany(company0.getId()));
            deleteDummyCompanies(company0, company1, company2);

            printTitle("ADDING NEW CUSTOMER");
            Customer customer = new Customer("Amit", "Rei", "Amitrei@gmail.com", "1234");
            System.out.println("ADDING THIS CUSTOMER:" + customer);
            try {
                adminFacade.addCustomer(customer);
            } catch (CustomerAlreadyExistsException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("CHECKING IF CUSTOMER ADDED TO DB:" + adminFacade.getAllCustomers());
            System.out.println("TRYING TO ADD A CUSTOMER WITH THE SAME EMAIL:");
            Customer fakeCustomer = new Customer("moshe", "cohen", "Amitrei@gmail.com", "41414");

            try {
                adminFacade.addCustomer(fakeCustomer);
            } catch (CustomerAlreadyExistsException e) {
                System.out.println(e.getMessage());
            }

            printTitle("UPDATING CUSTOMER");
            System.out.println("UPDATING CUSTOMER FROM " + customer);
            customer.setFirstName("Moshe");
            customer.setEmail("Moshes@gmail.com");
            customer.setLastName("Moshiko");
            System.out.println("TO " + customer);
            try {
                adminFacade.updateCustomer(customer);
            } catch (CustomerAlreadyExistsException e) {
                System.out.println(e.getMessage());
            }
            try {
                System.out.println("CHECKING UPDATE FROM DB: " + adminFacade.getOneCustomer(customer.getId()));
            } catch (CustomerDoesNotExists e) {
                System.out.println(e.getMessage());
            }
            System.out.println("TRYING CHANGING ID:");
            customer.setId(123);


            printTitle("DELETING CUSTOMER");
            /** NEEDS TO DO **/


            printTitle("GETTING ALL THE CUSTOMERS:");
            System.out.println("**** CREATING DUMMY CUSTOMERS");
            Customer customer1 = new Customer("avi", "ron", "Aviron@gmail.com", "1234");
            Customer customer2 = new Customer("Simha", "Rif", "simhaRif@gmail.com", "1234");
            Customer customer3 = new Customer("Ram", "Kol", "RamKol@gmail.com", "1234");
            creatingCustomerDummies(customer1, customer2, customer3);
            System.out.println("ALL CUSTOMERS:");
            System.out.println(adminFacade.getAllCustomers());
            deleteCustomerDummies(customer1, customer2, customer3);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (CustomerDoesNotExists customerDoesNotExists) {
            customerDoesNotExists.printStackTrace();
        } catch (CompanyDoesNotExistsException e) {
            e.printStackTrace();
        }

    }


    private void printTitle(String title) {
        System.out.println();
        System.out.println("*************************************************************************************************       ~          " + title + "              ~          *************************************************************************************************************************************************************************************************");
        System.out.println();

    }

    private void printCenter(String title) {
        System.out.println();
        System.out.println("                                             " + title + "                                                                                                                                                                                                                           ");
        System.out.println();

    }

    private void addDummyCompanies(Company company0, Company company1, Company company2) {
        try {

            var admin = LoginManager.getInstance().login("admin@admin.com", "admin", ClientType.Administrator);
            ((AdminFacade) admin).addCompany(company0);
            ((AdminFacade) admin).addCompany(company1);
            ((AdminFacade) admin).addCompany(company2);


        } catch (CompanyDoesNotExistsException e) {
            e.printStackTrace();
        } catch (CustomerDoesNotExists customerDoesNotExists) {
            customerDoesNotExists.printStackTrace();
        } catch (CompanyAlreadyExistsException e) {
            e.printStackTrace();
        }

    }

    private void deleteDummyCompanies(Company company0, Company company1, Company company2) {
        try {

            var admin = LoginManager.getInstance().login("admin@admin.com", "admin", ClientType.Administrator);
            ((AdminFacade) admin).deleteCompany(company0.getId());
            ((AdminFacade) admin).deleteCompany(company1.getId());
            ((AdminFacade) admin).deleteCompany(company2.getId());


        } catch (CompanyDoesNotExistsException e) {
            e.printStackTrace();
        } catch (CustomerDoesNotExists customerDoesNotExists) {
            customerDoesNotExists.printStackTrace();
        }

    }


    private void creatingCustomerDummies(Customer customer1, Customer customer2, Customer customer3) {
        try {
            adminFacade.addCustomer(customer1);
            adminFacade.addCustomer(customer2);
            adminFacade.addCustomer(customer3);
        } catch (CustomerAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }


    private void deleteCustomerDummies(Customer customer1, Customer customer2, Customer customer3) {
        try {
            adminFacade.deleteCustomer(customer1.getId());
            adminFacade.deleteCustomer(customer2.getId());
            adminFacade.deleteCustomer(customer3.getId());

        } catch (CustomerDoesNotExists e) {
            System.out.println(e.getMessage());
        }
    }
}
