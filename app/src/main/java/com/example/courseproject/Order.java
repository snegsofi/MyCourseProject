package com.example.courseproject;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Order {

    private String id;
    private Integer table;
    private Integer price;
    private String status;
    private Date datetime;
    private HashMap<String,List<String>> orders;
    //private List<GuestCart> orders;
    private String idWaiter;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime= datetime;
    }

    public  HashMap<String,List<String>> getOrders() {
        return orders;
    }

    public void setOrders( HashMap<String,List<String>> orders) {
        this.orders = orders;
    }

    public String getIdWaiter() {
        return idWaiter;
    }

    public void setIdWaiter(String idWaiter) {
        this.idWaiter = idWaiter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTable() {
        return table;
    }

    public void setTable(Integer table) {
        this.table = table;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Order(String  id, Integer table, Integer price, String status, Date datetime,  HashMap<String,List<String>> orders) {
        this.id = id;
        this.table = table;
        this.price = price;
        this.status = status;
        this.datetime = datetime;
        this.orders = orders;
    }

    public Order(String  id, Integer table, Integer price) {
        this.id = id;
        this.table = table;
        this.price = price;
    }

    public Order() {
    }
}
