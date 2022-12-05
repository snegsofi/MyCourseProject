package com.example.courseproject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.List;

public class CartViewModel extends ViewModel {

    private MutableLiveData<List<Dish>> dish=new MutableLiveData<>();

    public void setRating(List<Dish> input){
        dish.setValue(input);
    }

    public LiveData<List<Dish>> getRating(){
        return dish;
    }

}
