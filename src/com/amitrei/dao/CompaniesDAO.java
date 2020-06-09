package com.amitrei.dao;

import com.amitrei.beans.Company;

import java.util.List;

public interface CompaniesDAO {
    void isCompanyExists(String email,String password);
    void addCompany(Company company);
    void updateCompany(Company company);
    void updateCompany(int companyID);
    void deleteCompany(int companyID);
    List<Company> getAllCompanies();
    Company getOneCompany(int companyID);
}
