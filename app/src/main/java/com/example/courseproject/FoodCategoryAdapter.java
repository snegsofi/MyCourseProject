package com.example.courseproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodCategoryAdapter extends RecyclerView.Adapter<FoodCategoryAdapter.ViewHolder> {

    List<FoodCategory> foodCategories;
    Context context;

    public FoodCategoryAdapter(Context context, List<FoodCategory> foodCategoryList) {
        this.context=context;
        this.foodCategories = foodCategoryList;
    }

    @NonNull
    @Override
    public FoodCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.category_food_recyclerview, parent, false);

        return new FoodCategoryAdapter.ViewHolder(contactView);
    }

    DishAdapter adapter;
    @Override
    public void onBindViewHolder(@NonNull FoodCategoryAdapter.ViewHolder holder, int position) {

        holder.foodCategoryTextView.setText(foodCategories.get(position).getCategoryName());

        adapter=new DishAdapter(context, foodCategories.get(position).getDishList());

        // размещение элементов
        holder.foodRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
        // Прикрепрепляем адаптер к recyclerView
        holder.foodRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return foodCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // описывает и предоставляет доступ ко всем представлениям в каждой строке элемента
        public TextView foodCategoryTextView;
        public RecyclerView foodRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);

            foodCategoryTextView = (TextView) itemView.findViewById(R.id.textView_categoryName);
            foodRecyclerView=(RecyclerView) itemView.findViewById(R.id.rv_child);

        }
    }

    public void setFilterList(List<FoodCategory> filterList){
        this.foodCategories=filterList;
        notifyDataSetChanged();
    }
}
