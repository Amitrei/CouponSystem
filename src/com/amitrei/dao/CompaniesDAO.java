package com.amitrei.dao;
import com.amitrei.beans.Company;
import java.util.List;

public interface CompaniesDAO {

    Boolean isCompanyExistsById(int companyID);
    Boolean isCompanyExistsByEmail(String companyEmail);
    Boolean isCompanyExists(String email, String password);
    void addCompany(Company company);
    void updateCompany(Company company);
    void deleteCompany(int companyID);
    List<Company> getAllCompanies();
    Company getOneCompany(int companyID);
    Company getOneCompany(String companyEmail);

    int getCompanyIDFromDB(Company company);
}
