package com.amitrei.facade;

import com.amitrei.beans.Company;
import com.amitrei.beans.Coupon;
import com.amitrei.beans.Customer;
import com.amitrei.dbdao.CompaniesDBDAO;
import com.amitrei.dbdao.CouponsDBDAO;
import com.amitrei.dbdao.CustomersDBDAO;
import com.amitrei.exceptions.CompanyExceptions.CompanyAlreadyExistsException;
import com.amitrei.exceptions.CompanyExceptions.CompanyDoesNotExistsException;
import com.amitrei.exceptions.CustomerExceptions.CustomerAlreadyExistsException;
import com.amitrei.exceptions.CustomerExceptions.CustomerDoesNotExists;

import java.util.List;

public class AdminFacade extends ClientFacade {

    private final String ADMIN_EMAIL = "admin@admin.com";
    private final String ADMIN_PASSWORD = "admin";

    public AdminFacade() {
        super();


    }

    @Override
    public boolean login(String email, String password) {
        return email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD);
    }

    public void addCompany(Company company) throws CompanyAlreadyExistsException {
        if (companiesDAO.isCompanyExistsByName(company.getName()) && companiesDAO.isCompanyExistsByEmail(company.getEmail()))
            throw new CompanyAlreadyExistsException();
        companiesDAO.addCompany(company);
    }

    public void updateCompany(Company company) throws CompanyDoesNotExistsException {
        if (!companiesDAO.isCompanyExistsByName(company.getName())) throw new CompanyDoesNotExistsException();
        companiesDAO.updateCompany(company);
    }

    public void deleteCompany(int companyID) throws CompanyDoesNotExistsException {
        if (!companiesDAO.isCompanyExistsById(companyID)) throw new CompanyDoesNotExistsException();
        couponsDAO.deleteCouponsPurchasesOfCompany(companyID);
        for (Coupon companyCoupon : couponsDAO.getAllCouponsOfCompany(companyID)) {
            couponsDAO.deleteCoupon(companyCoupon.getId());
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
    }

    public void updateCustomer(Customer customer) throws CustomerDoesNotExists {
        if (!customersDAO.isCustomerExists(customer.getEmail())) throw new CustomerDoesNotExists();
        customersDAO.updateCustomer(customersDAO.getCustomerIDFromDB(customer), customer);
    }

    public void deleteCustomer(int customerID) throws CustomerAlreadyExistsException {
        if (customersDAO.isCustomerExists(customerID)) throw new CustomerAlreadyExistsException();
        customersDAO.deleteCustomer(customerID);
    }

    public List<Customer> getAllCustomers() {
        return customersDAO.getAllCustomers();
    }

    public Customer getOneCustomer(int customerID) throws CustomerAlreadyExistsException {
        if (customersDAO.isCustomerExists(customerID)) throw new CustomerAlreadyExistsException();
        return customersDAO.getOneCustomer(customerID);
    }
}
