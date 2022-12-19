package com.example.courseproject;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HallFragment extends Fragment {

    List<ImageButton> tableList;

    AlertDialog alertDialog;

    private static final String TAG = "Tables";

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    public HallFragment(){

    }

    private static final String ARG_PARAM1="param1";
    HashMap<Integer,Boolean> tables;

    public static HallFragment newInstance(String waiter)
    {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1,waiter);
        HallFragment fragment = new HallFragment();
        fragment.setArguments(args);
        return fragment;

    }

    public static HallFragment newInstance()
    {
        return new HallFragment();

    }

    String waiter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null) {
            waiter = getArguments().getString(ARG_PARAM1);
        }

        //Log.d("waiterHallFragment",waiter);
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
        tableList.add(view.findViewById(R.id.imageButton11));

        for(int i=0;i< tableList.size();i++){
            tableList.get(i).setBackgroundColor(getResources().getColor(R.color.grey));
            buttonPress(tableList.get(i), (i+1),waiter);
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tables=new HashMap<>();
        readData();

        return view;
    }

    public void buttonPress(ImageButton table, Integer tableNumber, String waiter){
        table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorDrawable tableColor = (ColorDrawable) table.getBackground();
                if(tableColor.getColor()==getResources().getColor(R.color.orange)){
                    table.setBackgroundColor(getResources().getColor(R.color.grey));
                }
                else{
                    table.setBackgroundColor(getResources().getColor(R.color.orange));
                    showDialog(tableNumber,waiter);
                }
            }
        });
    }

    public void showDialog(Integer tableNumber,String waiter){
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
                    setFragment(guestCount,(tableNumber-1), waiter);
                    alertDialog.dismiss();
                }
            }
        });
        builder.setView(showDialogView);
        alertDialog=builder.create();
        alertDialog.show();
    }

    public void setFragment(int guestCount, int tableNumber, String waiter){
        Fragment fragment=MenuFragment.newInstance(guestCount, tableNumber, waiter);

        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.fl_content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }





    private void readData(){

        //HashMap<Integer,Boolean> tables=new HashMap<>();

        db.collection("Tables")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
////
                                tables.put(Integer.parseInt(document.get("Id").toString()),
                                        Boolean.parseBoolean(document.get("isBusy").toString()));
////

                                Log.d(TAG,"Id="+document.get("Id").toString());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                        Log.d(TAG,"Id="+tables.size());

                        for(int i=0;i< tables.size();i++){

                            for (Map.Entry<Integer, Boolean> entry : tables.entrySet()) {

                                if(i==entry.getKey()){
                                    if(entry.getValue()){
                                        tableList.get(i).setBackgroundColor(getResources().getColor(R.color.orange));
                                        tableList.get(i).setEnabled(false);
                                    }
                                    else{
                                        tableList.get(i).setBackgroundColor(getResources().getColor(R.color.grey));
                                    }
                                }
                            }
                        }
                    }
                });
        //return tables;
    }



}
