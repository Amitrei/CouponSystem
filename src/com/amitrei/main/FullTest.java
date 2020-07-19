package com.amitrei.main;


import com.amitrei.beans.Category;
import com.amitrei.beans.Company;
import com.amitrei.beans.Coupon;
import com.amitrei.beans.Customer;
import com.amitrei.dailyjob.CouponExpirationDailyJob;
import com.amitrei.dao.CouponsDAO;
import com.amitrei.dao.CustomersDAO;
import com.amitrei.db.ConnectionPool;
import com.amitrei.dbdao.CouponsDBDAO;
import com.amitrei.dbdao.CustomersDBDAO;
import com.amitrei.exceptions.AlreadyExistsException;
import com.amitrei.exceptions.DoesNotExistsException;
import com.amitrei.exceptions.IllegalActionException;
import com.amitrei.facade.AdminFacade;
import com.amitrei.facade.ClientFacade;
import com.amitrei.facade.CompanyFacade;
import com.amitrei.facade.CustomerFacade;
import com.amitrei.login.ClientType;
import com.amitrei.login.LoginManager;
import com.amitrei.utils.MyDateUtil;


import java.sql.Connection;
import java.sql.SQLException;

public class FullTest {
    MyDateUtil myDateUtil = new MyDateUtil();
    CouponsDAO couponsDAO = new CouponsDBDAO();
    AdminFacade adminFacade = new AdminFacade();
    CustomerFacade customerFacade = new CustomerFacade();
    CompanyFacade companyFacade = new CompanyFacade();
    CustomersDAO customersDAO = new CustomersDBDAO();
    CouponExpirationDailyJob dailyJob = new CouponExpirationDailyJob();


