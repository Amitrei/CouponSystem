package com.amitrei.dao;

import com.amitrei.beans.Company;
import com.amitrei.exceptions.CompanyAlreadyExistsException;
import com.amitrei.exceptions.CompanyDoesNotExistsException;

import java.util.List;

public interface CompaniesDAO {
    Boolean isCompanyExists(String email,String password);
    void addCompany(Company company) throws CompanyAlreadyExistsException;
    void updateCompany(int companyID,Company company) throws CompanyDoesNotExistsException;
    void deleteCompany(int companyID) throws CompanyDoesNotExistsException;
    List<Company> getAllCompanies();
    Company getOneCompany(int companyID) throws CompanyDoesNotExistsException;
}
