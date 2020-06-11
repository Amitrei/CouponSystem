package com.amitrei.exceptions;

public class CouponAlreadyExistsException extends  Exception{

    public CouponAlreadyExistsException(int couponID) {
        super("coupon id" +couponID +" already exists." );
    }
}
