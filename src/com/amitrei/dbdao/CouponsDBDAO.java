package com.amitrei.dbdao;

import com.amitrei.beans.Category;
import com.amitrei.beans.Coupon;
import com.amitrei.dao.CouponsDAO;
import com.amitrei.db.ConnectionPool;
import com.amitrei.utils.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CouponsDBDAO implements CouponsDAO {
    public static final String ADD_COUPON = "INSERT INTO `couponsystem`.`coupons` (`COMPANY_ID`, `CATEGORY_ID`,`TITLE`,`DESCRIPTION`,`START_DATE`,`END_DATE`,`AMOUNT`,`PRICE`,`IMAGE`) VALUES (?,?,?,?,?,?,?,?,?);";
    public static final String UPDATE_COUPON = "UPDATE `couponsystem`.`coupons` SET  `CATEGORY_ID` = ?,`TITLE`= ? ,`DESCRIPTION` = ?,`START_DATE`=?,`END_DATE`=?,`AMOUNT`=?,`PRICE`=?,`IMAGE`=?  WHERE (`ID` = ?)";
    public static final String DELETE_COUPONS_PURCHASES_OF_COMPANY_BY_ID = "DELETE FROM `couponsystem`.`customers_vs_coupons` WHERE (`COUPON_ID` = ?)";
    public static final String ALL_COMPANIES_BY_ID = "SELECT * FROM `couponsystem`.`coupons` WHERE (`COMPANY_ID`=?)";
    public static final String DELETE_COUPON = "DELETE FROM `couponsystem`.`coupons` WHERE ID=?";
    public static final String GET_ALL_COUPONS = "SELECT * FROM `couponsystem`.`coupons`";
    public static final String GET_COUPON_BY_ID = "SELECT * FROM `couponsystem`.`coupons` WHERE `ID`=?";
    public static final String ADD_COUPON_PURCHASE = "INSERT INTO `couponsystem`.`customers_vs_coupons` (`CUSTOMER_ID`, `COUPON_ID`) VALUES (?,?);";
    public static final String DELETE_COUPON_PURCHASE = "DELETE FROM `couponsystem`.`customers_vs_coupons` WHERE (`CUSTOMER_ID` = ?) and (`COUPON_ID` = ?);";
    public static final String DELETE_COUPON_PURCHASE_BY_COUPONID = "DELETE FROM `couponsystem`.`customers_vs_coupons` WHERE (`COUPON_ID` = ?);";
    public static final String IS_COUPON_EXISTS_BY_ID = "SELECT EXISTS(SELECT 1 FROM `couponsystem`.`coupons` WHERE `ID`=? LIMIT 1)";
    public static final String IS_COUPON_EXISTS_BY_TITLE_AND_ID = "SELECT EXISTS(SELECT 1 FROM `couponsystem`.`coupons` WHERE `COMPANY_ID`=? AND `TITLE`=? LIMIT 1)";
    public static final String GET_COUPON_ID = "SELECT * FROM `couponsystem`.`coupons` WHERE `TITLE`=? AND `COMPANY_ID`=?";
    private Connection connection = null;
    DateUtil dateUtil = new DateUtil();


    @Override
    public void addCoupon(Coupon coupon) {
        Connection connection2 = null;

        try {
            connection2 = ConnectionPool.getInstance().getConnection();


            String sql = ADD_COUPON;
            PreparedStatement preparedStatement = connection2.prepareStatement(sql);
            preparedStatement.setInt(1, coupon.getCompanyID());
            preparedStatement.setInt(2, coupon.getCategory().ordinal() + 1);
            preparedStatement.setString(3, coupon.getTitle());
            preparedStatement.setString(4, coupon.getDescription());
            preparedStatement.setDate(5, dateUtil.convertToSql(coupon.getStartDate()));
            preparedStatement.setDate(6, dateUtil.convertToSql(coupon.getEndDate()));
            preparedStatement.setInt(7, coupon.getAmount());
            preparedStatement.setDouble(8, coupon.getPrice());
            preparedStatement.setString(9, coupon.getImage());
            preparedStatement.executeUpdate();


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
            String sql = UPDATE_COUPON;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, coupon.getCategory().ordinal() + 1);
            preparedStatement.setString(2, coupon.getTitle());
            preparedStatement.setString(3, coupon.getDescription());
            preparedStatement.setDate(4, dateUtil.convertToSql(coupon.getStartDate()));
            preparedStatement.setDate(5, dateUtil.convertToSql(coupon.getEndDate()));
            preparedStatement.setInt(6, coupon.getAmount());
            preparedStatement.setDouble(7, coupon.getPrice());
            preparedStatement.setString(8, coupon.getImage());
            preparedStatement.setInt(9, couponID);
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
                String sql = DELETE_COUPONS_PURCHASES_OF_COMPANY_BY_ID;
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
            String sql = ALL_COMPANIES_BY_ID;
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
                Coupon coupon = new Coupon(getCompanyID, Category.values()[getCategoryID - 1], getTitle, getDescription, getStartDate, getEndDate, getAmount, getPrice, getImage);
                coupon.setId(getID);
                couponsList.add(coupon);
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
            String sql = DELETE_COUPON;

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
            String sql = GET_ALL_COUPONS;
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
                Coupon coupon=new Coupon(getCompanyID, Category.values()[getCategoryID - 1], getTitle, getDescription, getStartDate, getEndDate, getAmount, getPrice, getImage);
                coupon.setId(getID);
                couponsList.add(coupon);
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
            String sql = GET_COUPON_BY_ID;
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
                coupon = new Coupon(getCompanyID, Category.values()[getCategoryID - 1], getTitle, getDescription, getStartDate, getEndDate, getAmount, getPrice, getImage);
                coupon.setId(getID);
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
            String sql = ADD_COUPON_PURCHASE;
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
            String sql = DELETE_COUPON_PURCHASE;
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
            String sql = DELETE_COUPON_PURCHASE_BY_COUPONID;
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
            String sql = IS_COUPON_EXISTS_BY_ID;
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
            String sql = IS_COUPON_EXISTS_BY_TITLE_AND_ID;
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
            String sql = GET_COUPON_ID;
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
