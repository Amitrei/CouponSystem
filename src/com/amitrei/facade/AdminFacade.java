package com.amitrei.facade;

import com.amitrei.beans.Company;
import com.amitrei.beans.Customer;
import com.amitrei.dbdao.CompaniesDBDAO;
import com.amitrei.dbdao.CustomersDBDAO;
import com.amitrei.exceptions.CompanyExceptions.CompanyAlreadyExistsException;
import com.amitrei.exceptions.CompanyExceptions.CompanyDoesNotExistsException;
import com.amitrei.exceptions.CustomerExceptions.CustomerAlreadyExistsException;

import java.util.List;

public class AdminFacade extends ClientFacade {


    public AdminFacade() {
        companiesDAO = new CompaniesDBDAO();
        customersDAO = new CustomersDBDAO();

    }

    @Override
    public boolean login(String email, String password) {
        return email.equals("admin@admin.com") && password.equals("admin");
    }

    public void addCompany(Company company) throws CompanyAlreadyExistsException {
        companiesDAO.addCompany(company);
    }

    public void updateCompany(Company company) throws CompanyDoesNotExistsException {
        companiesDAO.updateCompany(companiesDAO.getCompanyIDFromDB(company), company);
    }

    public void deleteCompany(int companyID) throws CompanyDoesNotExistsException {
        companiesDAO.deleteCompany(companyID);
    }

    public List<Company> getAllCompanies() {
        return companiesDAO.getAllCompanies();
    }

    public Company getOneCompany(int companyID) throws CompanyDoesNotExistsException {
        return companiesDAO.getOneCompany(companyID);
    }

    public void addCustomer(Customer customer) throws CustomerAlreadyExistsException {
        customersDAO.addCustomer(customer);
    }

    public void updateCustomer(Customer customer) {
        customersDAO.updateCustomer(customersDAO.getCustomerIDFromDB(customer),customer);
    }

    public void deleteCustomer(int customerID) {
        customersDAO.deleteCustomer(customerID);
    }
    public List<Customer> getAllCustomers() {
        return customersDAO.getAllCustomers();
    }

    public Customer getOneCustomer(int customerID) {
        return customersDAO.getOneCustomer(customerID);
    }
}
