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
    public boolean login(String email, String password) throws DoesNotExistsException {

        if (!email.equals(ADMIN_EMAIL) || !password.equals(ADMIN_PASSWORD))
            throw new DoesNotExistsException("Inccorect login details");


        return true;
    }


    public void addCompany(Company company) throws AlreadyExistsException {

        List<Company> allCompanies = companiesDAO.getAllCompanies();
        for (Company comp : allCompanies) {

            if (comp.getName().equals(company.getName()) || comp.getEmail().equals(company.getEmail())) {
                throw new AlreadyExistsException("company name or email");

            }

        }

        companiesDAO.addCompany(company);

        company.setId(companiesDAO.getCompanyIDFromDB(company));


    }

    public void updateCompany(Company company) throws IllegalActionException, DoesNotExistsException {

        if (!getOneCompany(company.getId()).getName().equals(company.getName()))
            throw new IllegalActionException("cannot update company name");

        if (!companiesDAO.isCompanyExistsById(company.getId()))
            throw new DoesNotExistsException("company");

        if (companiesDAO.isCompanyExistsByEmail(company.getEmail()) && companiesDAO.getOneCompany(company.getEmail()).getId() != company.getId())
            throw new IllegalActionException("cannot update to an already exists email address.");


        companiesDAO.updateCompany(company);
    }

    public void deleteCompany(int companyID) throws DoesNotExistsException {

        List<Coupon> allCouponsOfCompany = couponsDAO.getAllCouponsOfCompany(companyID);

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

        customer.setId(customersDAO.getCustomerIDFromDB(customer));

    }


    public void updateCustomer(Customer customer) throws DoesNotExistsException, AlreadyExistsException {

        if (!customersDAO.isCustomerExists(customer.getId())) throw new DoesNotExistsException("Customer");

        if (customersDAO.isCustomerExists(customer.getEmail()) && customersDAO.getOneCustomer(customer.getEmail()).getId() != customer.getId())
            throw new AlreadyExistsException("Customer");


        customersDAO.updateCustomer(customer.getId(), customer);
    }

    public void deleteCustomer(int customerID) throws DoesNotExistsException {
        if (!customersDAO.isCustomerExists(customerID)) throw new DoesNotExistsException("Customer");
        customersDAO.deleteCustomerPurchases(customerID);
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
