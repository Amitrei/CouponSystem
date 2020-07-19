package com.amitrei.dailyjob;

import com.amitrei.beans.Coupon;
import com.amitrei.dao.CouponsDAO;
import com.amitrei.dbdao.CouponsDBDAO;
import com.amitrei.utils.DateUtil;

import java.util.List;

public class CouponExpirationDailyJob implements Runnable {
    CouponsDAO couponsDAO = new CouponsDBDAO();
    DateUtil myDateUtil = new DateUtil();
    private  volatile  Boolean quit = false;

    @Override
    public void run() {


        while (!quit) {

            List<Coupon> allCoupons =couponsDAO.getAllCoupons();
                for (Coupon coupon : allCoupons) {
                    if (myDateUtil.isDatePassed(coupon.getSQLEndDate())) {
                        couponsDAO.deleteCouponPurchase(coupon.getId());
                        couponsDAO.deleteCoupon(coupon.getId());
                    }
                }



            try {
                Thread.sleep(1000*60*60*24);
            } catch (InterruptedException e) {
            }

        }
    }

    public void stop() {
        quit =true;


    }




}
