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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.order_recyclerview, parent, false);

        return new OrderAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {

        Order order = orders.get(position);

        // Установка значений элементам
        TextView textView = holder.dateOrderTextView;
        textView.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(order.getDatetime()));
        TextView textView1 = holder.tableOrderTextView;
        textView1.setText(Integer.toString(order.getTable()));
        TextView textView2 = holder.priceOrderTextView;
        textView2.setText(Integer.toString(order.getPrice()));
        TextView textView3 = holder.statusOrderTextView;
        textView3.setText(order.getStatus());


        String dishes="";
        List<String> dishList=new ArrayList<>();
        HashMap<String,Integer> dishHashMap=new HashMap<>();
        for (Map.Entry<String, List<String>> entry : order.getOrders().entrySet()) {
            dishList.add(entry.getValue().toString());
        }

        for(int i=0;i<dishList.size();i++){
            if(i<(dishList.size()-1)){
                if(dishList.get(i).contains(dishList.get(i+1))){
                    dishHashMap.put(dishList.get(i),(dishHashMap.get(i)+1));
                }
                else{
                    dishHashMap.put(dishList.get(i),1);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : dishHashMap.entrySet()) {
            dishes+=entry.getKey()+" x "+entry.getValue();
        }

        TextView textView4 = holder.dishesOrderTextView;
        textView4.setText(dishes);

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // описывает и предоставляет доступ ко всем представлениям в каждой строке элемента
        public TextView priceOrderTextView;
        public TextView dateOrderTextView;
        public TextView tableOrderTextView;
        public TextView statusOrderTextView;
        public TextView dishesOrderTextView;


        public ViewHolder(View itemView) {
            super(itemView);

            dateOrderTextView = (TextView) itemView.findViewById(R.id.dateOrder);
            tableOrderTextView = (TextView) itemView.findViewById(R.id.tableOrder);
            priceOrderTextView=(TextView) itemView.findViewById(R.id.priceOrder);
            statusOrderTextView = (TextView) itemView.findViewById(R.id.statusOrder);
            dishesOrderTextView = (TextView) itemView.findViewById(R.id.dishesOrder);

        }
    }

    List<Order> orders;
    Context context;

    CartViewModel cartViewModel;

    public OrderAdapter(Context context, List<Order> orderList, OrderAdapter.ItemClickListener clickListener) {
        this.context=context;
        orders = orderList;
        this.clickListener=clickListener;

    }

    private OrderAdapter.ItemClickListener clickListener;

    public interface ItemClickListener{
        void onItemClick(Order order, int position);
    }



}
