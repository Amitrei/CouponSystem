package com.amitrei.exceptions.CouponsExceptions;

public class CouponDateExpiredException extends  Exception {


    public CouponDateExpiredException()
    {
        super("Coupon date already expired.");
    }
}
