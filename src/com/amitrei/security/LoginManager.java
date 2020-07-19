package com.amitrei.security;

import com.amitrei.exceptions.DoesNotExistsException;
import com.amitrei.facade.AdminFacade;
import com.amitrei.facade.ClientFacade;
import com.amitrei.facade.CompanyFacade;
import com.amitrei.facade.CustomerFacade;

public class LoginManager {

    public static LoginManager instance = null;


    private LoginManager() {

    }


    public ClientFacade login(String email, String password, ClientType clientType) throws DoesNotExistsException {

        switch (clientType) {

            case Administrator:
                AdminFacade admin = new AdminFacade();
                if (admin.login(email, password)) return admin;
                else {
                    return null;
                }

            case Company:
                CompanyFacade company = new CompanyFacade();
                if (company.login(email, password)) return company;
                else {
                    return null;
                }

            case Customer:
                CustomerFacade customer = new CustomerFacade();
                if (customer.login(email, password)) return customer;
                else {
                    return null;
                }

            default:
                return null;
        }
    }


    public static LoginManager getInstance() {
        if (instance == null) {

            synchronized (LoginManager.class) {
                if (instance == null) {
                    instance = new LoginManager();
                }
            }
        }
        return instance;
    }

}
