package com.amitrei.dao;

import com.amitrei.beans.Category;
import com.amitrei.beans.Coupon;
import com.amitrei.beans.Customer;
import com.amitrei.exceptions.CustomerExceptions.CustomerAlreadyExistsException;

import java.util.List;

public interface CustomersDAO {
    Boolean isCustomerExists(String email);
    Boolean isCustomerExists(String email,String password);
    Boolean isCustomerExists(int customerID);
    void addCustomer(Customer customer) throws CustomerAlreadyExistsException;
    void updateCustomer(int customerID,Customer customer);
    void deleteCustomer(int customerID);
    List<Customer> getAllCustomers();
    Customer getOneCustomer(int customerID);
    Customer getOneCustomer(String email);
    List<Coupon> getCustomerCoupons(int CustomerID);
    List<Coupon> getCustomerCoupons(int customerID,Category category);
    List<Coupon> getCustomerCoupons(int customerID,double maxPrice);
    boolean isCustomerAlreadyHaveCoupon(int customerID, int couponID);
    int getCustomerIDFromDB(Customer customer);
    List<Integer> GetCustomerCouponPurchases(int customerID);

}
