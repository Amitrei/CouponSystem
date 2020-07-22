package com.amitrei.test;


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
import com.amitrei.security.ClientType;
import com.amitrei.security.LoginManager;
import com.amitrei.utils.DateUtil;


import java.sql.SQLException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FullTest {
    DateUtil myDateUtil = new DateUtil();
    CouponsDAO couponsDAO = new CouponsDBDAO();
    AdminFacade adminFacade = new AdminFacade();
    CustomersDAO customersDAO = new CustomersDBDAO();
    CouponExpirationDailyJob dailyJob = new CouponExpirationDailyJob();


    public void testAll() {

        initTestTitle();
        System.out.println("### Initialized connections ###");
        System.out.println();
        System.out.println();
        Coupon expiredCoupon = new Coupon(351, Category.FOOD, "EXPIRAED COUPON", "12% discount", myDateUtil.currentDate(), myDateUtil.expiredDateFromToday(-1), 5, 20, "image.png");
        couponsDAO.addCoupon(expiredCoupon);
        System.out.println();
        System.out.println("Coupon list with expired coupon before thread init");
        couponsDAO.getAllCoupons().stream().forEach(System.out::print);

        Thread t1 = new Thread(dailyJob);

        System.out.println();
        System.out.println("### Initialize the daily job ### ");
        t1.start();


        /***
         *
         * Thread sleeping in order to let the dailyjob remove the expired coupon and printing in without delay
         *
         */


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();
        System.out.println("Checking if expaired coupon got deleted by the daily job");
        couponsDAO.getAllCoupons().stream().forEach(System.out::print);


        /**
         *
         *
         * Admin facade test
         *
         **/

        adminTestTitle();
        printTitle("ADMINISTRATOR LOGIN");
        ClientFacade LoggedInAsAdmin = null;

        System.out.print("ADMIN LOGIN WITH WRONG PASSWORD AND USERNAME:  ");
        System.out.println();
        try {
            LoggedInAsAdmin = LoginManager.getInstance().login("admin@admin.com", "1234", ClientType.Administrator);
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());


        }
        System.out.println(LoggedInAsAdmin);
        try {

            LoggedInAsAdmin = LoginManager.getInstance().login("admin@admin.com", "admin", ClientType.Administrator);
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());

        }

        System.out.print("ADMIN LOGIN WITH CURRECT PASSWORD AND USERNAME AND GETTING THE CURRECT FACADE: ");
        System.out.println(LoggedInAsAdmin.getClass());


        printTitle("ADDING COMPANY");


        Company company = new Company("Couponim", "couponim@couponim.com", "password");

        System.out.println("ADDING COMPANY:     " + company.toString());
        try {
            ((AdminFacade) LoggedInAsAdmin).addCompany(company);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }


        System.out.println("MAKING SURE COMPANY WAS ADDED BY GETTING COMPANIES FROM DB     ");
        ((AdminFacade) LoggedInAsAdmin).getAllCompanies().stream().forEach(System.out::print);


        System.out.println();
        printSeperationLine();
        Company companyWithSameName=new Company(company.getName(),"TestComp@gmail.com","1234");
        System.out.println("ADDING COMPANY WITH THE SAME COMPANY NAME:     " + companyWithSameName);
        try {
            ((AdminFacade) LoggedInAsAdmin).addCompany(companyWithSameName);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();

        printSeperationLine();
        System.out.println();

         companyWithSameName=new Company("TestComp",company.getEmail(),"1234");
        System.out.println("ADDING COMPANY WITH THE SAME COMPANY EMAIL:     " + companyWithSameName);
        try {
            ((AdminFacade) LoggedInAsAdmin).addCompany(companyWithSameName);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();
        printTitle("UPDATE COMPANY");
        System.out.println("COMPANY DETAILS BEFORE UPDATE FROM THE DB     " + ((AdminFacade) LoggedInAsAdmin).getOneCompany(company.getId()));

        company.setEmail("Changedemail@gmail.com");
        company.setPassword("12345");
        System.out.println("UPDATING COMPANY DETAILS:     " + company);
        try {
            ((AdminFacade) LoggedInAsAdmin).updateCompany(company);
        } catch (IllegalActionException e) {
            System.out.println(e.getMessage());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());

        }


        System.out.println("COMPANY DETAILS FROM DB AFTER UPDATE     " + ((AdminFacade) LoggedInAsAdmin).getOneCompany(company.getId()));
        System.out.println();
        printSeperationLine();
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
        printSeperationLine();
        System.out.println();
        System.out.println("TRYING CHANGING ID:");
        company.setId(123);


        printTitle("DELETE COMPANY");
        System.out.println("** ADDING COUPONS AND PURCHASES TO THE COMPANY ** ");
        Coupon coupon = new Coupon(company.getId(), Category.WINTER, "Best Muffins in town", "Descreption", myDateUtil.currentDate(), myDateUtil.expiredDateFromToday(10), 100, 99.99, "Image.png");
        couponsDAO.addCoupon(coupon);
        couponsDAO.addCouponPurchase(62, couponsDAO.getCouponIDFromDB(coupon));


        couponsDAO.getAllCouponsOfCompany(company.getId()).stream().flatMap(couponOfCompany -> Stream.of(couponOfCompany.getId()+"-" +couponOfCompany.getTitle())).collect(Collectors.toList()).forEach(System.out::println);
        System.out.println();
        System.out.println("DELETING COMPANY BY COMPANY ID:" + company.getId());
        System.out.println();
        System.out.println("BEFORE DELETE GETTING COMPANIES FROM DB     ");
        ((AdminFacade) LoggedInAsAdmin).getAllCompanies().stream().forEach(System.out::print);

        try {
            ((AdminFacade) LoggedInAsAdmin).deleteCompany(company.getId());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();

        System.out.println("AFTER DELETE GETTING COMPANIES FROM DB     ");
        ((AdminFacade) LoggedInAsAdmin).getAllCompanies().stream().forEach(System.out::print);


        printTitle("GETTING ALL COMPANIES");

        System.out.println("ADDING DUMMY COMPANIES AND GETTING ALL COMPANIES FROM DB");


        Company company0 = new Company("Couponim1", "111@gmail.com", "1111");
        Company company1 = new Company("Couponim2", "2222@gmail.com", "22222");
        Company company2 = new Company("Couponim3", "3333@gmail.com", "3333333");
        addDummyCompanies(company0, company1, company2);


        ((AdminFacade) LoggedInAsAdmin).getAllCompanies().stream().forEach(System.out::print);

        printTitle("GETTING SINGLE COMPANY BY ID");
        System.out.println("GETTING COMPANY BY A SINGLE ID: " + company0.getId());
        System.out.println(((AdminFacade) LoggedInAsAdmin).getOneCompany(company0.getId()));
        deleteDummyCompanies(company0, company1, company2);


        printTitle("ADDING NEW CUSTOMER");
        System.out.println();
        Customer customer = new Customer("Amit", "Rei", "Amitrei@gmail.com", "1234");
        System.out.println("ADDING THIS CUSTOMER:" + customer);
        try {
            adminFacade.addCustomer(customer);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("CHECKING IF CUSTOMER ADDED TO DB:");
        adminFacade.getAllCustomers().stream().forEach(System.out::print);
        System.out.println();
        printSeperationLine();
        System.out.println();
        System.out.println("TRYING TO ADD A CUSTOMER WITH THE SAME EMAIL:");
        Customer fakeCustomer = new Customer("moshe", "cohen", "Amitrei@gmail.com", "41414");
        System.out.println(fakeCustomer);
        System.out.println();


        try {
            adminFacade.addCustomer(fakeCustomer);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        printTitle("UPDATING CUSTOMER");
        System.out.println("UPDATING CUSTOMER FROM ");
        System.out.println(customer);
        customer.setFirstName("Moshe");
        customer.setEmail("Moshes@gmail.com");
        customer.setLastName("Moshiko");
        customer.setPassword("4321");
        System.out.println("TO");
        System.out.println(customer);
        try {
            adminFacade.updateCustomer(customer);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }
        try {
            System.out.println("CHECKING UPDATE FROM DB: ");
            System.out.println( adminFacade.getOneCustomer(customer.getId()));
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
        printSeperationLine();
        System.out.println("TRYING CHANGING ID:");
        customer.setId(123);


        printTitle("DELETING CUSTOMER");
        System.out.println("### ADDING COUPONS PURCHASE TO THE CUSTOMER ###");

        company0 = new Company("Couponim1", "111@gmail.com", "1111");

        try {
            adminFacade.addCompany(company0);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
        coupon = new Coupon(company0.getId(), Category.WINTER, "Title", "Descreption", myDateUtil.currentDate(), myDateUtil.expiredDateFromToday(10), 100, 99.99, "Image.png");
        couponsDAO.addCoupon(coupon);


        couponsDAO.addCouponPurchase(customer.getId(), couponsDAO.getCouponIDFromDB(coupon));


        System.out.println("GETTING CUSTOMER COUPONS FROM DB");
        System.out.println(customersDAO.getCustomerCoupons(customer.getId()));
        System.out.println();
        printSeperationLine();
        System.out.println();
        System.out.println("DELETING THIS CUSTOMER " + customer);
        System.out.println("ALL CUSTOMERS FROM DB BEFORE DELETED");
        adminFacade.getAllCustomers().stream().forEach(System.out::print);

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

        // Deleting coupon to prevent coupon already exists on the next test run
        couponsDAO.deleteCoupon(couponsDAO.getCouponIDFromDB(coupon));


        System.out.println();
        System.out.println("ALL CUSTOMERS FROM DB AFTER DELETED ");
        adminFacade.getAllCustomers().stream().forEach(System.out::print);
        System.out.println();
        printTitle("GETTING ALL THE CUSTOMERS:");
        System.out.println("### CREATING DUMMY CUSTOMERS ###");
        Customer customer1 = new Customer("avi", "ron", "Aviron@gmail.com", "1234");
        Customer customer2 = new Customer("Simha", "Rif", "simhaRif@gmail.com", "1234");
        Customer customer3 = new Customer("Ram", "Kol", "RamKol@gmail.com", "1234");
        creatingCustomerDummies(customer1, customer2, customer3);
        System.out.println("ALL CUSTOMERS:");

        adminFacade.getAllCustomers().stream().forEach(System.out::print);

        printTitle("GETTING A SINGLE CUSTOMER USING CUSTOMER ID");
        System.out.println("GETTING THE DUMMIE CUSTOMER USING HIS ID: " + customer1.getId());
        try {
            System.out.println(adminFacade.getOneCustomer(customer1.getId()));
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());

        }

        //Deleting dummies to preven exception next test run
        deleteCustomerDummies(customer1, customer2, customer3);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();


        /**
         *
         *
         * COMPANY FACADE TEST
         *
         *
         */


        companyTestTitle();
        printTitle("COMPANY LOGIN");

        System.out.println("MY TESTING COMPANY:" + adminFacade.getOneCompany(351));


        System.out.println("### TRYING TO LOG-IN WITH WRONG PASSWORD ###");
        ClientFacade wrongPassword = null;

        try {
            System.out.println(wrongPassword = LoginManager.getInstance().login("testCompany@gmail.com", "WrongPassword", ClientType.Company));

        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(wrongPassword);
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
        companyLoggedIn.getCompanyCoupons().stream().forEach(System.out::print);
        System.out.println();
        printSeperationLine();
        System.out.println();
        System.out.println("TRYING TO ADD THE SAME TITLE COUPON ON THE SAME COMPANY:");
        try {
            companyLoggedIn.addCoupon(coupon);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
        printSeperationLine();
        System.out.println();
        System.out.println("ADDING ANOTHER COUPON WITH THE SAME TITLE BUT DIFFRENT COMPANY:");
        Coupon coupon2 = new Coupon(904, Category.WINTER, "testTitle", "MyDescription", myDateUtil.currentDate(), myDateUtil.expiredDateFromToday(10), 100, 99.9, "image.png");
        System.out.print(coupon2);
        try {
            companyLoggedIn.addCoupon(coupon2);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
        System.out.println("CHECKING FROM DB IF BOTH COUPONS HAVING THE SAME TITLE BUT DIFFRENTS COMAPNIES:");
        couponsDAO.getAllCoupons().stream().filter(coupon1 -> coupon1.getTitle().equals("testTitle")).collect(Collectors.toList()).forEach(System.out::print);

        //Deleting coupon to prevent an a exception on the next test run
        try {
            companyLoggedIn.deleteCoupon(coupon2.getId());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }


        printTitle("UPDATING COUPON");
        System.out.println("UPDATE COUPON ID:");
        coupon.setId(123);
        System.out.println();
        printSeperationLine();
        System.out.println();
        System.out.println("UPDATE COMPANY ID:");
        System.out.println("cannot update company id ( there is no setter  + no companyId field in query statment)");
        System.out.println();
        printSeperationLine();


        System.out.println();

        System.out.println("UPDATING COUPON DETAILS:");
        coupon.setAmount(9999);
        coupon.setCategory(Category.FOOD);
        coupon.setImage("ChangedImage.jpeg");
        coupon.setPrice(9999);
        coupon.setTitle("UPDATED");
        System.out.println(coupon);
        System.out.println("BEFORE UPDATING COUPON FROM DB:");
        System.out.println(couponsDAO.getOneCoupon(coupon.getId()));


        try {
            companyLoggedIn.updateCoupon(coupon);
        } catch (IllegalActionException e) {

            System.out.println(e.getMessage());
        }


        System.out.println();
        System.out.println("AFTER UPDATING COUPON FROM DB:");
        System.out.println(couponsDAO.getOneCoupon(coupon.getId()));


        printTitle("DELETING COUPON");

        System.out.println("### ADDING PURCHASE TO THE COUPON ###");
        couponsDAO.addCouponPurchase(62, coupon.getId());
        System.out.println();


        System.out.println("DELETING COUPON " + coupon);
        System.out.println("BEFORE DELETING COUPON FROM DB:");
        couponsDAO.getAllCoupons().stream().forEach(System.out::print);
        System.out.println();
        System.out.println("AFTER DELETING COUPON FROM DB:");


        try {
            companyLoggedIn.deleteCoupon(coupon.getId());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }

        couponsDAO.getAllCoupons().stream().forEach(System.out::print);

        printTitle("ALL COUPONS OF COMPANY");
        System.out.println("### ADDING COUPONS DUMMIES ###");
        System.out.println();
        System.out.println("GETTING ALL COUPONS OF THE CURRENT COMPANY:");

        companyLoggedIn.getCompanyCoupons().stream().forEach(System.out::print);


        printTitle("ALL COUPONS OF SPECIFIC CATEGORY");
        System.out.println("ALL COUPONS OF THE SPECIFIC CATEGORY:" + Category.FOOD);
        System.out.println(companyLoggedIn.getCompanyCoupons(Category.FOOD));
        System.out.println();
        printSeperationLine();
        System.out.println();
        System.out.println("ALL COUPONS OF THE SPECIFIC CATEGORY:" + Category.Electricity);
        companyLoggedIn.getCompanyCoupons(Category.Electricity).stream().forEach(System.out::print);

        printTitle("ALL COUPONS BY MAXIMUM PRICE");
        System.out.println("ALL COUPONS BY MAXIMUM PRICE OF:30");
        System.out.println(companyLoggedIn.getCompanyCoupons(30.0));
        System.out.println();
        printSeperationLine();
        System.out.println();
        System.out.println("ALL COUPONS BY MAXIMUM PRICE OF:60");
        System.out.println(companyLoggedIn.getCompanyCoupons(60.0));
        System.out.println();
        printSeperationLine();
        System.out.println();
        System.out.println("ALL COUPONS BY MAXIMUM PRICE OF:100");
        System.out.println(companyLoggedIn.getCompanyCoupons(100.0));
        System.out.println();
        printTitle("COMPANY DETAILS");

        System.out.println(companyLoggedIn.getCompanyDetails());

/**
 *
 *
 * CUSTOMER FACADE TEST
 *
 *
 */
        customerTestTitle();
        printTitle("CUSTOMER LOGIN");

        System.out.println("### MY TEST CUSTOMER ###");
        System.out.println(customersDAO.getOneCustomer("amit@gmail.com"));
        CustomerFacade customerLoggedIn = null;
        try {
            customerLoggedIn = (CustomerFacade) LoginManager.getInstance().login("amit@gmail.com", "1234", ClientType.Customer);
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }


        System.out.println("CUSTOMER LOGIN WITH WRONG DETAILS:");
        ClientFacade  wrongCustomerLoggedIn=null;
        try {
            wrongCustomerLoggedIn =LoginManager.getInstance().login("amit@gmail.com", "1234WRONG", ClientType.Customer);
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());

        }

        System.out.println(wrongCustomerLoggedIn);
        System.out.println();
        System.out.println("CUSTOMER LOGIN WITH CURRECT DETAILS: " + customerLoggedIn.getClass());


        printTitle("PURCHASE COUPON");
        coupon = new Coupon(351, Category.FOOD, "Test title", "Test description", myDateUtil.currentDate(), myDateUtil.expiredDateFromToday(10), 100, 99.9, "image.png");

        try {
            companyLoggedIn.addCoupon(coupon);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        Coupon myCoupon = couponsDAO.getOneCoupon(coupon.getId());
        String tempCoupon = myCoupon.toString();

        System.out.println("TRYING TO PURCHASE THIS COUPON" + myCoupon);


        try {
            customerLoggedIn.purchaseCoupon(myCoupon);
        } catch (IllegalActionException e) {
            System.out.println(e.getMessage());

        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }


        System.out.println("MAKING SURE COUPON PURCHASED BY GETTING CUSTOMER COUPONS ");
        customerLoggedIn.getCustomerCoupons().stream().forEach(System.out::print);
        System.out.println();
        printSeperationLine();
        System.out.println();
        System.out.println("AFTER PURCHASING MAKING SURE THAT AMOUNT HAS CHANGED:");
        System.out.println(tempCoupon);
        System.out.println(myCoupon);
        System.out.println();
        printSeperationLine();
        System.out.println();
        System.out.println("TRYING TO RE-PURCHASE THE SAME COUPON");
        try {
            customerLoggedIn.purchaseCoupon(myCoupon);
        } catch (IllegalActionException e) {
            System.out.println(e.getMessage());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }


        System.out.println();
        printSeperationLine();
        myCoupon.setEndDate(myDateUtil.expiredDateFromToday(-10));
        try {
            companyLoggedIn.updateCoupon(myCoupon);
        } catch (IllegalActionException e) {
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
        } catch (IllegalActionException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();

        Coupon newCoupon = new Coupon(351, Category.FOOD, "NO-AMOUNT-COUPON", "blabla", myDateUtil.currentDate(), myDateUtil.expiredDateFromToday(10), 0, 99, "image");

        try {
            companyLoggedIn.addCoupon(newCoupon);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
        printSeperationLine();
        System.out.println();
        System.out.println("PURCHASING COUPON WITH AMOUNT 0" + newCoupon);
        System.out.println("TRYING TO PURCHASE");
        try {
            customerLoggedIn.purchaseCoupon(newCoupon);
        } catch (IllegalActionException e) {
            System.out.println(e.getMessage());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }

        //prevent exception next test
        couponsDAO.deleteCoupon(newCoupon.getId());
        couponsDAO.deleteCoupon(myCoupon.getId());


        printTitle("ALL COUPONS OF CUSTOMERS");
        System.out.println("ALL COUPONS OF CUSTOMER");
        customerLoggedIn.getCustomerCoupons().stream().forEach(System.out::print);

        printSeperationLine();
        System.out.println();
        System.out.println("ALL COUPONS OF CUSTOMER BY CATEGORY:" + Category.Electricity);
        System.out.println(customerLoggedIn.getCustomerCoupons(Category.Electricity));
        System.out.println();
        printSeperationLine();
        System.out.println("ALL COUPONS OF CUSTOMER MAXIMUM PRICE OF: 50");
        System.out.println(customerLoggedIn.getCustomerCoupons(50.0));


        printTitle("CUSTOMER DETAILS");
        System.out.println(customerLoggedIn.getCustomerDetails());


        printCloseTest();
        System.out.println("### Shuting down daily job ###");


        /**
         * CLOSING THREAD BY INTERPURTING THE 24 HOURS SLEEP BEFORE SHUTTING DOWN
         *
         */
        t1.interrupt();
        dailyJob.stop();


        System.out.println("### Closing all connections ###");
        try {
            ConnectionPool.getInstance().closeAllConnections();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private void printSeperationLine() {
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
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
        } catch (AlreadyExistsException e) {
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