    public void testAll() {

        initTestTitle();
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            System.out.println("### Initialized connections ###");

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {

        }

        System.out.println();
        Coupon expiredCoupon = new Coupon(351, Category.FOOD, "EXPIRAED COUPON", "12% discount", myDateUtil.currentDate(), myDateUtil.expiredDateFromToday(-1), 5, 20, "image.png");
        couponsDAO.addCoupon(expiredCoupon);
        System.out.println("Coupon list before thread init");
        System.out.println(couponsDAO.getAllCoupons());

        Thread t1 = new Thread(dailyJob);

        System.out.println();
        System.out.println("### Initialize the daily job ### ");
        t1.start();


        /***
         *
         * Thread sleeping in order to let the dailyjob remove the coupon and printing in without delay
         *
         */

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();
        System.out.println(" Checking if expaired coupon got deleted by the daily job");
        System.out.println("Coupon list after thread init");
        System.out.println(couponsDAO.getAllCoupons());






        adminTestTitle();
        printTitle("ADMINISTRATOR LOGIN");
        ClientFacade LoggedInAsAdmin = null;
        try {
            LoggedInAsAdmin = LoginManager.getInstance().login("admin@admin.com", "1234", ClientType.Administrator);
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());


        }
        System.out.print("ADMIN LOGIN WITH WRONG PASSWORD AND USERNAME:  ");
        System.out.println(LoggedInAsAdmin);
        try {

            LoggedInAsAdmin = LoginManager.getInstance().login("admin@admin.com", "admin", ClientType.Administrator);
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());

        }

        System.out.print("ADMIN LOGIN WITH  PASSWORD AND USERNAME AND GETTING THE CURRECT FACADE: ");
        System.out.println(LoggedInAsAdmin.getClass());


        printTitle("ADDING COMPANY");
        Company company = null;
        try {
            company = new Company("Couponim", "couponim@couponim.com", "password");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("ADDING COMPANY:     " + company.toString());
        try {
            ((AdminFacade) LoggedInAsAdmin).addCompany(company);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("MAKING SURE COMPANY WAS ADDED BY GETTING COMPANIES FROM DB     " + ((AdminFacade) LoggedInAsAdmin).getAllCompanies());
        System.out.println();
        System.out.println("ADDING THE SAME COMPANY AGAIN:     " + company);
        try {
            ((AdminFacade) LoggedInAsAdmin).addCompany(company);
        } catch (AlreadyExistsException e) {
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
        } catch (IllegalActionException e) {
            System.out.println(e.getMessage());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());

        }


        System.out.println("COMPANY DETAILS FROM DB AFTER UPDATE     " + ((AdminFacade) LoggedInAsAdmin).getAllCompanies());
        System.out.println();
        System.out.println("TRYING TO CHANGE COMPANY NAME:");
        company.setName("Blala");

        try {
            ((AdminFacade) LoggedInAsAdmin).updateCompany(company);
        } catch (IllegalActionException e) {
            System.out.println(e.getMessage());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());

        }
        System.out.println();
        System.out.println("COMPANY DETAILS FROM DB AFTER UPDATE     " + ((AdminFacade) LoggedInAsAdmin).getAllCompanies());
        System.out.println();
        System.out.println("TRYING CHANGING ID:");
        company.setId(123);


        printTitle("DELETE COMPANY");
        System.out.println("** ADDING COUPONS AND PURCHASES TO THE COMPANY ** ");
        Coupon coupon = new Coupon(company.getId(), Category.WINTER, "Title", "Descreption", myDateUtil.currentDate(), myDateUtil.expiredDateFromToday(10), 100, 99.99, "Image.png");
        couponsDAO.addCoupon(coupon);

            couponsDAO.addCouponPurchase(62, couponsDAO.getCouponIDFromDB(coupon));

        System.out.println();
        System.out.println("DELETING COMPANY BY COMPANY ID:" + company.getId());
        System.out.println();
        System.out.println("BEFORE DELETE GETTING COMPANIES FROM DB     " + ((AdminFacade) LoggedInAsAdmin).getAllCompanies());

        try {
            ((AdminFacade) LoggedInAsAdmin).deleteCompany(company.getId());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();

        System.out.println("AFTER DELETE GETTING COMPANIES FROM DB     " + ((AdminFacade) LoggedInAsAdmin).getAllCompanies());

        printTitle("GETTING ALL COMPANIES");
        System.out.println("ADDING DUMMY COMPANIES");
        Company company0 = null;
        Company company1 = null;
        Company company2 = null;
        try {
            company0 = new Company("Couponim1", "111@gmail.com", "1111");
            company1 = new Company("Couponim2", "2222@gmail.com", "22222");
            company2 = new Company("Couponim3", "3333@gmail.com", "3333333");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

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
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("CHECKING IF CUSTOMER ADDED TO DB:" + adminFacade.getAllCustomers());
        System.out.println();
        System.out.println("TRYING TO ADD A CUSTOMER WITH THE SAME EMAIL:");
        Customer fakeCustomer = new Customer("moshe", "cohen", "Amitrei@gmail.com", "41414");

        try {
            adminFacade.addCustomer(fakeCustomer);
        } catch (AlreadyExistsException e) {
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
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }
        try {
            System.out.println("CHECKING UPDATE FROM DB: " + adminFacade.getOneCustomer(customer.getId()));
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("TRYING CHANGING ID:");
        customer.setId(123);


        printTitle("DELETING CUSTOMER");
        System.out.println("### ADDING COUPONS PURCHASE TO THE CUSTOMER ###");
        try {
            company0 = new Company("Couponim1", "111@gmail.com", "1111");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        try {
            adminFacade.addCompany(company0);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
        coupon = new Coupon(company0.getId(), Category.WINTER, "Title", "Descreption", myDateUtil.currentDate(), myDateUtil.expiredDateFromToday(10), 100, 99.99, "Image.png");
        couponsDAO.addCoupon(coupon);


            couponsDAO.addCouponPurchase(customer.getId(), couponsDAO.getCouponIDFromDB(coupon));


        System.out.println("PURCHASE ADDED SUCCSESSFULY: " + customersDAO.getCustomerCoupons(customer.getId()));
        System.out.println();
        System.out.println("DELETING THIS CUSTOMER " + customer);
        System.out.println("ALL CUSTOMERS FROM DB BEFORE DELETED " + adminFacade.getAllCustomers());

        try {
            adminFacade.deleteCustomer(customer.getId());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());

        }
        try {
            adminFacade.deleteCompany(company0.getId());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());

        }
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
        try {
            System.out.println(adminFacade.getOneCustomer(customer1.getId()));
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());

        }
        deleteCustomerDummies(customer1, customer2, customer3);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        companyTestTitle();
        printTitle("COMPANY LOGIN");

            System.out.println("MY TESTING COMPANY:" + adminFacade.getOneCompany(351));



        System.out.println("### TRYING TO LOG-IN WITH WRONG PASSWORD ###");
        try {
            System.out.println(LoginManager.getInstance().login("testCompany@gmail.com", "WrongPassword", ClientType.Company));
        }
        catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();
        System.out.println("### TRYING TO LOG-IN WITH CURRECT PASSWORD ### ");
        try {
            System.out.println(LoginManager.getInstance().login("testCompany@gmail.com", "1234", ClientType.Company).getClass());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());

        }
        CompanyFacade companyLoggedIn = null;
        try {
            companyLoggedIn = ((CompanyFacade) LoginManager.getInstance().login("testCompany@gmail.com", "1234", ClientType.Company));
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }


        printTitle("COMPANY ADDING COUPON");
        coupon = new Coupon(companyLoggedIn.getCompanyID(), Category.WINTER, "testTitle", "MyDescription", myDateUtil.currentDate(), myDateUtil.expiredDateFromToday(10), 100, 99.9, "image.png");
        System.out.println("ADDING THIS COUPON: " + coupon);
        try {
            companyLoggedIn.addCoupon(coupon);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("CHECKING IF COUPON IS ADDED TO DB:");
        System.out.println(companyLoggedIn.getCompanyCoupons());
        System.out.println("TRYING TO ADD THE SAME TITLE COUPON:");
        try {
            companyLoggedIn.addCoupon(coupon);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
        System.out.println("ADDING COUPON:");
        Coupon coupon2 = new Coupon(904, Category.WINTER, "testTitle", "MyDescription", myDateUtil.currentDate(), myDateUtil.expiredDateFromToday(10), 100, 99.9, "image.png");
        System.out.println(coupon2);
        try {
            companyLoggedIn.addCoupon(coupon2);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("CHECKING IF BOTH COUPONS HAVING THE SAME TITLE BUT DIFFRENTS COMAPNIES:");
        System.out.println(couponsDAO.getAllCoupons());
        try {
            companyLoggedIn.deleteCoupon(coupon2.getId());
        } catch (DoesNotExistsException e) {
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
        coupon.setTitle("UpdatedTitle");
        System.out.println(coupon);
        System.out.println("BEFORE UPDATING COUPON FROM DB:");
        System.out.println(companyLoggedIn.getCompanyCoupons());

            couponsDAO.updateCoupon(coupon.getId(), coupon);

        System.out.println("AFTER UPDATING COUPON FROM DB:");
        System.out.println(companyLoggedIn.getCompanyCoupons());


        printTitle("DELETING COUPON");

        System.out.println("### ADDING PURCHASE TO THE COUPON ###");

            couponsDAO.addCouponPurchase(62, coupon.getId());


        System.out.println("DELETING COUPON " + coupon);
        System.out.println("BEFORE DELETING COUPON FROM DB:");
        System.out.println(couponsDAO.getAllCoupons());
        System.out.println("AFTER DELETING COUPON FROM DB:");


        try {
            companyLoggedIn.deleteCoupon(coupon.getId());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(couponsDAO.getAllCoupons());

        printTitle("ALL COUPONS OF COMPANY");
        System.out.println("### ADDING COUPONS DUMMIES ###");
        System.out.println();
        System.out.println("GETTING ALL COUPONS OF THE CURRENT COMPANY:");

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
        System.out.println();
        System.out.println("ALL COUPONS BY MAXIMUM PRICE OF:100");
        System.out.println(companyLoggedIn.getCompanyCoupons(100.0));
        System.out.println();
        printTitle("COMPANY DETAILS");
        try {
            System.out.println(companyLoggedIn.getCompanyDetails());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());

        }


        customerTestTitle();
        printTitle("CUSTOMER LOGIN");
        CustomerFacade customerLoggedIn = null;
        try {
            customerLoggedIn = (CustomerFacade) LoginManager.getInstance().login("amit@gmail.com", "1234", ClientType.Customer);
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }

        Customer myCustomer = customerLoggedIn.getCustomerDetails();
        try {
            System.out.println("CUSTOMER LOGIN WITH WRONG DETAILS: " + LoginManager.getInstance().login("amit@gmail.com", "1234WRONG", ClientType.Customer));
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());

        }

        System.out.println("CUSTOMER LOGIN WITH CURRECT DETAILS: " + customerLoggedIn.getClass());
        System.out.println("LOGGED IN SUCCSSESFULY");
        printTitle("PURCHASE COUPON");

        coupon = new Coupon(351, Category.FOOD, "Test title", "Test description", myDateUtil.currentDate(), myDateUtil.expiredDateFromToday(10), 100, 99.9, "image.png");



        try {
            companyLoggedIn.addCoupon(coupon);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

            Coupon myCoupon = couponsDAO.getOneCoupon(coupon.getId());





            System.out.println("TRYING TO PURCHASE THIS COUPON" + myCoupon);


        try {
            customerLoggedIn.purchaseCoupon(myCoupon);
        } catch (IllegalActionException e) {
            System.out.println(e.getMessage());

        }
        catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }


        System.out.println("MAKING SURE IN DB COUPON PURCHASED ");
        System.out.println(customerLoggedIn.getCustomerCoupons());
        System.out.println();
        System.out.println("AFTER PURCHASING MAKING SURE THAT AMOUNT HAS CHANGED:" + myCoupon);
        System.out.println("TRYING TO RE-PURCHASE THE SAME COUPON");
        try {
            customerLoggedIn.purchaseCoupon(myCoupon);
        } catch (IllegalActionException e) {
            System.out.println(e.getMessage());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }


        myCoupon.setEndDate(myDateUtil.expiredDateFromToday(-1));
        try {
            companyLoggedIn.updateCoupon(myCoupon);
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }


        couponsDAO.deleteCouponPurchase(customerLoggedIn.getCustomerID(), coupon.getId());
        System.out.println();
        System.out.println("MAKING THE COUPON EXPIRED" + myCoupon);
        System.out.println("TRYING TO PURCHASE THE EXPIRED COUPON:");

        try {
            customerLoggedIn.purchaseCoupon(myCoupon);
        } catch (IllegalActionException e) {
            System.out.println(e.getMessage());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }

        couponsDAO.deleteCouponPurchase(customerLoggedIn.getCustomerID(), coupon.getId());
        myCoupon.setEndDate(myDateUtil.expiredDateFromToday(10));
        try {
            companyLoggedIn.updateCoupon(myCoupon);
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();

        Coupon newCoupon = new Coupon(351,Category.FOOD,"NO-AMOUNT-COUPON","blabla",myDateUtil.currentDate(),myDateUtil.expiredDateFromToday(10),0,99,"image");

        try {
            companyLoggedIn.addCoupon(newCoupon);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("CHANGING COUPON AMOUNT TO 0" + newCoupon);
        System.out.println("TRYING TO PURCHASE");
        try {
            customerLoggedIn.purchaseCoupon(newCoupon);
        } catch (IllegalActionException e) {
            System.out.println(e.getMessage());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }


        couponsDAO.deleteCoupon(newCoupon.getId());
        couponsDAO.deleteCoupon(myCoupon.getId());



        printTitle("ALL COUPONS OF CUSTOMERS");
        System.out.println("### MAKING DUMMY COUPONS AND PURCHASING THEM###");
//            Coupon coupon4=new Coupon(351,Category.FOOD,"DummyTitle1","DummyDescription1",myDateUtil.currentDate(),myDateUtil.expiredDate(10),10,30,"DummyImage1.png");
//            Coupon coupon5=new Coupon(351,Category.Electricity,"DummyTitle2","DummyDescription2",myDateUtil.currentDate(),myDateUtil.expiredDate(10),10,60,"DummyImage2.png");
//            Coupon coupon6=new Coupon(351,Category.Electricity,"DummyTitle3","DummyDescription3",myDateUtil.currentDate(),myDateUtil.expiredDate(10),10,90,"DummyImage3.png");
//            addDummyCoupons(coupon4,coupon5,coupon6);
//            try {
//                customerLoggedIn.purchaseCoupon(coupon4);
//                customerLoggedIn.purchaseCoupon(coupon5);
//                customerLoggedIn.purchaseCoupon(coupon6);
//            } catch (IllegalActionException e) {
//                System.out.println(e.getMessage());
//            } catch (DoesNotExistsException e) {
//                System.out.println(e.getMessage());
//            } catch (IllegalActionException e) {
//                System.out.println(e.getMessage());
//            } catch (IllegalActionException e) {
//                System.out.println(e.getMessage());
//            }
        System.out.println("ALL COUPONS OF CUSTOMER");
        System.out.println(customerLoggedIn.getCustomerCoupons());
        System.out.println();
        System.out.println("ALL COUPONS OF CUSTOMER BY CATEGORY:" + Category.Electricity);
        System.out.println(customerLoggedIn.getCustomerCoupons(Category.Electricity));
        System.out.println();
        System.out.println("ALL COUPONS OF CUSTOMER MAXIMUM PRICE OF: 50");
        System.out.println(customerLoggedIn.getCustomerCoupons(50.0));


        printTitle("CUSTOMER DETAILS");
        System.out.println(myCustomer);







//            deleteDummyCoupons(coupon4,coupon5,coupon6);


        printCloseTest();
        System.out.println("### Shuting down daily job ###");
        dailyJob.stopIt();
        System.out.println("### Closing all connections ###");
        try {
            ConnectionPool.getInstance().closeAllConnections();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } catch (InterruptedException e) {
//            System.out.println(e.getMessage());
//        } catch (DoesNotExistsException e) {
//            System.out.println(e.getMessage());
//        } catch (DoesNotExistsException e) {
//            System.out.println(e.getMessage());
    }


    private void printTitle(String title) {
        System.out.println();
        System.out.println("*************************************************************************************************       ~          " + title + "              ~          *************************************************************************************************************************************************************************************************");
        System.out.println();

    }




    private void addDummyCoupons(Coupon coupon1, Coupon coupon2, Coupon coupon3) {
        CompanyFacade companyLoggedIn = null;
        try {
            companyLoggedIn = ((CompanyFacade) LoginManager.getInstance().login("testCompany@gmail.com", "1234", ClientType.Company));
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }


        try {
            companyLoggedIn.addCoupon(coupon1);
            companyLoggedIn.addCoupon(coupon2);
            companyLoggedIn.addCoupon(coupon3);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

    }


    private void deleteDummyCoupons(Coupon coupon1, Coupon coupon2, Coupon coupon3) {
        CompanyFacade companyLoggedIn = null;
        try {
            companyLoggedIn = ((CompanyFacade) LoginManager.getInstance().login("testCompany@gmail.com", "1234", ClientType.Company));
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }


        try {
            companyLoggedIn.deleteCoupon(coupon1.getId());
            companyLoggedIn.deleteCoupon(coupon2.getId());
            companyLoggedIn.deleteCoupon(coupon3.getId());

        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }

    }

    private void addDummyCompanies(Company company0, Company company1, Company company2) {
        try {

            var admin = LoginManager.getInstance().login("admin@admin.com", "admin", ClientType.Administrator);
            ((AdminFacade) admin).addCompany(company0);
            ((AdminFacade) admin).addCompany(company1);
            ((AdminFacade) admin).addCompany(company2);


        } catch (DoesNotExistsException e) {
            e.printStackTrace();
        }  catch (AlreadyExistsException e) {
            e.printStackTrace();
        }

    }

    private void deleteDummyCompanies(Company company0, Company company1, Company company2) {
        try {

            var admin = LoginManager.getInstance().login("admin@admin.com", "admin", ClientType.Administrator);
            ((AdminFacade) admin).deleteCompany(company0.getId());
            ((AdminFacade) admin).deleteCompany(company1.getId());
            ((AdminFacade) admin).deleteCompany(company2.getId());


        } catch (DoesNotExistsException e) {
            e.printStackTrace();
        }

    }


    private void creatingCustomerDummies(Customer customer1, Customer customer2, Customer customer3) {
        try {
            adminFacade.addCustomer(customer1);
            adminFacade.addCustomer(customer2);
            adminFacade.addCustomer(customer3);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }


    private void deleteCustomerDummies(Customer customer1, Customer customer2, Customer customer3) {
        try {
            adminFacade.deleteCustomer(customer1.getId());
            adminFacade.deleteCustomer(customer2.getId());
            adminFacade.deleteCustomer(customer3.getId());

        } catch (DoesNotExistsException e) {
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

    private void customerTestTitle() {
        System.out.println("                                                                                                                                                                                              \n" +
                "                                                                                                                                                                                              \n" +
                "                                      .g8\"\"\"bgd `7MMF'   `7MF' .M\"\"\"bgd MMP\"\"MM\"\"YMM   .g8\"\"8q.   `7MMM.     ,MMF'`7MM\"\"\"YMM  `7MM\"\"\"Mq.      MMP\"\"MM\"\"YMM `7MM\"\"\"YMM   .M\"\"\"bgd MMP\"\"MM\"\"YMM \n" +
                "                                    .dP'     `M   MM       M  ,MI    \"Y P'   MM   `7 .dP'    `YM.   MMMb    dPMM    MM    `7    MM   `MM.     P'   MM   `7   MM    `7  ,MI    \"Y P'   MM   `7 \n" +
                "                                    dM'       `   MM       M  `MMb.          MM      dM'      `MM   M YM   ,M MM    MM   d      MM   ,M9           MM        MM   d    `MMb.          MM      \n" +
                "                                    MM            MM       M    `YMMNq.      MM      MM        MM   M  Mb  M' MM    MMmmMM      MMmmdM9            MM        MMmmMM      `YMMNq.      MM      \n" +
                "                                    MM.           MM       M  .     `MM      MM      MM.      ,MP   M  YM.P'  MM    MM   Y  ,   MM  YM.            MM        MM   Y  , .     `MM      MM      \n" +
                "                                    `Mb.     ,'   YM.     ,M  Mb     dM      MM      `Mb.    ,dP'   M  `YM'   MM    MM     ,M   MM   `Mb.          MM        MM     ,M Mb     dM      MM      \n" +
                "                                      `\"bmmmd'     `bmmmmd\"'  P\"Ybmmd\"     .JMML.      `\"bmmd\"'   .JML. `'  .JMML..JMMmmmmMMM .JMML. .JMM.       .JMML.    .JMMmmmmMMM P\"Ybmmd\"     .JMML.    \n" +
                "                                                                                                                                                                                              \n" +
                "                                                                                                                                                                                              ");
    }

    private void initTestTitle() {
        System.out.println("\n" +
                "                                                                                                                                                                                                                   \n" +
                "                                                                                                                                                                                                                   \n" +
                "                `7MMF'    `7MN.   `7MF'    `7MMF'    MMP\"\"MM\"\"YMM     `7MMF'          db          MMP\"\"MM\"\"YMM     `7MM\"\"\"YMM                          MMP\"\"MM\"\"YMM     `7MM\"\"\"YMM       .M\"\"\"bgd     MMP\"\"MM\"\"YMM \n" +
                "                  MM        MMN.    M        MM      P'   MM   `7       MM           ;MM:         P'   MM   `7       MM    `7                          P'   MM   `7       MM    `7      ,MI    \"Y     P'   MM   `7 \n" +
                "                  MM        M YMb   M        MM           MM            MM          ,V^MM.             MM            MM   d                                 MM            MM   d        `MMb.              MM      \n" +
                "                  MM        M  `MN. M        MM           MM            MM         ,M  `MM             MM            MMmmMM                                 MM            MMmmMM          `YMMNq.          MM      \n" +
                "                  MM        M   `MM.M        MM           MM            MM         AbmmmqMA            MM            MM   Y  ,                              MM            MM   Y  ,     .     `MM          MM      \n" +
                "                  MM        M     YMM        MM           MM            MM        A'     VML           MM            MM     ,M                              MM            MM     ,M     Mb     dM          MM      \n" +
                "                .JMML.    .JML.    YM      .JMML.       .JMML.        .JMML.    .AMA.   .AMMA.       .JMML.        .JMMmmmmMMM                            .JMML.        .JMMmmmmMMM     P\"Ybmmd\"         .JMML.    \n" +
                "                                                                                                                                                                                                                   \n" +
                "                                                                                                                                                                                                                   \n");
    }


    private void printCloseTest() {

        System.out.println("\n" +
                "                                                                                                                                                                                                       \n" +
                "                                                                                                                                                                                                       \n" +
                "                      .g8\"\"\"bgd     `7MMF'            .g8\"\"8q.        .M\"\"\"bgd     `7MMF'    `7MN.   `7MF'      .g8\"\"\"bgd                  MMP\"\"MM\"\"YMM     `7MM\"\"\"YMM       .M\"\"\"bgd     MMP\"\"MM\"\"YMM \n" +
                "                    .dP'     `M       MM            .dP'    `YM.     ,MI    \"Y       MM        MMN.    M      .dP'     `M                  P'   MM   `7       MM    `7      ,MI    \"Y     P'   MM   `7 \n" +
                "                    dM'       `       MM            dM'      `MM     `MMb.           MM        M YMb   M      dM'       `                       MM            MM   d        `MMb.              MM      \n" +
                "                    MM                MM            MM        MM       `YMMNq.       MM        M  `MN. M      MM                                MM            MMmmMM          `YMMNq.          MM      \n" +
                "                    MM.               MM      ,     MM.      ,MP     .     `MM       MM        M   `MM.M      MM.    `7MMF'                     MM            MM   Y  ,     .     `MM          MM      \n" +
                "                    `Mb.     ,'       MM     ,M     `Mb.    ,dP'     Mb     dM       MM        M     YMM      `Mb.     MM                       MM            MM     ,M     Mb     dM          MM      \n" +
                "                      `\"bmmmd'      .JMMmmmmMMM       `\"bmmd\"'       P\"Ybmmd\"      .JMML.    .JML.    YM        `\"bmmmdPY                     .JMML.        .JMMmmmmMMM     P\"Ybmmd\"         .JMML.    \n" +
                "                                                                                                                                                                                                       \n" +
                "                                                                                                                                                                                                       \n");
    }
}