package com.amitrei.dailyjob;

import com.amitrei.beans.Coupon;
import com.amitrei.dao.CouponsDAO;
import com.amitrei.dbdao.CouponsDBDAO;
import com.amitrei.utils.MyDateUtil;

import java.util.ArrayList;
import java.util.List;

public class CouponExpirationDailyJob implements Runnable {
    CouponsDAO couponsDAO = new CouponsDBDAO();
    MyDateUtil myDateUtil = new MyDateUtil();
    private Boolean quit = false;

    @Override
    public void run() {

        while (!quit) {
            List<Coupon> allCoupons = new ArrayList();
            allCoupons = couponsDAO.getAllCoupons();

            for (Coupon coupon : allCoupons) {
                if (myDateUtil.isDatePassed(coupon.getEndDate())) {
                    couponsDAO.deleteCouponPurchase(coupon.getId());
                    couponsDAO.deleteCoupon(coupon.getId());
                }
            }


        }
    }

    public void stopIt() {
        this.quit = true;

    }

}
