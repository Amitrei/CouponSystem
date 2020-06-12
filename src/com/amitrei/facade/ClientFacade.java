package com.amitrei.facade;

import com.amitrei.dao.CompaniesDAO;
import com.amitrei.dao.CouponsDAO;
import com.amitrei.dao.CustomersDAO;
import com.amitrei.dbdao.CompaniesDBDAO;
import com.amitrei.dbdao.CouponsDBDAO;
import com.amitrei.dbdao.CustomersDBDAO;

public abstract class ClientFacade {
    protected CompaniesDAO companiesDAO;
    protected CouponsDAO couponsDAO;
    protected CustomersDAO customersDAO;

    public abstract boolean login(String email, String password);
}
