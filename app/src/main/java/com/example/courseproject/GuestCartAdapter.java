package com.example.courseproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GuestCartAdapter extends RecyclerView.Adapter<GuestCartAdapter.ViewHolder> {

    List<GuestCart> guestCarts;
    Context context;

    public GuestCartAdapter(Context context, List<GuestCart> guestCartList, ItemClickListener clickListener) {
        this.context=context;
        this.guestCarts = guestCartList;
        this.clickListener=clickListener;
    }

    @NonNull
    @Override
    public GuestCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.cart_recyclerview_parent, parent, false);

        return new GuestCartAdapter.ViewHolder(contactView);
    }

    CartAdapter adapter;
    @Override
    public void onBindViewHolder(@NonNull GuestCartAdapter.ViewHolder holder, int position) {

        holder.guestNameTextView.setText(Integer.toString(guestCarts.get(position).getGuestName()));

        adapter=new CartAdapter(context, guestCarts.get(position).getDishList(), new CartAdapter.ItemClickListener() {
            @Override
            public void onDeleteCartItemClick(Dish dish, int position) {
                clickListener.onDeleteCartItemClick2(guestCarts.get(position), holder.getAdapterPosition());
            }

            @Override
            public void onNewCartItemCount(Dish dish, int position) {

            }
        });

        // размещение элементов
        holder.dishRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
        // Прикрепрепляем адаптер к recyclerView
        holder.dishRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }



    @Override
    public int getItemCount() {
        return guestCarts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // описывает и предоставляет доступ ко всем представлениям в каждой строке элемента
        public TextView guestNameTextView;
        public RecyclerView dishRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);

            guestNameTextView = (TextView) itemView.findViewById(R.id.guestTextViewCart);
            dishRecyclerView=(RecyclerView) itemView.findViewById(R.id.dishRecyclerViewCart);

        }
    }

    public void setFilterList(List<GuestCart> filterList){
        this.guestCarts=filterList;
        notifyDataSetChanged();
    }


    private ItemClickListener clickListener;

    public interface ItemClickListener{
        void onDeleteCartItemClick2(GuestCart guestCart, int position);
    }
}

