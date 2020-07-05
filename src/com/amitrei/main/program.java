package com.amitrei.main;

import com.amitrei.beans.Category;
import com.amitrei.beans.Coupon;
import com.amitrei.dbdao.CouponsDBDAO;
import com.amitrei.utils.MyDateUtil;

public class program {

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        FullTest test = new FullTest();
        test.testAll();

    }

}
