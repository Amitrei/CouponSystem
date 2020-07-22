package com.amitrei.test;

import com.amitrei.beans.Category;
import com.amitrei.beans.Company;
import com.amitrei.beans.Coupon;
import com.amitrei.beans.Customer;
import com.amitrei.dao.CompaniesDAO;
import com.amitrei.dao.CouponsDAO;
import com.amitrei.dao.CustomersDAO;
import com.amitrei.dbdao.CompaniesDBDAO;
import com.amitrei.dbdao.CouponsDBDAO;
import com.amitrei.dbdao.CustomersDBDAO;
import com.amitrei.exceptions.AlreadyExistsException;
import com.amitrei.exceptions.DoesNotExistsException;
import com.amitrei.exceptions.IllegalActionException;
import com.amitrei.facade.AdminFacade;
import com.amitrei.facade.CompanyFacade;
import com.amitrei.facade.CustomerFacade;
import com.amitrei.security.ClientType;
import com.amitrei.security.LoginManager;
import com.amitrei.utils.DateUtil;

public class InterviewTest {

    CompaniesDAO companiesDAO = new CompaniesDBDAO();
    CouponsDAO couponsDAO = new CouponsDBDAO();
    CustomersDAO customersDAO = new CustomersDBDAO();

    public void Test() {
        DateUtil dateUtil = new DateUtil();

        // Admin Login
        LoginManager loginManager = LoginManager.getInstance();
        AdminFacade adminFacade = null;
        try {
            adminFacade = (AdminFacade) loginManager.login("admin@admin.com", "admin", ClientType.Administrator);
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }


        // Adding Company
        Company company = new Company("McDolands", "McDonalds@gmail.com", "1234");
        try {
            adminFacade.addCompany(company);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        //Company login
        CompanyFacade companyFacade = null;
        try {
            companyFacade = (CompanyFacade) loginManager.login("McDonalds@gmail.com", "1234", ClientType.Company);
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }


        //Adding Customer
        Customer customer = new Customer("Amit", "Reinich", "Amitreinich@gmail.com", "1234");
        try {
            adminFacade.addCustomer(customer);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }


        //Customer login
        CustomerFacade customerFacade = null;
        try {
            customerFacade = (CustomerFacade) loginManager.login("amitreinich@gmail.com", "1234", ClientType.Customer);
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }


        // Adding coupons
        Coupon coupon = new Coupon(company.getId(), Category.Resturant, "Best burgers", "Massive discount over the burgers!", dateUtil.currentDate(), dateUtil.expiredDateFromToday(10), 100, 100, "logo.jpeg");
        try {
            companyFacade.addCoupon(coupon);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        Coupon coupon2 = new Coupon(company.getId(), Category.Resturant, "Best Nuggets", "Tastiest nuggets on earth", dateUtil.currentDate(), dateUtil.expiredDateFromToday(10), 100, 100, "logo.jpeg");
        try {
            companyFacade.addCoupon(coupon2);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }


        //Purchase 2 coupon
        try {
            customerFacade.purchaseCoupon(coupon);
            customerFacade.purchaseCoupon(coupon2);
        } catch (IllegalActionException | DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }




        coupon2.setTitle("Best burgers2");
        try {
            companyFacade.updateCoupon(coupon2);
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }

//        // Printings
//        System.out.print(couponsDAO.getOneCoupon(coupon.getId()));
//        System.out.print(couponsDAO.getOneCoupon(coupon2.getId()));
//        System.out.print(companyFacade.getCompanyDetails());
//        System.out.print(customerFacade.getCustomerDetails());
//




        //Delete Company
        try {
            adminFacade.deleteCompany(company.getId());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }


//        // Delete Coupons
//        try {
//            companyFacade.deleteCoupon(coupon.getId());
//            companyFacade.deleteCoupon(coupon2.getId());
//        } catch (DoesNotExistsException e) {
//            System.out.println(e.getMessage());
//        }

//
        // Delete Customer
        try {
            adminFacade.deleteCustomer(customer.getId());
        } catch (DoesNotExistsException e) {
            System.out.println(e.getMessage());
        }






    }
}
