package com.amitrei.dbdao;

import com.amitrei.beans.Category;
import com.amitrei.beans.Coupon;
import com.amitrei.dao.CouponsDAO;
import com.amitrei.db.ConnectionPool;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CouponsDBDAO implements CouponsDAO {
    private Connection connection = null;


    @Override
    public void addCoupon(Coupon coupon) {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "INSERT INTO `couponsystem`.`coupons` (`COMPANY_ID`, `CATEGORY_ID`,`TITLE`,`DESCRIPTION`,`START_DATE`,`END_DATE`,`AMOUNT`,`PRICE`,`IMAGE`) VALUES (?,?,?,?,?,?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, coupon.getCompanyID());
            preparedStatement.setInt(2, coupon.getCategory().ordinal()+1);
            preparedStatement.setString(3, coupon.getTitle());
            preparedStatement.setString(4, coupon.getDescription());
            preparedStatement.setDate(5,coupon.getStartDate());
            preparedStatement.setDate(6,coupon.getEndDate());
            preparedStatement.setInt(7, coupon.getAmount());
            preparedStatement.setDouble(8, coupon.getPrice());
            preparedStatement.setString(9, coupon.getImage());
            preparedStatement.executeUpdate();
            System.out.println("The coupon was added successfully.");

        } catch (InterruptedException | SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().restoreConnection(connection);
                connection = null;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }


        }
    }

    @Override
    public void updateCoupon(Coupon coupon) {

    }

    @Override
    public void deleteCoupon(int couponID) {

    }

    @Override
    public List<Coupon> getAllCoupons() {
        return null;
    }

    @Override
    public Coupon getOneCoupon() {
        return null;
    }

    @Override
    public void addCouponPurchase(int customerID, int couponID) {

    }

    @Override
    public void deleteCouponPurchase(int customerID, int couponID) {

    }
}
