package com.amitrei.exceptions;

public class CouponAlreadyExistsException extends  Exception{

    public CouponAlreadyExistsException(int couponID) {
        super("coupon id" +couponID +" already exists." );
    }

    public CouponAlreadyExistsException(String couponTitle) {
        super("The coupon - " + couponTitle + " already exists.");
    }
}