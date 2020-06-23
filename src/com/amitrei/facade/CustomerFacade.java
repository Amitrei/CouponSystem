package com.amitrei.facade;

import com.amitrei.beans.Coupon;
import com.amitrei.exceptions.CompanyExceptions.CompanyDoesNotExistsException;
import com.amitrei.exceptions.CouponsExceptions.CouponDateExpiredException;
import com.amitrei.exceptions.CouponsExceptions.CouponNotFoundException;
import com.amitrei.exceptions.CouponsExceptions.CouponOutOfStockException;
import com.amitrei.exceptions.CustomerExceptions.CustomerAlreadyPurchasedCouponException;
import com.amitrei.exceptions.CustomerExceptions.CustomerDoesNotExists;
import com.amitrei.utils.MyDateUtil;

public class CustomerFacade extends ClientFacade {
    private int customerID;
    MyDateUtil myDateUtil = new MyDateUtil();

    @Override
    public boolean login(String email, String password) throws CompanyDoesNotExistsException, CustomerDoesNotExists {
        if (customersDAO.isCustomerExists(email, password)) {
            this.customerID = customersDAO.getOneCustomer(email).getId();
            return true;
        } else {
            throw new CustomerDoesNotExists();
        }
    }

    public void purchaseCoupon(Coupon coupon) throws CouponDateExpiredException, CouponNotFoundException, CustomerAlreadyPurchasedCouponException, CouponOutOfStockException {

        if (customersDAO.isCustomerHaveCoupon(this.customerID, coupon.getId()))
            throw new CustomerAlreadyPurchasedCouponException();
        else if (coupon.getAmount() <= 0) throw new CouponOutOfStockException();
        else if (myDateUtil.isDatePassed(coupon.getEndDate())) throw new CouponDateExpiredException();


        else {
            couponsDAO.addCouponPurchase(this.customerID, coupon.getId());
            coupon.setAmount(coupon.getAmount() - 1);
            couponsDAO.updateCoupon(coupon.getId(), coupon);
        }
    }


}
