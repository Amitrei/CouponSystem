package com.amitrei.facade;

import com.amitrei.beans.Category;
import com.amitrei.beans.Coupon;
import com.amitrei.beans.Customer;
import com.amitrei.exceptions.CompanyExceptions.CompanyDoesNotExistsException;
import com.amitrei.exceptions.CouponsExceptions.CouponDateExpiredException;
import com.amitrei.exceptions.CouponsExceptions.CouponNotFoundException;
import com.amitrei.exceptions.CouponsExceptions.CouponOutOfStockException;
import com.amitrei.exceptions.CustomerExceptions.CustomerAlreadyPurchasedCouponException;
import com.amitrei.exceptions.CustomerExceptions.CustomerDoesNotExists;
import com.amitrei.utils.MyDateUtil;

import java.util.List;

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
        if (!couponsDAO.isCouponExists(coupon.getId())) throw new CouponNotFoundException();
        else if (customersDAO.isCustomerAlreadyHaveCoupon(this.customerID, coupon.getId()))
            throw new CustomerAlreadyPurchasedCouponException();
        else if (coupon.getAmount() <= 0) throw new CouponOutOfStockException();
        else if (myDateUtil.isDatePassed(coupon.getEndDate())) throw new CouponDateExpiredException();


        else {
            couponsDAO.addCouponPurchase(this.customerID, coupon.getId());
            customersDAO.getOneCustomer(this.customerID).getCoupons().add(coupon);
            coupon.setAmount(coupon.getAmount() - 1);
            couponsDAO.updateCoupon(coupon.getId(), coupon);
        }
    }


    public List<Coupon> getCustomerCoupons() {
        return customersDAO.getCustomerCoupons(this.customerID);

    }

    public List<Coupon> getCustomerCoupons(Category category) {
        return customersDAO.getCustomerCoupons(this.customerID, category);

    }

    public List<Coupon> getCustomerCoupons(double maxPrice) {
        return customersDAO.getCustomerCoupons(this.customerID, maxPrice);

    }

    public Customer getCustomerDetails() {
        return customersDAO.getOneCustomer(this.customerID);
    }
}
