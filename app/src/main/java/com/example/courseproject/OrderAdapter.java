package com.example.courseproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

        HashMap<String,HashMap<String,Integer>> dishMap=new HashMap<>();
        for (Map.Entry<String, List<String>> entry : order.getOrders().entrySet()) {

            Log.d("order key", entry.getKey());
            Log.d("order value", entry.getValue().toString());



            HashMap<String,Integer> dishHashMap=new HashMap<>();
            List<String> list=new ArrayList<>();
            for(int i=0;i<entry.getValue().size();i++){
                if(!list.contains(entry.getValue().get(i))){
                    list.add(entry.getValue().get(i));
                    dishHashMap.put(entry.getValue().get(i),1);

                    Log.d("map value 1",entry.getValue().get(i)+" 1" );
                }
                else{
                    dishHashMap.put(entry.getValue().get(i),dishHashMap.get(entry.getValue().get(i))+1);

                    Log.d("map value 1",entry.getValue().get(i)+" "+(dishHashMap.get(entry.getValue().get(i))+1));
                }
            }

            dishMap.put(entry.getKey(),dishHashMap);


        }


        for (Map.Entry<String,HashMap<String,Integer>> entry : dishMap.entrySet()) {


            Log.d("key", entry.getKey());

            dishes+="Гость "+entry.getKey();
            dishes+="\n";

            HashMap<String,Integer> dishHashMap=entry.getValue();
            for (Map.Entry<String,Integer> entry1 : dishHashMap.entrySet()) {
                dishes+=entry1.getValue()+" x "+entry1.getKey();
                dishes+="\n";
            }

        }

        TextView textView4 = holder.dishesOrderTextView;
        textView4.setText(dishes);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTableChecked(order.getTable());
                setOrderStatus(order.getId());
            }
        });

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

            db = FirebaseFirestore.getInstance();
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

    private FirebaseFirestore db;
    private void setTableChecked(Integer selectedTable){

        Map<String, Object> table = new HashMap<>();
        table.put("isBusy", false);

        Log.d("selected table number", selectedTable+"");

        db.collection("Tables")
                .whereEqualTo("Id", selectedTable)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful() && !task.getResult().isEmpty()){

                            DocumentSnapshot documentSnapshot=task.getResult().getDocuments().get(0);
                            String documentID= documentSnapshot.getId();

                            db.collection("Tables")
                                    .document(documentID)
                                    .update(table)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("Update table", "Successful update");

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("Update table", "Failed update "+e.toString());

                                        }
                                    });
                        }
                        else{
                            Log.d("Update table", "Some failed");
                        }
                    }
                });

    }

    private void setOrderStatus(String id){

        Map<String, Object> status = new HashMap<>();
        status.put("status", "close");

        db.collection("Orders")
                .whereEqualTo("id", id)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful() && !task.getResult().isEmpty()){

                            DocumentSnapshot documentSnapshot=task.getResult().getDocuments().get(0);
                            String documentID= documentSnapshot.getId();

                            db.collection("Orders")
                                    .document(documentID)
                                    .update(status)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("Update status", "Successful update");

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("Update status", "Failed update "+e.toString());

                                        }
                                    });
                        }
                        else{
                            Log.d("Update status", "Some failed");
                        }
                    }
                });

    }


}
