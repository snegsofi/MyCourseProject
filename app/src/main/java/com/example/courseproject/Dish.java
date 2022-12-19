package com.example.courseproject;

import java.util.ArrayList;

public class Dish {

    private int DishCount;
    private String DishName;
    private int DishPhoto;
    private int DishPrice;

    public int getDishPrice() {
        return DishPrice;
    }

    public void setDishPrice(int dishPrice) {
        DishPrice = dishPrice;
    }

    public int getDishCount() {
        return DishCount;
    }

    public void setDishCount(int dishCount) {
        DishCount = dishCount;
    }

    public String getDishName() {
        return DishName;
    }

    public void setDishName(String dishName) {
        DishName = dishName;
    }

    public int getDishPhoto() {
        return DishPhoto;
    }

    public void setDishPhoto(int dishPhoto) {
        this.DishPhoto = dishPhoto;
    }

    public Dish(int dishPrice, String dishName, int dishPhoto,int dishCount) {
        DishPrice = dishPrice;
        DishName = dishName;
        DishPhoto=dishPhoto;
        DishCount=dishCount;
    }

    public Dish() {
    }


}
