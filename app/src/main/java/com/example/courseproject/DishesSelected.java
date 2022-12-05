package com.example.courseproject;

public class DishesSelected {
    private Integer positionParent;
    private Integer positionChild;
    private Integer dishCount;

    public Integer getPositionParent() {
        return positionParent;
    }

    public void setPositionParent(Integer positionParent) {
        this.positionParent = positionParent;
    }

    public Integer getPositionChild() {
        return positionChild;
    }

    public void setPositionChild(Integer positionChild) {
        this.positionChild = positionChild;
    }

    public Integer getDishCount() {
        return dishCount;
    }

    public void setDishCount(Integer dishCount) {
        this.dishCount = dishCount;
    }

    public DishesSelected(Integer positionParent, Integer positionChild, Integer dishCount) {
        this.positionParent = positionParent;
        this.positionChild = positionChild;
        this.dishCount = dishCount;
    }
}
