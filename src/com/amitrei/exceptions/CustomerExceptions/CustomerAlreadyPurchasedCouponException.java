package com.amitrei.exceptions.CustomerExceptions;

public class CustomerAlreadyPurchasedCouponException extends Exception {
    public CustomerAlreadyPurchasedCouponException() {
        super("Customer already purchased this coupon");
    }
}
