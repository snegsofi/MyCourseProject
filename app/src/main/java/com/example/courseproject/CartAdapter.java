package com.example.courseproject;

import android.content.Context;
import android.util.Log;
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

    public CartAdapter(Context context, List<Dish> dishList,ItemClickListener clickListener) {
        this.context=context;
        dishes = dishList;
        this.clickListener=clickListener;
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
        TextView textView1 = holder.nameDishTextView;
        textView1.setText(dish.getDishName());
        ImageView imageView=holder.dishPhotoImageView;
        imageView.setImageResource(dish.getDishPhoto());
        TextView count= holder.countDishTextView;
        count.setText(Integer.toString(dish.getDishCount()));
        TextView textView = holder.priceDishTextView;
        textView.setText(Integer.toString(dish.getDishPrice()));

        ImageButton removeButton= holder.removeButton;
        ImageButton addButton= holder.addButton;
        ImageButton deleteButton=holder.deleteButton;


        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(count.getText().toString())>1){
                    int countDish=Integer.parseInt(count.getText().toString());
                    countDish--;
                    count.setText(Integer.toString(countDish));

                    int newPrice=dish.getDishPrice()-(dish.getDishPrice()/(countDish+1));
                    dish.setDishPrice(newPrice);
                    textView.setText(Integer.toString(dish.getDishPrice()));
                }
                else{
                    clickListener.onDeleteCartItemClick(dish, holder.getAdapterPosition());
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int countDish=Integer.parseInt(count.getText().toString());
                countDish++;
                count.setText(Integer.toString(countDish));
                dish.setDishCount(countDish);

                int newPrice=countDish*(dish.getDishPrice()/(countDish-1));
                dish.setDishPrice(newPrice);
                textView.setText(Integer.toString(dish.getDishPrice()));

                clickListener.onNewCartItemCount(dish, holder.getAdapterPosition());
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onDeleteCartItemClick(dish, holder.getAdapterPosition());
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
        public ImageButton deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);

            priceDishTextView = (TextView) itemView.findViewById(R.id.textView_priceCart);
            nameDishTextView = (TextView) itemView.findViewById(R.id.textView_dishNameCart);
            dishPhotoImageView=(ImageView) itemView.findViewById(R.id.imageView_dishPhotoCart);
            countDishTextView=(TextView) itemView.findViewById(R.id.count_TextViewCart);
            removeButton=(ImageButton) itemView.findViewById(R.id.remove_ButtonCart);
            addButton=(ImageButton) itemView.findViewById(R.id.add_ButtonCart);
            deleteButton=(ImageButton) itemView.findViewById(R.id.delete_ButtonCart);
        }
    }

    private ItemClickListener clickListener;

    public interface ItemClickListener{
        void onDeleteCartItemClick(Dish dish, int position);
        void onNewCartItemCount(Dish dish,int position);
    }

}