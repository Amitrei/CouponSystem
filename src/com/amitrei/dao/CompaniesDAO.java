package com.amitrei.dao;

import com.amitrei.beans.Company;
import com.amitrei.exceptions.CompanyExceptions.CompanyAlreadyExistsException;
import com.amitrei.exceptions.CompanyExceptions.CompanyDoesNotExistsException;

import java.util.List;

public interface CompaniesDAO {
    Boolean isCompanyExistsByEmail(String email);
    Boolean isCompanyExistsByName(String name);
    Boolean isCompanyExistsById(int companyID);
    Boolean isCompanyExists(String email, String password);

    void addCompany(Company company) throws CompanyAlreadyExistsException;
    void updateCompany(Company company) throws CompanyDoesNotExistsException;
    void deleteCompany(int companyID) throws CompanyDoesNotExistsException;
    List<Company> getAllCompanies();
    Company getOneCompany(int companyID) throws CompanyDoesNotExistsException;
    Company getOneCompany(String companyEmail) throws CompanyDoesNotExistsException;

    int getCompanyIDFromDB(Company company);
}
