package com.amitrei;

import com.amitrei.beans.Category;
import com.amitrei.beans.Company;
import com.amitrei.beans.Coupon;
import com.amitrei.beans.Customer;
import com.amitrei.exceptions.AlreadyExistsException;
import com.amitrei.exceptions.DoesNotExistsException;
import com.amitrei.exceptions.IllegalActionException;
import com.amitrei.facade.AdminFacade;
import com.amitrei.facade.CompanyFacade;
import com.amitrei.facade.CustomerFacade;
import com.amitrei.security.ClientType;
import com.amitrei.security.LoginManager;
import com.amitrei.test.FullTest;
import com.amitrei.test.InterviewTest;
import com.amitrei.utils.DateUtil;


public class program {

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        FullTest test = new FullTest();
//        test.testAll();


        InterviewTest interviewTest = new InterviewTest();
        interviewTest.Test();



           }

}
