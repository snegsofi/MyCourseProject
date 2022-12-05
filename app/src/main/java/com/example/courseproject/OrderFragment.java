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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderFragment extends Fragment{

    public FloatingActionButton newOrderButton;

    public OrderFragment(){

    }

    public static OrderFragment newInstance()
    {
        return new OrderFragment();
    }


    int orderEmpty=0;

    ViewStub emptyLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.order_fragment,container,false);

        emptyLayout=(ViewStub) view.findViewById(R.id.emptyStateLayout);
        if(orderEmpty==0){
            emptyLayout.inflate();
        }


        newOrderButton=view.findViewById(R.id.newOrder_Button);
        newOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HallFragment hallFragment=new HallFragment();
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                fragmentTransaction.replace(R.id.fl_content, hallFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                Snackbar.make(view, "Создание заказа. Выберите стол", Snackbar.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
