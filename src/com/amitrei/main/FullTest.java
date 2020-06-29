package com.amitrei.main;


import com.amitrei.beans.Category;
import com.amitrei.beans.Company;
import com.amitrei.beans.Coupon;
import com.amitrei.beans.Customer;
import com.amitrei.dao.CouponsDAO;
import com.amitrei.dao.CustomersDAO;
import com.amitrei.db.ConnectionPool;
import com.amitrei.dbdao.CouponsDBDAO;
import com.amitrei.dbdao.CustomersDBDAO;
import com.amitrei.exceptions.CompanyExceptions.CannotChangeCompanyNameException;
import com.amitrei.exceptions.CompanyExceptions.CompanyAlreadyExistsException;
import com.amitrei.exceptions.CompanyExceptions.CompanyDoesNotExistsException;
import com.amitrei.exceptions.CouponsExceptions.CouponAlreadyExistsException;
import com.amitrei.exceptions.CouponsExceptions.CouponDateExpiredException;
import com.amitrei.exceptions.CouponsExceptions.CouponNotFoundException;
import com.amitrei.exceptions.CustomerExceptions.CustomerAlreadyExistsException;
import com.amitrei.exceptions.CustomerExceptions.CustomerDoesNotExists;
import com.amitrei.facade.AdminFacade;
import com.amitrei.facade.CompanyFacade;
import com.amitrei.facade.CustomerFacade;
import com.amitrei.login.ClientType;
import com.amitrei.login.LoginManager;
import com.amitrei.utils.MyDateUtil;
import jdk.swing.interop.SwingInterOpUtils;
import org.w3c.dom.ls.LSOutput;

import java.sql.Connection;
import java.sql.SQLException;

public class FullTest {
    MyDateUtil myDateUtil = new MyDateUtil();
    CouponsDAO couponsDAO = new CouponsDBDAO();
    AdminFacade adminFacade = new AdminFacade();
    CustomerFacade customerFacade = new CustomerFacade();
    CompanyFacade companyFacade = new CompanyFacade();
    CustomersDAO customersDAO = new CustomersDBDAO();

