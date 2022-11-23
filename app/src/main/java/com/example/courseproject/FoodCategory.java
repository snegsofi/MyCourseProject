package com.example.courseproject;

import java.util.List;

public class FoodCategory {
    private String categoryName;
    private List<Dish> dishList;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Dish> getDishList() {
        return dishList;
    }

    public void setDishList(List<Dish> dishList) {
        this.dishList = dishList;
    }

    public FoodCategory(String categoryName, List<Dish> dishList) {
        this.categoryName = categoryName;
        this.dishList = dishList;
    }

    public FoodCategory(){

    }

}
