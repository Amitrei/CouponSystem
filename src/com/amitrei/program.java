package com.amitrei;
import com.amitrei.test.FullTest;
import com.amitrei.test.InterviewTest;



public class program {

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        FullTest test = new FullTest();
        test.testAll();


        InterviewTest interviewTest = new InterviewTest();
//        interviewTest.Test();



           }

}
