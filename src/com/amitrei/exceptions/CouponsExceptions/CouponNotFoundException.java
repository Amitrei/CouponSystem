package com.amitrei.exceptions.CouponsExceptions;

public class CouponNotFoundException extends  Exception{
    public CouponNotFoundException(int couponID) {
        super("Coupon id: " + couponID + " not found.");
    }
}
