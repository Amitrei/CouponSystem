package com.amitrei.exceptions;

public class CouponDateExpiredException extends  Exception {
    public CouponDateExpiredException(int couponID) {
        super("coupon id: " + couponID + " date expired.");
    }
}
