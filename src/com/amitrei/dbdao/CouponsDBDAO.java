package com.amitrei.dbdao;

import com.amitrei.beans.Company;
import com.amitrei.beans.Coupon;
import com.amitrei.dao.CouponsDAO;
import com.amitrei.db.ConnectionPool;
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
            connection2 = ConnectionPool.getInstance().getConnection();

            for (Coupon coupon : coupons) {


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
    public void updateCoupon(int couponID, Coupon coupon) {


        try {

            connection = ConnectionPool.getInstance().getConnection();
            String sql = "UPDATE `couponsystem`.`coupons` SET  `CATEGORY_ID` = ?,`DESCRIPTION` = ?,`START_DATE`=?,`END_DATE`=?,`AMOUNT`=?,`PRICE`=?,`IMAGE`=?  WHERE (`ID` = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, coupon.getCategory().ordinal() + 1);
            preparedStatement.setString(2, coupon.getDescription());
            preparedStatement.setDate(3, coupon.getStartDate());
            preparedStatement.setDate(4, coupon.getEndDate());
            preparedStatement.setInt(5, coupon.getAmount());
            preparedStatement.setDouble(6, coupon.getPrice());
            preparedStatement.setString(7, coupon.getImage());
            preparedStatement.setInt(8, couponID);
            preparedStatement.executeUpdate();

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


    public void deleteCouponsPurchasesOfCompany(int couponCompanyID) {
        Connection connection2 = null;

        // Looping over an ArrayList of all coupons of the same company
        try {
            for (Coupon coupon : getAllCouponsOfCompany(couponCompanyID)) {
                connection2 = ConnectionPool.getInstance().getConnection();
                String sql = "DELETE FROM `couponsystem`.`customers_vs_coupons` WHERE (`COUPON_ID` = ?)";
                PreparedStatement preparedStatement = connection2.prepareStatement(sql);
                preparedStatement.setInt(1, coupon.getId());
                preparedStatement.executeUpdate();
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

    public List<Coupon> getAllCouponsOfCompany(int companyID) {
        List<Coupon> couponsList = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT * FROM `couponsystem`.`coupons` WHERE (`COMPANY_ID`=?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, companyID);
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
    public void deleteCoupon(int... couponsID) {
        Connection connection2 = null;
        try {
            connection2 = ConnectionPool.getInstance().getConnection();
            String sql = "DELETE FROM `couponsystem`.`coupons` WHERE ID=?";

            PreparedStatement preparedStatement = connection2.prepareStatement(sql);
            for (int couponId : couponsID) {


                preparedStatement.setInt(1, couponId);
                preparedStatement.executeUpdate();
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
        return couponsList;
    }

    @Override
    public Coupon getOneCoupon(int couponID) {


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
    public void addCouponPurchase(int customerID, int couponID) {


        try {
            connection = ConnectionPool.getInstance().getConnection();
            String sql = "INSERT INTO `couponsystem`.`customers_vs_coupons` (`CUSTOMER_ID`, `COUPON_ID`) VALUES (?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerID);
            preparedStatement.setInt(2, couponID);
            preparedStatement.executeUpdate();

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

    public void deleteCouponPurchase(int couponID) {
        try {


            connection = ConnectionPool.getInstance().getConnection();
            String sql = "DELETE FROM `couponsystem`.`customers_vs_coupons` WHERE (`COUPON_ID` = ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, couponID);
            preparedStatement.executeUpdate();

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

    public int getCouponIDFromDB(Coupon coupon) {
        Connection connection2 = null;
        int result = -1;
        try {
            connection2 = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT * FROM `couponsystem`.`coupons` WHERE `TITLE`=? AND `COMPANY_ID`=?";
            PreparedStatement preparedStatement = connection2.prepareStatement(sql);
            preparedStatement.setString(1, coupon.getTitle());
            preparedStatement.setInt(2, coupon.getCompanyID());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result = resultSet.getInt(1);
            }
            return result;

        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();

        } finally {
            try {
                ConnectionPool.getInstance().restoreConnection(connection2);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }

}
