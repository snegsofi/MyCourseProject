package com.example.courseproject;

import java.util.List;

public class GuestCart {
    private Integer guestName;
    private List<Dish> dishList;

    public Integer getGuestName() {
        return guestName;
    }

    public void setGuestName(Integer guestName) {
        this.guestName = guestName;
    }

    public List<Dish> getDishList() {
        return dishList;
    }

    public void setDishList(List<Dish> dishList) {
        this.dishList = dishList;
    }

    public GuestCart(Integer guestName, List<Dish> dishList) {
        this.guestName = guestName;
        this.dishList = dishList;
    }
}
