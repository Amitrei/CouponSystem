package com.amitrei.exceptions.CouponsExceptions;

public class CouponNotFoundException extends  Exception{
    public CouponNotFoundException() {
        super("Coupon not found");
    }
}
