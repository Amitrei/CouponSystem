package com.amitrei.dao;

import com.amitrei.beans.Category;
import com.amitrei.beans.Coupon;
import com.amitrei.beans.Customer;

import java.util.List;

public interface CustomersDAO {
    Boolean isCustomerExists(String email);
    Boolean isCustomerExists(String email,String password);
    Boolean isCustomerExists(int customerID);
    void addCustomer(Customer customer);
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
    void deleteCustomerPurchases(int customerID);
    List<Integer> GetCustomerCouponPurchases(int customerID);

}
