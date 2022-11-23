package com.example.courseproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    List<Dish> dishes;
    Context context;

    public CartAdapter(Context context, List<Dish> dishList) {
        this.context=context;
        dishes = dishList;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.cart_recyclerview, parent, false);

        return new CartAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {

        Dish dish = dishes.get(position);

        // Установка значений элементам
        TextView textView = holder.priceDishTextView;
        textView.setText(Integer.toString(dish.getDishPrice()));
        TextView textView1 = holder.nameDishTextView;
        textView1.setText(dish.getDishName());
        ImageView imageView=holder.dishPhotoImageView;
        imageView.setImageResource(dish.getDishPhoto());

        ImageButton removeButton= holder.removeButton;
        ImageButton addButton= holder.addButton;
        TextView count= holder.countDishTextView;

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(count.getText().toString())>0){
                    int countDish=Integer.parseInt(count.getText().toString());
                    countDish--;
                    count.setText(Integer.toString(countDish));
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int countDish=Integer.parseInt(count.getText().toString());
                countDish++;
                count.setText(Integer.toString(countDish));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // описывает и предоставляет доступ ко всем представлениям в каждой строке элемента
        public TextView priceDishTextView;
        public TextView nameDishTextView;
        public ImageView dishPhotoImageView;
        public TextView countDishTextView;
        public ImageButton removeButton;
        public ImageButton addButton;

        public ViewHolder(View itemView) {
            super(itemView);

            priceDishTextView = (TextView) itemView.findViewById(R.id.textView_priceCart);
            nameDishTextView = (TextView) itemView.findViewById(R.id.textView_dishNameCart);
            dishPhotoImageView=(ImageView) itemView.findViewById(R.id.imageView_dishPhotoCart);
            countDishTextView=(TextView) itemView.findViewById(R.id.count_TextViewCart);
            removeButton=(ImageButton) itemView.findViewById(R.id.remove_ButtonCart);
            addButton=(ImageButton) itemView.findViewById(R.id.add_ButtonCart);
        }
    }

}