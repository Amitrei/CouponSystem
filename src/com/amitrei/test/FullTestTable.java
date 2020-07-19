package com.amitrei.test;


import com.amitrei.beans.Category;
import com.amitrei.beans.Company;
import com.amitrei.beans.Coupon;
import com.amitrei.beans.Customer;
import com.amitrei.dailyjob.CouponExpirationDailyJob;
import com.amitrei.dao.CompaniesDAO;
import com.amitrei.dao.CouponsDAO;
import com.amitrei.dao.CustomersDAO;
import com.amitrei.db.ConnectionPool;
import com.amitrei.dbdao.CompaniesDBDAO;
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
import com.blinkfox.minitable.MiniTable;


import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FullTestTable {
    DateUtil myDateUtil = new DateUtil();
    CouponsDAO couponsDAO = new CouponsDBDAO();
    AdminFacade adminFacade = new AdminFacade();
    CustomerFacade customerFacade = new CustomerFacade();
    CompanyFacade companyFacade = new CompanyFacade();
    CustomersDAO customersDAO = new CustomersDBDAO();
    CompaniesDAO companyDAO = new CompaniesDBDAO();
    CouponExpirationDailyJob dailyJob = new CouponExpirationDailyJob();


    public void testAll() {

        initTestTitle();
        System.out.println("### Initialized connections ###");
        System.out.println();
        System.out.println();
        Coupon expiredCoupon = new Coupon(351, Category.FOOD, "EXPIRAED COUPON", "12% discount", myDateUtil.currentDate(), myDateUtil.expiredDateFromToday(-1), 5, 20, "image.png");
        couponsDAO.addCoupon(expiredCoupon);
        System.out.println("Coupon list before thread init");
        List<?> col1 = couponsDAO.getAllCoupons().stream().map(coupon -> coupon.getTitle()).collect(Collectors.toList());
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

        List<?> col2 = couponsDAO.getAllCoupons().stream().map(coupon -> coupon.getTitle()).collect(Collectors.toList());


        String table = new MiniTable()
                .addHeaders("Before thread actived coupons titles from db", "After thread activated coupons titles from db")
                .addDatas(col1,col2)
                .render();
        System.out.println(table);

        adminTestTitle();
        printTitle("ADMINISTRATOR LOGIN");
        ClientFacade LoggedInAsAdmin = null;
        try {
            LoggedInAsAdmin = LoginManager.getInstance().login("admin@admin.com", "1234", ClientType.Administrator);
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());


        }

        var varCol = LoggedInAsAdmin;

        try {

            LoggedInAsAdmin = LoginManager.getInstance().login("admin@admin.com", "admin", ClientType.Administrator);
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());

        }


        var varCol2 = LoggedInAsAdmin.getClass().toString();

         table = new MiniTable("Admin login test")
                .addHeaders("Correct login details", "Wrong login details")
                .addDatas(varCol2,varCol)
                .render();
        System.out.println(table);




        printTitle("ADDING COMPANY");
        col1 = adminFacade.getAllCompanies().stream().map(company -> company.getName()).collect(Collectors.toList());
        Company company = null;

            company = new Company("Couponim", "couponim@couponim.com", "password");

        System.out.println("ADDING COMPANY:     " + company.toString());
        try {
            ((AdminFacade) LoggedInAsAdmin).addCompany(company);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        col2 = adminFacade.getAllCompanies().stream().map(company2 -> company2.getName()).collect(Collectors.toList());






        table = new MiniTable("Adding a company to the DB: " +company.getName() )
                .addHeaders("Before adding", "After adding")
                .addDatas(col1,col2)
                .render();
        System.out.println(table);


        Company falseCompany= new Company("Amit","Amit@gmail.ocm","1234");


        falseCompany.setEmail(company.getEmail());
        var colException ="";
        try {
            ((AdminFacade) LoggedInAsAdmin).addCompany(falseCompany);
        } catch (AlreadyExistsException e) {
            colException =e.getMessage();
        }
        falseCompany.setEmail("abadra@gmail.com");
        falseCompany.setName(company.getName());

        try {
            ((AdminFacade) LoggedInAsAdmin).addCompany(falseCompany);
        } catch (AlreadyExistsException e) {
            colException =e.getMessage();
        }

        col1 = adminFacade.getAllCompanies().stream().map(company3 -> company3.getName()).collect(Collectors.toList());

        table = new MiniTable("Trying add the same email company: " +company.getEmail() )
                .addHeaders("Before adding", "After adding")
                .addDatas(col1,colException)
                .render();
        System.out.println(table);


  table = new MiniTable("Trying add the same name company: " +company.getName() )
                .addHeaders("Before adding", "After adding")
                .addDatas(col1,colException)
                .render();
        System.out.print(table);

        companyDAO.deleteCompany(company.getId());

        System.out.println();
        System.out.println();

        printTitle("DELETE COMPANY");

        company = new Company("Pepsi","pepsi@gmail.com","12345");
        companyDAO.addCompany(company);
        Coupon coupon = new Coupon(company.getId(),Category.FOOD,"BestCopuon","MyDescription",myDateUtil.currentDate(),myDateUtil.expiredDateFromToday(10),100,100,"image.png");
        Customer customer = new Customer("Amit","Reinich","Amitrei@gmail.com","1234");



        couponsDAO.addCoupon(coupon);
        customersDAO.addCustomer(customer);
        couponsDAO.addCouponPurchase(customer.getId(),coupon.getId());



        col1=companyDAO.getAllCompanies().stream().map(company1 -> company1.getName()).collect(Collectors.toList());

        try {
            adminFacade.deleteCompany(company.getId());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }
        col2=companyDAO.getAllCompanies().stream().map(company1 -> company1.getName()).collect(Collectors.toList());

        table = new MiniTable().addHeaders("Coupon of company:" + couponsDAO.getAllCouponsOfCompany(company.getId()).toString()).render();


        table = new MiniTable("Trying Delete the company with coupons and purchases: " +company.getName() )
                .addHeaders("Before delete", "After delete")
                .addDatas(col1,col2)
                .render();
        System.out.print(table);






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