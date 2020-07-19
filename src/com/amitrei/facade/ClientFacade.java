package com.amitrei.facade;

import com.amitrei.dao.CompaniesDAO;
import com.amitrei.dao.CouponsDAO;
import com.amitrei.dao.CustomersDAO;
import com.amitrei.dbdao.CompaniesDBDAO;
import com.amitrei.dbdao.CouponsDBDAO;
import com.amitrei.dbdao.CustomersDBDAO;
import com.amitrei.exceptions.DoesNotExistsException;


public abstract class ClientFacade {
    protected CompaniesDAO companiesDAO;
    protected CouponsDAO couponsDAO;
    protected CustomersDAO customersDAO;
    public ClientFacade() {
        companiesDAO = new CompaniesDBDAO();
        couponsDAO = new CouponsDBDAO();
        customersDAO = new CustomersDBDAO();
    }

    public abstract boolean login(String email, String password) throws DoesNotExistsException;


}
