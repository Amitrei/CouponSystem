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

        throw new DoesNotExistsException("Company");


    }

    public void addCoupon(Coupon coupon) throws AlreadyExistsException {


        if (couponsDAO.isCouponExists(coupon.getTitle(), coupon.getCompanyID()))
            throw new AlreadyExistsException("Coupon");
        couponsDAO.addCoupon(coupon);
        coupon.setId(couponsDAO.getCouponIDFromDB(coupon));

    }

    public void updateCoupon(Coupon coupon) throws DoesNotExistsException {
        if (!couponsDAO.isCouponExists(coupon.getTitle(), coupon.getCompanyID()))
            throw new DoesNotExistsException("Coupon");

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


    public Company getCompanyDetails() throws DoesNotExistsException {
        Company company = companiesDAO.getOneCompany(this.companyID);
        company.setCoupons(couponsDAO.getAllCouponsOfCompany(this.companyID));
        return company;
    }

}
