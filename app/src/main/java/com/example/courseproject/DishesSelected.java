package com.example.courseproject;

public class DishesSelected {
    private String dishName;
    private Integer dishCount;
    private Dish dish;

    public DishesSelected(String dishName, Integer dishCount,Dish dish) {
        this.dishName = dishName;
        this.dishCount = dishCount;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Integer getDishCount() {
        return dishCount;
    }

    public void setDishCount(Integer dishCount) {
        this.dishCount = dishCount;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
}
