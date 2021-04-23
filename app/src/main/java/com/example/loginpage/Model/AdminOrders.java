package com.example.loginpage.Model;

public class AdminOrders
{
    private String address, city, name, phone, state, time,date, totalAmount, username;

    public AdminOrders()
    {
    }

    public AdminOrders(String address, String city, String name, String phone, String state, String time, String date, String totalAmount, String username) {
        this.address = address;
        this.city = city;
        this.name = name;
        this.phone = phone;
        this.state = state;
        this.time = time;
        this.date = date;
        this.totalAmount = totalAmount;
        this.username = username;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
