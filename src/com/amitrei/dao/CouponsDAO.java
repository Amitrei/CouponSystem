package com.amitrei.dao;

import com.amitrei.beans.Coupon;


import java.util.List;

public interface CouponsDAO {
    void addCoupon(Coupon coupon);

    void updateCoupon(int couponID, Coupon coupon);

    void deleteCoupon(int... couponID);

    List<Coupon> getAllCoupons();

    Coupon getOneCoupon(int couponID);

    void addCouponPurchase(int customerID, int couponID);

    void deleteCouponPurchase(int customerID, int couponID);

    boolean isCouponExists(int couponID);


    boolean isCouponExists(String couponTitle, int companyID);

    List<Coupon> getAllCouponsOfCompany(int companyID);

    void deleteCouponsPurchasesOfCompany(int couponCompanyID);

    int getCouponIDFromDB(Coupon coupon);

    void deleteCouponPurchase(int couponID);
}
