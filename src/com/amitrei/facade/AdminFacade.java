package com.amitrei.facade;

import com.amitrei.beans.Company;
import com.amitrei.beans.Coupon;
import com.amitrei.beans.Customer;
import com.amitrei.exceptions.AlreadyExistsException;
import com.amitrei.exceptions.DoesNotExistsException;
import com.amitrei.exceptions.IllegalActionException;

import java.util.List;

public class AdminFacade extends ClientFacade {

    private final String ADMIN_EMAIL = "admin@admin.com";
    private final String ADMIN_PASSWORD = "admin";


    @Override
    public boolean login(String email, String password) {
        return email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD);
    }


    public void addCompany(Company company) throws AlreadyExistsException {

        List<Company> allCompanies = companiesDAO.getAllCompanies();
        for (Company comp : allCompanies) {

            if (comp.getName().equals(company.getName()) || comp.getEmail().equals(company.getEmail())) {
                throw new AlreadyExistsException("company name");

            }

        }

        companiesDAO.addCompany(company);
        company.setId(companiesDAO.getCompanyIDFromDB(company));

    }

    public void updateCompany(Company company) throws IllegalActionException, DoesNotExistsException {

        // Cannot change the company name
        if (!getOneCompany(company.getId()).getName().equals(company.getName()))
            throw new IllegalActionException("cannot update company name");

        else if (!companiesDAO.isCompanyExistsById(company.getId()))
            throw new DoesNotExistsException("company");

        companiesDAO.updateCompany(company);
    }

    public void deleteCompany(int companyID) throws DoesNotExistsException {

        List<Coupon> allCouponsOfCompany=couponsDAO.getAllCouponsOfCompany(companyID);

        if (!companiesDAO.isCompanyExistsById(companyID)) throw new DoesNotExistsException("company");
        if (allCouponsOfCompany.size() > 0) {
            couponsDAO.deleteCouponsPurchasesOfCompany(companyID);
            for (Coupon companyCoupon : allCouponsOfCompany) {
                couponsDAO.deleteCoupon(companyCoupon.getId());
            }
        }
        companiesDAO.deleteCompany(companyID);

    }

    public List<Company> getAllCompanies() {
        return companiesDAO.getAllCompanies();
    }

    public Company getOneCompany(int companyID) {
        return companiesDAO.getOneCompany(companyID);
    }

    public void addCustomer(Customer customer) throws AlreadyExistsException {
        if (customersDAO.isCustomerExists(customer.getEmail())) throw new AlreadyExistsException("Customer");
        customersDAO.addCustomer(customer);
        try {
            customer.setId(customersDAO.getCustomerIDFromDB(customer));
        } catch (IllegalActionException e) {
            System.out.println(e.getMessage());
        }
    }


    public void updateCustomer(Customer customer) throws DoesNotExistsException, AlreadyExistsException {

        if (!customersDAO.isCustomerExists(customer.getId())) throw new DoesNotExistsException("Customer");
        if (customersDAO.isCustomerExists(customer.getEmail())) throw new AlreadyExistsException("Customer");
        customersDAO.updateCustomer(customer.getId(), customer);
    }

    public void deleteCustomer(int customerID) throws DoesNotExistsException {
        if (!customersDAO.isCustomerExists(customerID)) throw new DoesNotExistsException("Customer");
        customersDAO.deleteCustomerPurchaseHistory(customerID);
        customersDAO.deleteCustomer(customerID);
    }

    public List<Customer> getAllCustomers() {
        return customersDAO.getAllCustomers();
    }

    public Customer getOneCustomer(int customerID) throws DoesNotExistsException {
        if (!customersDAO.isCustomerExists(customerID)) throw new DoesNotExistsException("Customer");
        return customersDAO.getOneCustomer(customerID);
    }
}
