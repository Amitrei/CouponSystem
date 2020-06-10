package com.amitrei.dao;

import com.amitrei.beans.Coupon;

import java.util.List;

public interface CouponsDAO {
    void addCoupon(Coupon coupon);
    void updateCoupon(Coupon coupon);
    void deleteCoupon (int couponID);
    List<Coupon> getAllCoupons();
    Coupon getOneCoupon();
    void addCouponPurchase(int customerID,int couponID);
    void deleteCouponPurchase(int customerID,int couponID);
}
