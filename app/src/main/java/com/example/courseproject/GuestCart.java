package com.example.courseproject;

import java.util.List;

public class GuestCart {
    private String guestName;
    private List<Dish> dishList;

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public List<Dish> getDishList() {
        return dishList;
    }

    public void setDishList(List<Dish> dishList) {
        this.dishList = dishList;
    }

    public GuestCart(String guestName, List<Dish> dishList) {
        this.guestName = guestName;
        this.dishList = dishList;
    }
}
