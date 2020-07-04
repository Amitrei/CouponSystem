package com.amitrei.facade;

import com.amitrei.beans.Company;
import com.amitrei.beans.Coupon;
import com.amitrei.beans.Customer;
import com.amitrei.dbdao.CompaniesDBDAO;
import com.amitrei.dbdao.CouponsDBDAO;
import com.amitrei.dbdao.CustomersDBDAO;
import com.amitrei.exceptions.CompanyExceptions.CannotChangeCompanyNameException;
import com.amitrei.exceptions.CompanyExceptions.CompanyAlreadyExistsException;
import com.amitrei.exceptions.CompanyExceptions.CompanyDoesNotExistsException;
import com.amitrei.exceptions.CustomerExceptions.CustomerAlreadyExistsException;
import com.amitrei.exceptions.CustomerExceptions.CustomerDoesNotExists;

import java.util.List;

public class AdminFacade extends ClientFacade {

    private final String ADMIN_EMAIL = "admin@admin.com";
    private final String ADMIN_PASSWORD = "admin";


    @Override
    public boolean login(String email, String password) {
        return email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD);
    }


    public void addCompany(Company company) throws CompanyAlreadyExistsException {

        List<Company> allCompanies = companiesDAO.getAllCompanies();
        for (Company comp : allCompanies) {

            if (comp.getName().equals(company.getName()) || comp.getEmail().equals(company.getEmail())) {
                throw new CompanyAlreadyExistsException();

            }

        }

        companiesDAO.addCompany(company);
        company.setId(companiesDAO.getCompanyIDFromDB(company));

    }

    public void updateCompany(Company company) throws CompanyDoesNotExistsException, CannotChangeCompanyNameException {

        // Cannot change the company name
        if (!getOneCompany(company.getId()).getName().equals(company.getName()))
            throw new CannotChangeCompanyNameException();

        else if (!companiesDAO.isCompanyExistsById(company.getId()))
            throw new CompanyDoesNotExistsException();

        companiesDAO.updateCompany(company);
    }

    public void deleteCompany(int companyID) throws CompanyDoesNotExistsException {

        List<Coupon> allCouponsOfCompany=couponsDAO.getAllCouponsOfCompany(companyID);

        if (!companiesDAO.isCompanyExistsById(companyID)) throw new CompanyDoesNotExistsException();
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

    public Company getOneCompany(int companyID) throws CompanyDoesNotExistsException {
        return companiesDAO.getOneCompany(companyID);
    }

    public void addCustomer(Customer customer) throws CustomerAlreadyExistsException {
        if (customersDAO.isCustomerExists(customer.getEmail())) throw new CustomerAlreadyExistsException();
        customersDAO.addCustomer(customer);
        customer.setId(customersDAO.getCustomerIDFromDB(customer));
    }


    public void updateCustomer(Customer customer) throws CustomerDoesNotExists, CustomerAlreadyExistsException {

        if (!customersDAO.isCustomerExists(customer.getId())) throw new CustomerDoesNotExists();
        if (customersDAO.isCustomerExists(customer.getEmail())) throw new CustomerAlreadyExistsException();
        customersDAO.updateCustomer(customer.getId(), customer);
    }

    public void deleteCustomer(int customerID) throws CustomerDoesNotExists {
        if (!customersDAO.isCustomerExists(customerID)) throw new CustomerDoesNotExists();
        customersDAO.deleteCustomerPurchaseHistory(customerID);
        customersDAO.deleteCustomer(customerID);
    }

    public List<Customer> getAllCustomers() {
        return customersDAO.getAllCustomers();
    }

    public Customer getOneCustomer(int customerID) throws CustomerDoesNotExists {
        if (!customersDAO.isCustomerExists(customerID)) throw new CustomerDoesNotExists();
        return customersDAO.getOneCustomer(customerID);
    }
}
