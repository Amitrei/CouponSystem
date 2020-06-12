package com.amitrei.exceptions.CouponsExceptions;

public class CouponDateExpiredException extends  Exception {
    public CouponDateExpiredException(int couponID) {
        super("coupon id: " + couponID + " date expired.");
    }
}
