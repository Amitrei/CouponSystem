package com.amitrei.dao;

import com.amitrei.beans.Customer;
import com.amitrei.exceptions.CustomerAlreadyExistsException;

import java.util.List;

public interface CustomersDAO {
    Boolean isCustomerExists(String email,String password);
    void addCustomer(Customer... customer) throws CustomerAlreadyExistsException;
    void updateCustomer(int customerID,Customer customer);
    void deleteCustomer(int... customerID);
    List<Customer> getAllCustomers();
    Customer getOneCustomer(int customerID);

}
