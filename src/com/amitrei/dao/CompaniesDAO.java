package com.amitrei.dao;

import com.amitrei.beans.Company;
import com.amitrei.exceptions.CompanyAlreadyExistsException;
import com.amitrei.exceptions.CompanyDoesnotExistsException;

import java.util.List;

public interface CompaniesDAO {
    Boolean isCompanyExists(String email,String password);
    void addCompany(Company company) throws CompanyAlreadyExistsException;
    void updateCompany(int companyID,Company company) throws CompanyDoesnotExistsException;
    void deleteCompany(int companyID) throws CompanyDoesnotExistsException;
    List<Company> getAllCompanies();
    Company getOneCompany(int companyID) throws CompanyDoesnotExistsException;
}
