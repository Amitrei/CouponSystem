package com.amitrei.beans;

import com.amitrei.exceptions.IllegalActionException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Coupon> coupons = new ArrayList<>();

    public Customer(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }


    /**
     *
     * Injecting the auto increment ID from the setter with a lock and not from CTOR to prevent custom id update.
     *
     */


    public void setId(int id) {

        if (this.id == 0) this.id = id;


        else {
            try {
                throw new IllegalActionException("Cannot change customer ID");
            }
            catch (IllegalActionException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    @Override
    public String toString() {
        List<String> couponsListForPrint = coupons.stream().flatMap(coupon -> Stream.of(coupon.getId()+"-" +coupon.getTitle())).collect(Collectors.toList());
        return
                "Customer id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", coupons=" +  couponsListForPrint +
                " \n";
    }

}
