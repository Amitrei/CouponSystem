package com.amitrei.dao;

import com.amitrei.beans.Coupon;
import com.amitrei.exceptions.CouponsExceptions.CouponDateExpiredException;
import com.amitrei.exceptions.CouponsExceptions.CouponNotFoundException;

import java.util.List;

public interface CouponsDAO {
    void addCoupon(Coupon... coupon);
    void updateCoupon(int couponID,Coupon coupon) throws CouponNotFoundException;
    void deleteCoupon (int... couponID);
    List<Coupon> getAllCoupons();
    Coupon getOneCoupon(int couponID) throws CouponNotFoundException;
    void addCouponPurchase(int customerID,int couponID) throws CouponNotFoundException, CouponDateExpiredException;
    void deleteCouponPurchase(int customerID,int couponID);
    boolean isCouponExists(int couponID);
    boolean isCouponExists(String couponTitle, int companyID);
}
