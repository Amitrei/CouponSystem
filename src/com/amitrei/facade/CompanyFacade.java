package com.amitrei.facade;

import com.amitrei.beans.Category;
import com.amitrei.beans.Company;
import com.amitrei.beans.Coupon;
import com.amitrei.exceptions.CompanyExceptions.CompanyDoesNotExistsException;
import com.amitrei.exceptions.CouponsExceptions.CouponAlreadyExistsException;
import com.amitrei.exceptions.CouponsExceptions.CouponNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyFacade extends ClientFacade {

    private int companyID;

    @Override
    public boolean login(String email, String password) throws CompanyDoesNotExistsException {

        if (companiesDAO.isCompanyExists(email, password)) {
            this.companyID = companiesDAO.getOneCompany(email).getId();
            return true;
        }

        return false;

    }

    public void addCoupon(Coupon coupon) throws CouponAlreadyExistsException {
        if (couponsDAO.isCouponExists(coupon.getTitle(), coupon.getCompanyID()))
            throw new CouponAlreadyExistsException(coupon.getTitle());
        couponsDAO.addCoupon(coupon);
        coupon.setId(couponsDAO.getCouponIDFromDB(coupon));
    }

    public void updateCoupon(Coupon coupon) throws CouponNotFoundException {
        coupon.setId(couponsDAO.getCouponIDFromDB(coupon));
        if (!couponsDAO.isCouponExists(coupon.getTitle(), coupon.getCompanyID()))
            throw new CouponNotFoundException();

        couponsDAO.updateCoupon(coupon.getId(), coupon);


    }

    public void deleteCoupon(int couponID) throws CouponNotFoundException {
        if (!couponsDAO.isCouponExists(couponID)) throw new CouponNotFoundException();
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
        List<Coupon> companyCouponsFilteredByPrice = new ArrayList<>();

        for (Coupon coupon : companyCoupons) {
            if (coupon.getPrice() <= maxPrice) {
                companyCouponsFilteredByPrice.add(coupon);
            }
        }
        return companyCouponsFilteredByPrice;
    }

    public int getCompanyID() {
        return this.companyID;
    }


    public Company getCompanyDetails() throws CompanyDoesNotExistsException {
        Company company = companiesDAO.getOneCompany(this.companyID);
        company.setCoupons(couponsDAO.getAllCouponsOfCompany(this.companyID));
        company.setId(this.companyID);
        return company;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }
}