    public void TestAll() {
        try {

            Connection connection = ConnectionPool.getInstance().getConnection();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            adminTestTitle();
            printTitle("ADMINISTRATOR LOGIN");
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
            System.out.println();
            System.out.println("ADDING THE SAME COMPANY AGAIN:     " + company);
            try {
                ((AdminFacade) LoggedInAsAdmin).addCompany(company);
            } catch (CompanyAlreadyExistsException e) {
                System.out.println(e.getMessage());
            }
            System.out.println();


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
            System.out.println();
            System.out.println("TRYING TO CHANGE COMPANY NAME:");
            company.setName("Blala");

            try {
                ((AdminFacade) LoggedInAsAdmin).updateCompany(company);
            } catch (CannotChangeCompanyNameException e) {
                System.out.println(e.getMessage());
            }
            System.out.println();
            System.out.println("COMPANY DETAILS FROM DB AFTER UPDATE     " + ((AdminFacade) LoggedInAsAdmin).getAllCompanies());
            System.out.println();
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
            System.out.println();
            System.out.println("DELETING COMPANY BY COMPANY ID:" + company.getId());
            System.out.println();
            System.out.println("BEFORE DELETE GETTING COMPANIES FROM DB     " + ((AdminFacade) LoggedInAsAdmin).getAllCompanies());

            try {
                ((AdminFacade) LoggedInAsAdmin).deleteCompany(company.getId());
            } catch (CompanyDoesNotExistsException e) {
                System.out.println(e.getMessage());
            }
            System.out.println();

            System.out.println("AFTER DELETE GETTING COMPANIES FROM DB     " + ((AdminFacade) LoggedInAsAdmin).getAllCompanies());

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
            System.out.println();
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
            customer.setPassword("4321");
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
            System.out.println("### ADDING COUPONS PURCHASE TO THE CUSTOMER ###");
            company0 = new Company("Couponim1", "111@gmail.com", "1111");
            try {
                adminFacade.addCompany(company0);
            } catch (CompanyAlreadyExistsException e) {
                System.out.println(e.getMessage());
            }
            coupon = new Coupon(company0.getId(), Category.WINTER, "Title", "Descreption", myDateUtil.currentDate(), myDateUtil.expiredDate(10), 100, 99.99, "Image.png");
            couponsDAO.addCoupon(coupon);

            try {
                couponsDAO.addCouponPurchase(customer.getId(), couponsDAO.getCouponIDFromDB(coupon));
            } catch (CouponNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (CouponDateExpiredException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("PURCHASE ADDED SUCCSESSFULY: " + customersDAO.getCustomerCoupons(customer.getId()));
            System.out.println();
            System.out.println("DELETING THIS CUSTOMER " + customer);
            System.out.println("ALL CUSTOMERS FROM DB BEFORE DELETED " + adminFacade.getAllCustomers());

            adminFacade.deleteCustomer(customer.getId());
            adminFacade.deleteCompany(company0.getId());
            couponsDAO.deleteCoupon(couponsDAO.getCouponIDFromDB(coupon));
            System.out.println("ALL CUSTOMERS FROM DB AFTER DELETED " + adminFacade.getAllCustomers());


            printTitle("GETTING ALL THE CUSTOMERS:");
            System.out.println("### CREATING DUMMY CUSTOMERS ###");
            Customer customer1 = new Customer("avi", "ron", "Aviron@gmail.com", "1234");
            Customer customer2 = new Customer("Simha", "Rif", "simhaRif@gmail.com", "1234");
            Customer customer3 = new Customer("Ram", "Kol", "RamKol@gmail.com", "1234");
            creatingCustomerDummies(customer1, customer2, customer3);
            System.out.println("ALL CUSTOMERS:");
            System.out.println(adminFacade.getAllCustomers());

            printTitle("GETTING A SINGLE CUSTOMER USING CUSTOMER ID");
            System.out.println("GETTING THE DUMMIE CUSTOMER USING HIS ID: " + customer1.getId());
            System.out.println(adminFacade.getOneCustomer(customer1.getId()));
            deleteCustomerDummies(customer1, customer2, customer3);
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            companyTestTitle();
            printTitle("COMPANY LOGIN");
            System.out.println("MY TESTING COMPANY:" + adminFacade.getOneCompany(351));
            System.out.println("### TRYING TO LOG-IN WITH WRONG PASSWORD ###");
            System.out.println(LoginManager.getInstance().login("testCompany@gmail.com", "WrongPassword", ClientType.Company));
            System.out.println();
            System.out.println("### TRYING TO LOG-IN WITH CURRECT PASSWORD ### ");
            System.out.println(LoginManager.getInstance().login("testCompany@gmail.com", "1234", ClientType.Company).getClass());
            var companyLoggedIn = ((CompanyFacade) LoginManager.getInstance().login("testCompany@gmail.com", "1234", ClientType.Company));
            printTitle("COMPANY ADDING COUPON");
            coupon = new Coupon(companyLoggedIn.getCompanyID(), Category.WINTER, "testTitle", "MyDescription", myDateUtil.currentDate(), myDateUtil.expiredDate(10), 100, 99.9, "image.png");
            System.out.println("ADDING THIS COUPON: " + coupon);
            try {
                companyLoggedIn.addCoupon(coupon);
            } catch (CouponAlreadyExistsException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("CHECKING IF COUPON IS ADDED TO DB:");
            System.out.println(companyLoggedIn.getCompanyCoupons());
            System.out.println("TRYING TO ADD THE SAME TITLE COUPON:");
            try {
                companyLoggedIn.addCoupon(coupon);
            } catch (CouponAlreadyExistsException e) {
                System.out.println(e.getMessage());
            }
            System.out.println();
            System.out.println("ADDING COUPON:");
            Coupon coupon2 = new Coupon(413, Category.WINTER, "testTitle", "MyDescription", myDateUtil.currentDate(), myDateUtil.expiredDate(10), 100, 99.9, "image.png");
            System.out.println(coupon2);
            try {
                companyLoggedIn.addCoupon(coupon2);
            } catch (CouponAlreadyExistsException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("CHECKING IF BOTH COUPONS HAVING THE SAME TITLE BUT DIFFRENTS COMAPNIES:");
            System.out.println(couponsDAO.getAllCoupons());
            try {
                companyLoggedIn.deleteCoupon(coupon2.getId());
            } catch (CouponNotFoundException e) {
                System.out.println(e.getMessage());
            }
            printTitle("UPDATING COUPON");
            System.out.println("TRYING TO UPDATE COUPON ID:");
            coupon.setId(123);
            System.out.println();
            System.out.println("TRYING TO UPDATE COMPANY ID:");
            coupon.setCompanyID(123);
            System.out.println();

            System.out.println("UPDATING COUPON DETAILS:");
            coupon.setAmount(5);
            coupon.setCategory(Category.FOOD);
            coupon.setImage("ChangedImage.jpeg");
            coupon.setPrice(13);
            System.out.println(coupon);
            System.out.println("BEFORE UPDATING COUPON FROM DB:");
            System.out.println(companyLoggedIn.getCompanyCoupons());

            try {
                couponsDAO.updateCoupon(coupon.getId(), coupon);
            } catch (CouponNotFoundException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("AFTER UPDATING COUPON FROM DB:");
            System.out.println(companyLoggedIn.getCompanyCoupons());


            printTitle("DELETING COUPON");

            System.out.println("### ADDING PURCHASE TO THE COUPON ###");
            try {
                couponsDAO.addCouponPurchase(62, coupon.getId());
            } catch (CouponNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (CouponDateExpiredException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("DELETING COUPON " + coupon);
            System.out.println("BEFORE DELETING COUPON FROM DB:");
            System.out.println(couponsDAO.getAllCoupons());
            System.out.println("AFTER DELETING COUPON FROM DB:");


            try {
                companyLoggedIn.deleteCoupon(coupon.getId());
            } catch (CouponNotFoundException e) {
                System.out.println(e.getMessage());
            }

            System.out.println(couponsDAO.getAllCoupons());

            printTitle("ALL COUPONS OF COMPANY");
            System.out.println("### ADDING COUPONS DUMMIES ###");
            System.out.println();
            System.out.println("GETTING ALL COUPONS OF THE CURRENT COMPANY:");
            coupon=new Coupon(351,Category.FOOD,"DummyTitle1","DummyDescription1",myDateUtil.currentDate(),myDateUtil.expiredDate(10),10,30,"DummyImage1.png");
            coupon2=new Coupon(351,Category.Electricity,"DummyTitle2","DummyDescription2",myDateUtil.currentDate(),myDateUtil.expiredDate(10),10,60,"DummyImage2.png");
            Coupon coupon3=new Coupon(351,Category.Electricity,"DummyTitle3","DummyDescription3",myDateUtil.currentDate(),myDateUtil.expiredDate(10),10,90,"DummyImage3.png");
            addDummyCoupons(coupon,coupon2,coupon3);
            System.out.println(companyLoggedIn.getCompanyCoupons());

            printTitle("ALL COUPONS OF SPECIFIC CATEGORY");
            System.out.println("ALL COUPONS OF THE SPECIFIC CATEGORY:" + Category.FOOD);
            System.out.println(companyLoggedIn.getCompanyCoupons(Category.FOOD));
            System.out.println();
            System.out.println("ALL COUPONS OF THE SPECIFIC CATEGORY:" + Category.Electricity);
            System.out.println(companyLoggedIn.getCompanyCoupons(Category.Electricity));
            printTitle("ALL COUPONS BY MAXIMUM PRICE");
            System.out.println("ALL COUPONS BY MAXIMUM PRICE OF:30");
            System.out.println(companyLoggedIn.getCompanyCoupons(30.0));
            System.out.println();
            System.out.println("ALL COUPONS BY MAXIMUM PRICE OF:60");
            System.out.println(companyLoggedIn.getCompanyCoupons(60.0));
            System.out.println();   System.out.println("ALL COUPONS BY MAXIMUM PRICE OF:100");
            System.out.println(companyLoggedIn.getCompanyCoupons(100.0));
            System.out.println();
            printTitle("COMPANY DETAILS");
            System.out.println(companyLoggedIn.getCompanyDetails());
            deleteDummyCoupons(coupon,coupon2,coupon3);








        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (CustomerDoesNotExists e) {
            System.out.println(e.getMessage());
        } catch (CompanyDoesNotExistsException e) {
            System.out.println(e.getMessage());
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


    private void addDummyCoupons( Coupon coupon1, Coupon coupon2, Coupon coupon3) {
        CompanyFacade companyLoggedIn=null;
        try {
           companyLoggedIn = ((CompanyFacade) LoginManager.getInstance().login("testCompany@gmail.com", "1234", ClientType.Company));
        } catch (CompanyDoesNotExistsException e) {
            System.out.println(e.getMessage());
        } catch (CustomerDoesNotExists e) {
            System.out.println(e.getMessage());
        }
        try {
            companyLoggedIn.addCoupon(coupon1);
            companyLoggedIn.addCoupon(coupon2);
            companyLoggedIn.addCoupon(coupon3);
        } catch (CouponAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

    }


    private void deleteDummyCoupons(Coupon coupon1, Coupon coupon2, Coupon coupon3) {
        CompanyFacade companyLoggedIn=null;
        try {
            companyLoggedIn = ((CompanyFacade) LoginManager.getInstance().login("testCompany@gmail.com", "1234", ClientType.Company));
        } catch (CompanyDoesNotExistsException e) {
            System.out.println(e.getMessage());
        } catch (CustomerDoesNotExists e) {
            System.out.println(e.getMessage());
        }
        try {
            companyLoggedIn.deleteCoupon(coupon1.getId());
            companyLoggedIn.deleteCoupon(coupon2.getId());
            companyLoggedIn.deleteCoupon(coupon3.getId());

        } catch (CouponNotFoundException e) {
            System.out.println(e.getMessage());
        }

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

    private void adminTestTitle() {

        System.out.println("                                                                                                                                                                                \n" +
                "                                                                                                                                                                                \n" +
                "                                                                      db      `7MM\"\"\"Yb. `7MMM.     ,MMF'`7MMF'`7MN.   `7MF'    MMP\"\"MM\"\"YMM `7MM\"\"\"YMM   .M\"\"\"bgd MMP\"\"MM\"\"YMM \n" +
                "                                                                     ;MM:       MM    `Yb. MMMb    dPMM    MM    MMN.    M      P'   MM   `7   MM    `7  ,MI    \"Y P'   MM   `7 \n" +
                "                                                                    ,V^MM.      MM     `Mb M YM   ,M MM    MM    M YMb   M           MM        MM   d    `MMb.          MM      \n" +
                "                                                                   ,M  `MM      MM      MM M  Mb  M' MM    MM    M  `MN. M           MM        MMmmMM      `YMMNq.      MM      \n" +
                "                                                                   AbmmmqMA     MM     ,MP M  YM.P'  MM    MM    M   `MM.M           MM        MM   Y  , .     `MM      MM      \n" +
                "                                                                  A'     VML    MM    ,dP' M  `YM'   MM    MM    M     YMM           MM        MM     ,M Mb     dM      MM      \n" +
                "                                                                .AMA.   .AMMA..JMMmmmdP' .JML. `'  .JMML..JMML..JML.    YM         .JMML.    .JMMmmmmMMM P\"Ybmmd\"     .JMML.    \n" +
                "                                                                                                                                                                                \n" +
                "                                                                                                                                                                                ");
    }

    private void companyTestTitle() {
        System.out.println("                                                                                                                                                                                             \n" +
                "                                                                                                                                                                                             \n" +
                "                                                      .g8\"\"\"bgd   .g8\"\"8q. `7MMM.     ,MMF'`7MM\"\"\"Mq.   db      `7MN.   `7MF'`YMM'   `MM'    MMP\"\"MM\"\"YMM `7MM\"\"\"YMM   .M\"\"\"bgd MMP\"\"MM\"\"YMM \n" +
                "                                                    .dP'     `M .dP'    `YM. MMMb    dPMM    MM   `MM. ;MM:       MMN.    M    VMA   ,V      P'   MM   `7   MM    `7  ,MI    \"Y P'   MM   `7 \n" +
                "                                                    dM'       ` dM'      `MM M YM   ,M MM    MM   ,M9 ,V^MM.      M YMb   M     VMA ,V            MM        MM   d    `MMb.          MM      \n" +
                "                                                    MM          MM        MM M  Mb  M' MM    MMmmdM9 ,M  `MM      M  `MN. M      VMMP             MM        MMmmMM      `YMMNq.      MM      \n" +
                "                                                    MM.         MM.      ,MP M  YM.P'  MM    MM      AbmmmqMA     M   `MM.M       MM              MM        MM   Y  , .     `MM      MM      \n" +
                "                                                    `Mb.     ,' `Mb.    ,dP' M  `YM'   MM    MM     A'     VML    M     YMM       MM              MM        MM     ,M Mb     dM      MM      \n" +
                "                                                      `\"bmmmd'    `\"bmmd\"' .JML. `'  .JMML..JMML. .AMA.   .AMMA..JML.    YM     .JMML.          .JMML.    .JMMmmmmMMM P\"Ybmmd\"     .JMML.    \n" +
                "                                                                                                                                                                                             \n" +
                "                                                                                                                                                                                             ");
    }
}
