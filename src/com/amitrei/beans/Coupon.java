package com.amitrei.beans;

import com.amitrei.exceptions.IllegalActionException;

import java.util.Date;

public class Coupon {
    private int id;
    private int companyID;
    private Category category;
    private String title;
    private Date startDate;
    private Date endDate;
    private int amount;
    private double price;
    private String image;

    private String description;



    /**
     * CTOR for method addCoupon
     */
    public Coupon(int companyID, Category category, String title, String description, Date startDate, Date endDate, int amount, double price, String image) {

        this.companyID = companyID;
        this.category = category;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.price = price;
        this.image = image;
        this.description = description;

    }


    public Coupon(int companyID, int CategoryID, String title, String description, Date startDate, Date endDate, int amount, double price, String image) {
        this.companyID = companyID;
        // Converting int CategoryID --> Category
        this.category = Category.values()[CategoryID - 1];
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.price = price;
        this.image = image;
        this.description = description;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {

        if (this.id == 0)  this.id = id;

        else {
            try {
                throw new IllegalActionException("Cannot change coupon ID ");
            } catch (IllegalActionException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public int getCompanyID() {
        return companyID;
    }

    public String getDescription() {
        return description;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public java.util.Date getStartDate() {
        return this.startDate;
    }

    public java.sql.Date getSQLStartDate() {
        return new java.sql.Date(startDate.getTime());
    }

    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }

    public java.util.Date getEndDate() {
        return this.endDate;
    }

    public java.sql.Date getSQLEndDate() {
        return new java.sql.Date(endDate.getTime());
    }

    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "\nCoupon " +
                "id=" + id +
                ", CompanyID=" + companyID +
                ", category=" + category +
                ", title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", amount=" + amount +
                ", price=" + price +
                ", image='" + image + '\'' +
                "\n";
    }
}
