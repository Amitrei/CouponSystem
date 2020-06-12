package com.amitrei.dbdao;

import com.amitrei.beans.Coupon;
import com.amitrei.dao.CouponsDAO;
import com.amitrei.db.ConnectionPool;
import com.amitrei.exceptions.CouponAlreadyExistsException;
import com.amitrei.exceptions.CouponDateExpiredException;
import com.amitrei.exceptions.CouponNotFoundException;
import com.amitrei.utils.MyDateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CouponsDBDAO implements CouponsDAO {
    private Connection connection = null;
    MyDateUtil dateUtil = new MyDateUtil();


    @Override
    public void addCoupon(Coupon... coupons) {
        Connection connection2 = null;
        try {
            for (Coupon coupon : coupons) {

                if (isCouponExists(coupon.getTitle(), coupon.getCompanyID())) {
                    try {
                        throw new CouponAlreadyExistsException(coupon.getTitle());
                    } catch (CouponAlreadyExistsException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }

                connection2 = ConnectionPool.getInstance().getConnection();
                String sql = "INSERT INTO `couponsystem`.`coupons` (`COMPANY_ID`, `CATEGORY_ID`,`TITLE`,`DESCRIPTION`,`START_DATE`,`END_DATE`,`AMOUNT`,`PRICE`,`IMAGE`) VALUES (?,?,?,?,?,?,?,?,?);";
                PreparedStatement preparedStatement = connection2.prepareStatement(sql);
                preparedStatement.setInt(1, coupon.getCompanyID());
                preparedStatement.setInt(2, coupon.getCategory().ordinal() + 1);
                preparedStatement.setString(3, coupon.getTitle());
                preparedStatement.setString(4, coupon.getDescription());
                preparedStatement.setDate(5, coupon.getStartDate());
                preparedStatement.setDate(6, coupon.getEndDate());
                preparedStatement.setInt(7, coupon.getAmount());
                preparedStatement.setDouble(8, coupon.getPrice());
                preparedStatement.setString(9, coupon.getImage());
                preparedStatement.executeUpdate();
                System.out.println("The coupon title " + coupon.getTitle() + " - " + coupon.getDescription() + " was added successfully.");
            }
        } catch (InterruptedException | SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().restoreConnection(connection2);
                connection2 = null;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }


        }
    }

    @Override
    public void updateCoupon(int couponID, Coupon coupon) throws CouponNotFoundException {

        if (!isCouponExists(couponID)) throw new CouponNotFoundException(couponID);


        try {

            connection = ConnectionPool.getInstance().getConnection();
            String sql = "UPDATE `couponsystem`.`coupons` SET `COMPANY_ID` = ?, `CATEGORY_ID` = ?, `TITLE` = ?,`DESCRIPTION` = ?,`START_DATE`=?,`END_DATE`=?,`AMOUNT`=?,`PRICE`=?,`IMAGE`=?  WHERE (`ID` = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, coupon.getCompanyID());
            preparedStatement.setInt(2, coupon.getCategory().ordinal() + 1);
            preparedStatement.setString(3, coupon.getTitle());
            preparedStatement.setString(4, coupon.getDescription());
            preparedStatement.setDate(5, coupon.getStartDate());
            preparedStatement.setDate(6, coupon.getEndDate());
            preparedStatement.setInt(7, coupon.getAmount());
            preparedStatement.setDouble(8, coupon.getPrice());
            preparedStatement.setString(9, coupon.getImage());
            preparedStatement.setInt(10, couponID);
            preparedStatement.executeUpdate();
            System.out.println("The update completed successfully.");
        } catch (InterruptedException | SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().restoreConnection(connection);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            connection = null;
        }
    }


    @Override
    public void deleteCoupon(int... couponsID) {
        Connection connection2 = null;
        try {
            connection2 = ConnectionPool.getInstance().getConnection();
            String sql = "DELETE FROM `couponsystem`.`coupons` WHERE ID=?";

            PreparedStatement preparedStatement = connection2.prepareStatement(sql);
            for (int couponId : couponsID) {

                if (!isCouponExists(couponId)) try {
                    throw new CouponNotFoundException(couponId);
                } catch (CouponNotFoundException e) {
                    System.out.println(e.getMessage());
                    continue;
                }

                preparedStatement.setInt(1, couponId);
                preparedStatement.executeUpdate();
                System.out.println("The coupon with the id: " + couponId + " deleted successfully.");
            }
        } catch (InterruptedException | SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().restoreConnection(connection2);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            connection2 = null;
        }
    }


    @Override
    public List<Coupon> getAllCoupons() {
        List<Coupon> couponsList = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT * FROM `couponsystem`.`coupons`";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int getID = resultSet.getInt(1);
                int getCompanyID = resultSet.getInt(2);
                int getCategoryID = resultSet.getInt(3);
                String getTitle = resultSet.getString(4);
                String getDescription = resultSet.getString(5);
                java.sql.Date getStartDate = resultSet.getDate(6);
                java.sql.Date getEndDate = resultSet.getDate(7);
                int getAmount = resultSet.getInt(8);
                Double getPrice = resultSet.getDouble(9);
                String getImage = resultSet.getString(10);
                couponsList.add(new Coupon(getID, getCompanyID, getCategoryID, getTitle, getDescription, getStartDate, getEndDate, getAmount, getPrice, getImage));
            }
            return couponsList;

        } catch (InterruptedException | SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().restoreConnection(connection);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            connection = null;
        }
        return null;

    }

    @Override
    public Coupon getOneCoupon(int couponID) throws CouponNotFoundException {

        if (!isCouponExists(couponID)) throw new CouponNotFoundException(couponID);

        try {
            Coupon coupon = null;
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT * FROM `couponsystem`.`coupons` WHERE `ID`=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, couponID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int getID = resultSet.getInt(1);
                int getCompanyID = resultSet.getInt(2);
                int getCategoryID = resultSet.getInt(3);
                String getTitle = resultSet.getString(4);
                String getDescription = resultSet.getString(5);
                java.sql.Date getStartDate = resultSet.getDate(6);
                java.sql.Date getEndDate = resultSet.getDate(7);
                int getAmount = resultSet.getInt(8);
                Double getPrice = resultSet.getDouble(9);
                String getImage = resultSet.getString(10);
                coupon = new Coupon(getID, getCompanyID, getCategoryID, getTitle, getDescription, getStartDate, getEndDate, getAmount, getPrice, getImage);
            }
            return coupon;

        } catch (InterruptedException | SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().restoreConnection(connection);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            connection = null;
        }
        return null;
    }

    @Override
    public void addCouponPurchase(int customerID, int couponID) throws CouponNotFoundException, CouponDateExpiredException {


        if (dateUtil.isDatePassed(getOneCoupon(couponID).getEndDate())) throw new CouponDateExpiredException(couponID);


        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "INSERT INTO `couponsystem`.`customers_vs_coupons` (`CUSTOMER_ID`, `COUPON_ID`) VALUES (?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerID);
            preparedStatement.setInt(2, couponID);
            preparedStatement.executeUpdate();
            System.out.println("Coupon purchased successfully");

        } catch (InterruptedException | SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().restoreConnection(connection);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            connection = null;

        }
    }

    @Override
    public void deleteCouponPurchase(int customerID, int couponID) {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "DELETE FROM `couponsystem`.`customers_vs_coupons` WHERE (`CUSTOMER_ID` = ?) and (`COUPON_ID` = ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerID);
            preparedStatement.setInt(2, couponID);
            preparedStatement.executeUpdate();
            System.out.println("Purchased of coupon id:" + couponID + " of customerID " + customerID + " successfully deleted");

        } catch (InterruptedException | SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().restoreConnection(connection);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            connection = null;

        }
    }

    public boolean isCouponExists(int couponID) {
        try {
            int isExists = -99;
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT EXISTS(SELECT 1 FROM `couponsystem`.`coupons` WHERE `ID`=? LIMIT 1)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, couponID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                isExists = resultSet.getInt(1);
            }
            return isExists > 0;

        } catch (InterruptedException | SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().restoreConnection(connection);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            connection = null;
        }
        return false;

    }

    public boolean isCouponExists(String couponTitle, int companyID) {
        try {
            int isExists = -99;
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT EXISTS(SELECT 1 FROM `couponsystem`.`coupons` WHERE `COMPANY_ID`=? AND `TITLE`=? LIMIT 1)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, companyID);
            preparedStatement.setString(2, couponTitle);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                isExists = resultSet.getInt(1);
            }
            return isExists > 0;

        } catch (InterruptedException | SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConnectionPool.getInstance().restoreConnection(connection);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            connection = null;
        }
        return false;

    }

}
