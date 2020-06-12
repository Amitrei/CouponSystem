package com.amitrei.facade;

import com.amitrei.dbdao.CompaniesDBDAO;
import com.amitrei.dbdao.CustomersDBDAO;

public class AdminFacade extends ClientFacade {


    public AdminFacade() {
        companiesDAO = new CompaniesDBDAO();
        customersDAO = new CustomersDBDAO();

    }

    @Override
    public boolean login(String email, String password) {
        return email.equals("admin@admin.com") && password.equals("admin");
    }
}
