package com.example.courseproject;

import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderFragment extends Fragment implements OrderAdapter.ItemClickListener{

    public FloatingActionButton newOrderButton;

    private static final String TAG = "orders";

    private static final String ARG_PARAM1="param1";

    public OrderFragment(){

    }

    public static OrderFragment newInstance(String waiter)
    {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1,waiter);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        Log.d("waiterOrderFragment",waiter);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null) {
            waiter = getArguments().getString(ARG_PARAM1);
        }

        //Log.d("waiterOrderFragment",waiter);
    }

    int orderEmpty=0;

    ViewStub emptyLayout;

    RecyclerView orders_rv;
    OrderAdapter adapter;
    List<Order> orders;

    String waiter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.order_fragment,container,false);

        guestCarts=new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        orders_rv=view.findViewById(R.id.rv_order);
        newOrderButton=view.findViewById(R.id.newOrder_Button);
        newOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment hallFragment=HallFragment.newInstance(waiter);

                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                fragmentTransaction.replace(R.id.fl_content, hallFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                Snackbar.make(view, "Создание заказа. Выберите стол", Snackbar.LENGTH_LONG).show();
            }
        });

        orders=new ArrayList<>();
        initialArray();



        return view;
    }


    @Override
    public void onItemClick(Order order, int position) {

    }

    List<GuestCart> guestCarts;
    FirebaseFirestore db;

    public void initialArray(){

        db.collection("Orders")
                .whereEqualTo("idWaiter",waiter)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());


                                Order order=document.toObject(Order.class);
                                orders.add(order);


                                List<Order> sortedByDate=sortedOrder(orders);
                                List<Order> sortedList=sortedByStatusOrder(sortedByDate);

                                // Создание адаптера
                                adapter = new OrderAdapter(getContext(), sortedList,OrderFragment.this);
                                // размещение элементов
                                orders_rv.setLayoutManager(new LinearLayoutManager(getContext()));
                                // Прикрепрепляем адаптер к recyclerView
                                orders_rv.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                    }
                });

    }


    public List<Order> sortedOrder(List<Order> list){
        Map<Integer,Date> orderMap=new HashMap<>();
        for(int i=0;i<list.size();i++){
            orderMap.put(i,list.get(i).getDatetime());
        }


        LinkedHashMap<Integer,Date> sortedOrder= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sortedOrder = orderMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(LinkedHashMap::new,
                            (m, c) -> m.put(c.getKey(), c.getValue()),
                            LinkedHashMap::putAll);
        }


        List<Order> orderList=new ArrayList<>();
        for (Map.Entry<Integer, Date> entry : sortedOrder.entrySet()) {
            orderList.add(list.get(entry.getKey()));
        }

        return orderList;

    }

    public List<Order> sortedByStatusOrder(List<Order> list){

        List<Order> sortedList=new ArrayList<>();

        for(int i=0;i<list.size();i++){
            if(list.get(i).getStatus().contains("open")){
                sortedList.add(list.get(i));
            }
        }

        for(int i=0;i<list.size();i++){
            if(list.get(i).getStatus().contains("close")){
                sortedList.add(list.get(i));
            }
        }

        return sortedList;

    }



}
