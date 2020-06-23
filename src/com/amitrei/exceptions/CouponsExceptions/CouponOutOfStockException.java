package com.amitrei.exceptions.CouponsExceptions;

public class CouponOutOfStockException extends Exception {
    public CouponOutOfStockException() {
        super("Coupon is out of stock");
    }
}
