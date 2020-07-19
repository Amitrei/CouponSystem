package com.amitrei;

import com.amitrei.test.FullTest;
import com.amitrei.test.FullTestTable;

public class program {

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        FullTest test = new FullTest();
        test.testAll();

    }

}
