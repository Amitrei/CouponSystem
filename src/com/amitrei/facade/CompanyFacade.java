package com.amitrei.facade;

import com.amitrei.beans.Category;
import com.amitrei.beans.Company;
import com.amitrei.beans.Coupon;
import com.amitrei.exceptions.AlreadyExistsException;
import com.amitrei.exceptions.DoesNotExistsException;
import com.amitrei.exceptions.IllegalActionException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyFacade extends ClientFacade {

    private int companyID;

    @Override
    public boolean login(String email, String password) throws DoesNotExistsException {

        if (companiesDAO.isCompanyExists(email, password)) {
            this.companyID = companiesDAO.getOneCompany(email).getId();
            return true;
        }

        throw new DoesNotExistsException("incorrect login details, company ");


    }

    public void addCoupon(Coupon coupon) throws AlreadyExistsException {


        if (couponsDAO.isCouponExists(coupon.getTitle(), coupon.getCompanyID()))
            throw new AlreadyExistsException("Coupon");
        couponsDAO.addCoupon(coupon);
        coupon.setId(couponsDAO.getCouponIDFromDB(coupon));

    }

    public void updateCoupon(Coupon coupon) throws IllegalActionException {


        if (couponsDAO.getOneCoupon(coupon.getId()).getCompanyID() != coupon.getCompanyID()) {
            throw new IllegalActionException("Cannot change coupon company ID");
        }

        couponsDAO.updateCoupon(coupon.getId(), coupon);


    }

    public void deleteCoupon(int couponID) throws DoesNotExistsException {
        if (!couponsDAO.isCouponExists(couponID)) throw new DoesNotExistsException("Coupon");
        couponsDAO.deleteCouponPurchase(couponID);
        couponsDAO.deleteCoupon(couponID);
    }

    public List<Coupon> getCompanyCoupons() {
        return couponsDAO.getAllCouponsOfCompany(this.companyID);
    }

    public List<Coupon> getCompanyCoupons(Category category) {

        // Testing stream
        List<Coupon> companyCoupons = getCompanyCoupons();
        var newListFiltered = companyCoupons.stream()
                .filter(coupon -> coupon.getCategory().equals(category))
                .collect(Collectors.toList());


        return newListFiltered;
    }

    public List<Coupon> getCompanyCoupons(double maxPrice) {
        List<Coupon> companyCoupons = getCompanyCoupons();
        List<Coupon> CouponsFilteredByPrice = new ArrayList<>();

        for (Coupon coupon : companyCoupons) {
            if (coupon.getPrice() <= maxPrice) {
                CouponsFilteredByPrice.add(coupon);
            }
        }
        return CouponsFilteredByPrice;
    }

    public int getCompanyID() {
        return this.companyID;
    }


    public Company getCompanyDetails() {
        Company company = companiesDAO.getOneCompany(this.companyID);
        company.setCoupons(couponsDAO.getAllCouponsOfCompany(this.companyID));
        return company;
    }

}
