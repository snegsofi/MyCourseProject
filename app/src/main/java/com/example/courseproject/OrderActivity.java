package com.example.courseproject;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    List<Guest> guestList;

    List<Dish> dish1List;
    List<Dish> dish2List;

    List<FoodCategory> foodCategoryList;

    GuestAdapter adapter;
    FoodCategoryAdapter foodCategoryAdapter;
    Button addGuest_btn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_screen);

        addGuest_btn=findViewById(R.id.addGuestButton);

        RecyclerView rvEmployees = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView rvFoodCategory=findViewById(R.id.foodCategory_recyclerView);

        // Initialize contacts
        guestList = new ArrayList<>();
        dish1List=new ArrayList<>();
        dish2List=new ArrayList<>();
        foodCategoryList=new ArrayList<>();

        setInitialData();

        addGuest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dish dish=new Dish(1,"Блюдо");
                //List<Dish> dishes=new ArrayList<>();
                //dishes.add(dish);
                //guestList.add(new Guest("Гость "+Integer.toString(guestList.size()+1),dishes));
                //adapter.notifyDataSetChanged();
            }
        });

        // Создание адаптера
        adapter = new GuestAdapter(this, guestList);
        // размещение элементов
        rvEmployees.setLayoutManager(new LinearLayoutManager(this));
        // Прикрепрепляем адаптер к recyclerView
        rvEmployees.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        foodCategoryAdapter=new FoodCategoryAdapter(this,foodCategoryList);
        rvFoodCategory.setLayoutManager(new LinearLayoutManager(this));
        rvFoodCategory.setAdapter(foodCategoryAdapter);
        foodCategoryAdapter.notifyDataSetChanged();
    }

    public void setInitialData(){

        //dish1List.add(new Dish(1,"lalala"));
        //dish2List.add(new Dish(2,"qwe"));
//
        //guestList.add(new Guest("Quest 1", dish1List));
//
        //foodCategoryList.add(new FoodCategory("Мясо"));
        //foodCategoryList.add(new FoodCategory("Рыба"));
        //foodCategoryList.add(new FoodCategory("Птица"));
        //foodCategoryList.add(new FoodCategory("Напитки"));
        //foodCategoryList.add(new FoodCategory("Супы"));

    }
}
