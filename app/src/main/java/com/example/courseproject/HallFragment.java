package com.example.courseproject;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;

public class HallFragment extends Fragment {

    List<ImageButton> tableList;

    AlertDialog alertDialog;

    public HallFragment(){

    }

    public static HallFragment newInstance()
    {
        return new HallFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.hall_fragment,container,false);

        tableList=new ArrayList<>();

        tableList.add(view.findViewById(R.id.imageButton1));
        tableList.add(view.findViewById(R.id.imageButton2));
        tableList.add(view.findViewById(R.id.imageButton3));
        tableList.add(view.findViewById(R.id.imageButton4));
        tableList.add(view.findViewById(R.id.imageButton5));
        tableList.add(view.findViewById(R.id.imageButton6));
        tableList.add(view.findViewById(R.id.imageButton7));
        tableList.add(view.findViewById(R.id.imageButton8));
        tableList.add(view.findViewById(R.id.imageButton9));
        tableList.add(view.findViewById(R.id.imageButton10));


        for(int i=0;i< tableList.size();i++){
            tableList.get(i).setBackgroundColor(getResources().getColor(R.color.grey));
            buttonPress(tableList.get(i));
        }

        return view;
    }


    public void buttonPress(ImageButton table){
        table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorDrawable tableColor = (ColorDrawable) table.getBackground();
                if(tableColor.getColor()==getResources().getColor(R.color.orange)){
                    table.setBackgroundColor(getResources().getColor(R.color.grey));
                }
                else{
                    table.setBackgroundColor(getResources().getColor(R.color.orange));
                    showDialog();
                }
            }
        });
    }

    public void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Введите количество гостей");

        View showDialogView=getLayoutInflater().inflate(R.layout.custom_dialog,null);
        EditText eCount;
        eCount=showDialogView.findViewById(R.id.countGuest_EditText);
        Button submit=showDialogView.findViewById(R.id.addGuest_Button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!eCount.getText().toString().isEmpty()){
                    Integer guestCount=Integer.parseInt(eCount.getText().toString());
                    setFragment(guestCount);
                    alertDialog.dismiss();
                }
            }
        });
        builder.setView(showDialogView);
        alertDialog=builder.create();
        alertDialog.show();
    }


    public void setFragment(int guestCount){
        Fragment fragment=MenuFragment.newInstance(guestCount);

        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.fl_content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
