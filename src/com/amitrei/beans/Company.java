package com.amitrei.beans;

import com.amitrei.dbdao.CompaniesDBDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Company {
    private int id;
    private String name;
    private String email;
    private String password;
    private List<Coupon> coupons = new ArrayList<>();

    public Company(String name, String email, String password, List<Coupon> coupons) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.coupons = coupons;
    }

    public Company(String name, String email, String password) throws SQLException, InterruptedException {
        this.name = name;
        this.email = email;
        this.password = password;

    }

    // CTOR for updateCompany
    public Company(String email, String password) throws SQLException, InterruptedException {
        this.email = email;
        this.password = password;

    }

    // CTOR for CompaniesDBDAO - getAllCompanies

    public Company(int id, String name, String email, String password) throws SQLException, InterruptedException {
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = id;

    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setId(int id) {

        this.id = id;
    }


    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", coupons=" + coupons +
                '}';
    }
}
