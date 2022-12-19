package com.example.courseproject;

import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

        //emptyLayout=(ViewStub) view.findViewById(R.id.emptyStateLayout);
        //if(orderEmpty==0){
        //    emptyLayout.inflate();
        //}


        guestCarts=new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        orders_rv=view.findViewById(R.id.rv_order);
        newOrderButton=view.findViewById(R.id.newOrder_Button);
        newOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment hallFragment=HallFragment.newInstance(waiter);

                // HallFragment hallFragment=new HallFragment();
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
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());


                                Order order=document.toObject(Order.class);
                                orders.add(order);

                                // Создание адаптера
                                adapter = new OrderAdapter(getContext(), orders,OrderFragment.this);
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
}
