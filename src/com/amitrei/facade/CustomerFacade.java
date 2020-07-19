package com.amitrei.facade;

import com.amitrei.beans.Category;
import com.amitrei.beans.Coupon;
import com.amitrei.beans.Customer;

import com.amitrei.exceptions.DoesNotExistsException;
import com.amitrei.exceptions.IllegalActionException;
import com.amitrei.utils.DateUtil;

import java.util.List;

public class CustomerFacade extends ClientFacade {
    private int customerID;

    DateUtil myDateUtil = new DateUtil();

    @Override
    public boolean login(String email, String password) throws DoesNotExistsException {
        if (customersDAO.isCustomerExists(email, password)) {
            this.customerID = customersDAO.getOneCustomer(email).getId();

            return true;
        }

        else {
            throw new DoesNotExistsException("incorrect login details, customer ");
        }
    }

    public void purchaseCoupon(Coupon coupon) throws DoesNotExistsException, IllegalActionException {
        if (!couponsDAO.isCouponExists(coupon.getId())) throw new DoesNotExistsException("Coupon");

        else if (customersDAO.isCustomerAlreadyHaveCoupon(this.customerID, coupon.getId()))
            throw new IllegalActionException("Customer already purchased this coupon");

        else if (coupon.getAmount() <= 0) throw new IllegalActionException("Coupon out of stock");


        else if (myDateUtil.isDatePassed(coupon.getSQLEndDate())) throw new IllegalActionException("Coupon already expired");


        else {

            couponsDAO.addCouponPurchase(this.customerID, coupon.getId());
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

    public int getCustomerID() {
        return customerID;
    }

    public Customer getCustomerDetails() {
        Customer myCustomer = customersDAO.getOneCustomer(this.customerID);

        for (Coupon coupon : getCustomerCoupons()) {
            myCustomer.getCoupons().add(coupon);
        }
        return myCustomer;
    }
}
