package com.reecelin.bean;

import java.math.BigDecimal;

/**
 * @author Reece
 * @ClassName Order.java
 * @Description TODO
 * @createTime 2021年05月09日 12:39:00
 */
public class Order {


    private String userId;

    private String goodName;

    private String description;


    private BigDecimal price;

    private String receiveAddress;


    private String phoneNumber;


    public Order() {
    }


    public Order(String userId, String goodName, String description, BigDecimal price, String receiveAddress, String phoneNumber) {
        this.userId = userId;
        this.goodName = goodName;
        this.description = description;
        this.price = price;
        this.receiveAddress = receiveAddress;
        this.phoneNumber = phoneNumber;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @Override
    public String toString() {
        return "Order{" +
                "userId='" + userId + '\'' +
                ", goodName='" + goodName + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", receiveAddress='" + receiveAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
