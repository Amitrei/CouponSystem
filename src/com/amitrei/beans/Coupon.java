package com.amitrei.beans;

import java.util.Date;

public class Coupon {
    private int id;
    private int CompanyID;
    private Category category;
    private String title;
    private Date startDate;
    private Date endDate;
    private int amount;
    private double price;
    private String image;

    private String description;

    public Coupon(int id, int companyID, Category category, String title, String description, Date startDate, Date endDate, int amount, double price, String image) {
        this.id = id;
        CompanyID = companyID;
        this.category = category;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.price = price;
        this.image = image;
        this.description = description;
    }

    public Coupon(int companyID, Category category, String title, String description, Date startDate, Date endDate, int amount, double price, String image) {

        CompanyID = companyID;
        this.category = category;
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
        this.id = id;
    }

    public int getCompanyID() {
        return CompanyID;
    }

    public String getDescription() {
        return description;
    }

    public void setCompanyID(int companyID) {
        CompanyID = companyID;
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

    public java.sql.Date getStartDate() {
        return new java.sql.Date(startDate.getTime());
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public java.sql.Date getEndDate() {
        return new java.sql.Date(endDate.getTime());
    }

    public void setEndDate(Date endDate) {
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
        return "Coupon{" +
                "id=" + id +
                ", CompanyID=" + CompanyID +
                ", category=" + category +
                ", title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", amount=" + amount +
                ", price=" + price +
                ", image='" + image + '\'' +
                '}';
    }
}
