package com.example.courseproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.ViewHolder> {

    List<Guest> guests;
    private Context context;

    public GuestAdapter(Context context,List<Guest> guestList) {
        this.context=context;
        guests = guestList;
    }

    @NonNull
    @Override
    public GuestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.recyclerview_quest_layout, parent, false);

        return new ViewHolder(contactView);
    }

    DishAdapter dishAdapter;
    @Override
    public void onBindViewHolder(@NonNull GuestAdapter.ViewHolder holder, int position) {
        holder.guestTextView.setText(guests.get(position).getGuestName());

        dishAdapter=new DishAdapter(context, guests.get(position).getDishList(), new DishAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Dish dish, int position) {

            }
        });

        // размещение элементов
        holder.dishRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
        // Прикрепрепляем адаптер к recyclerView
        holder.dishRecyclerView.setAdapter(dishAdapter);
        dishAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return guests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // описывает и предоставляет доступ ко всем представлениям в каждой строке элемента
        public TextView guestTextView;
        public RecyclerView dishRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);

            guestTextView = (TextView) itemView.findViewById(R.id.guestTextView);
            dishRecyclerView=(RecyclerView) itemView.findViewById(R.id.dishRecyclerView);
        }
    }

}
