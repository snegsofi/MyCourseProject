package com.example.courseproject;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MenuFragment extends Fragment implements
        FoodCategoryAdapter.ItemClickListener, ChipAdapter.ItemClickListener,
        GuestCartAdapter.ItemClickListener {
    List<FoodCategory> parentModelClassList;

    List<Dish> breakfastList;
    List<Dish> saladList;
    List<Dish> mainDishList;
    List<Dish> dessertList;
    List<Dish> drinkList;
    List<Dish> wordGameList;

    FoodCategoryAdapter adapter;
    SearchView searchView;

    ImageButton backOrderButton;
    Button toCartButton;
    Button createOrderButton;

    List<Dish> cartList;
    CartAdapter cartAdapter;
    GuestCartAdapter guestCartAdapter;
    ChipAdapter chipAdapter;
    RecyclerView parent_rv;

    RecyclerView chipRecyclerView;
    ChipGroup chipGroup;

    List<ChipModel> guestList;

    HashMap<Integer,List<DishesSelected>> dishesSelectedHashMap;

    List<GuestCart> guestCarts;

    FirebaseFirestore db;
    RecyclerView cartRecyclerView;
    BottomSheetDialog cartBottomSheetDialog;



    private static final String ARG_PARAM1="param1";
    private static final String ARG_PARAM2="param2";
    private static final String ARG_PARAM3="param3";
    private static final String TAG = "orders";


    public MenuFragment(){

    }

    public static MenuFragment newInstance(int guestCount, int tableNumber, String waiter)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1,guestCount);
        args.putInt(ARG_PARAM2,tableNumber);
        args.putString(ARG_PARAM3,waiter);
        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public void initialComponent(View view){


        mDatabase = FirebaseDatabase.getInstance().getReference();
        backOrderButton=view.findViewById(R.id.backOrderButton);
        toCartButton=view.findViewById(R.id.toCart_Button);
        searchView=view.findViewById(R.id.searchView);
        chipGroup=view.findViewById(R.id.chipGroup);
        chipRecyclerView=view.findViewById(R.id.rv_chip);
        guestCarts=new ArrayList<>();
        dishesSelectedHashMap=new HashMap<>();
        guestList=new ArrayList<>();
        parentModelClassList=new ArrayList<>();
        breakfastList=new ArrayList<>();
        saladList=new ArrayList<>();
        mainDishList=new ArrayList<>();
        dessertList=new ArrayList<>();
        drinkList=new ArrayList<>();
        wordGameList=new ArrayList<>();
        cartList=new ArrayList<>();
        guestNumber=0;
        db = FirebaseFirestore.getInstance();
        selectedMenuItemGuest=new HashMap<>();
        parent_rv=(RecyclerView) view.findViewById(R.id.rv_parent);
        saveCartMap=new HashMap<>();
    }

    RecyclerView guestCart_rv;


    Map<String,List<Dish>> saveCartMap;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.menu_fragment,container,false);

        initialComponent(view);

        // получаем идентификатор по имени файла
        int ids = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(ids);
        textView.setTextColor(Color.BLACK);

        searchView.clearFocus();

        //setInitialData();

        readMenu();



        toCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createDialog();

                guestCarts=new ArrayList<>();

                Integer price=0;
                for(int l=0;l<guestCount;l++){

                    Log.d("contains key", selectedMenuItemGuest.keySet().toString());

                    if(selectedMenuItemGuest.containsKey(l)) {

                        List<DishesSelected> selectedDishes=selectedMenuItemGuest.get(l);
                        List<Dish> dishesToCart=new ArrayList<>();

                        for(int k=0;k<selectedDishes.size();k++){
                            for (int i = 0; i < parentModelClassList.size(); i++) {
                                for (int j = 0; j < parentModelClassList.get(i).getDishList().size(); j++) {
                                    if(parentModelClassList.get(i).getDishList().get(j).getDishName().contains(selectedDishes.get(k).getDishName())){
                                        dishesToCart.add(parentModelClassList.get(i).getDishList().get(j));
                                        dishesToCart.get(k).setDishCount(selectedDishes.get(k).getDishCount());
                                        price+=dishesToCart.get(k).getDishPrice();
                                    }
                                }
                            }
                        }

                        guestCarts.add(new GuestCart(l+1,dishesToCart));

                        saveCartMap.put(Integer.toString(l),dishesToCart);

                    }
                }


                totalPriceTextView.setText(Integer.toString(price));
                guestCartAdapter = new GuestCartAdapter(getContext(), guestCarts,MenuFragment.this);
                // размещение элементов
                guestCart_rv.setLayoutManager(new LinearLayoutManager(getContext()));
                // Прикрепрепляем адаптер к recyclerView
                guestCart_rv.setAdapter(guestCartAdapter);
                guestCartAdapter.notifyDataSetChanged();
                cartBottomSheetDialog.show();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // вызывается при отправке запроса пользователем
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // вызывается при изменение запроса пользователем
            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });


        backOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderFragment orderFragment=new OrderFragment();
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                fragmentTransaction.replace(R.id.fl_content, orderFragment);
                fragmentTransaction.commit();
            }
        });



        for(int i=0;i<guestCount;i++){
            if(i==0){
                guestList.add(new ChipModel("Гость "+Integer.toString(i+1),true));
            }
            else{
                guestList.add(new ChipModel("Гость "+Integer.toString(i+1),false));
            }
        }

        chipAdapter=new ChipAdapter(getContext(),guestList,MenuFragment.this);
        chipRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        chipRecyclerView.setAdapter(chipAdapter);
        chipAdapter.notifyDataSetChanged();



        return view;
    }


    HashMap<Integer,List<DishesSelected>> selectedMenuItemGuest;

    // FoodCategoryAdapter
    @Override
    public void onAddItemClick(Dish dish, int categoryPosition, int dishPosition) {

        Log.d("SelectedItem", categoryPosition+" "+dishPosition);

        List<DishesSelected> selectedDishes=new ArrayList<>();

        if(selectedMenuItemGuest.containsKey(guestNumber)){
            selectedDishes=selectedMenuItemGuest.get(guestNumber);
        }


        Boolean setCountDish=false;

        for(int i=0;i<selectedDishes.size();i++){
            if(selectedDishes.get(i).getDishName().contains(dish.getDishName())){
                if(dish.getDishCount()==0){
                    selectedDishes.remove(i);
                }
                else {
                    selectedDishes.get(i).setDishCount(dish.getDishCount());
                }
                setCountDish = true;
            }
        }
        if(!setCountDish){
            selectedDishes.add(new DishesSelected(dish.getDishName(),dish.getDishCount(),dish));
        }

        selectedMenuItemGuest.put(guestNumber,selectedDishes);


        for(int i=0;i<selectedDishes.size();i++){
            Log.d("list of selected item", "dishes name="+selectedDishes.get(i).getDishName()+" count="+selectedDishes.get(i).getDishCount());
        }


        List<DishesSelected> checkList=selectedMenuItemGuest.get(guestNumber);
        for(int i=0;i<checkList.size();i++){
            Log.d("list check", "dishes name="+checkList.get(i).getDishName()+" count="+checkList.get(i).getDishCount());
        }

    }

    // guestCartAdapter
    // выбор гостя Chip
    Integer guestNumber;
    @Override
    public void onItemClick(int position) {
        guestNumber=position;
        for(int i=0;i<guestCount;i++){
                Chip chip1=(Chip) chipRecyclerView.getChildAt(i);
                if(i!=position){
                        chip1.setChecked(false);
                    }
                else{
                        chip1.setChecked(true);
                    }
                Log.d("pos----",Integer.toString(position));
            }


        if(selectedMenuItemGuest.containsKey(guestNumber)){

            List<DishesSelected> dishes=selectedMenuItemGuest.get(guestNumber);

            for(int i=0;i<parentModelClassList.size();i++){
                for(int j=0;j<parentModelClassList.get(i).getDishList().size();j++){
                    Boolean isContain=false;
                    for(int k=0;k<dishes.size();k++){
                        if(parentModelClassList.get(i).getDishList().get(j).getDishName().contains(dishes.get(k).getDishName())){
                            parentModelClassList.get(i).getDishList().get(j).setDishCount(dishes.get(k).getDishCount());
                            adapter.notifyDataSetChanged();
                            isContain=true;
                            Log.d("contains value",parentModelClassList.get(i).getDishList().get(j).getDishName()+" "+parentModelClassList.get(i).getDishList().get(j).getDishCount());

                        }
                    }
                    if(!isContain) {
                        Log.d("!contains value",parentModelClassList.get(i).getDishList().get(j).getDishName()+" "+parentModelClassList.get(i).getDishList().get(j).getDishCount());
                        parentModelClassList.get(i).getDishList().get(j).setDishCount(0);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
        else{
            for(int i=0;i<parentModelClassList.size();i++){
                for(int j=0;j<parentModelClassList.get(i).getDishList().size();j++){
                    parentModelClassList.get(i).getDishList().get(j).setDishCount(0);
                    adapter.notifyDataSetChanged();
                }
            }
        }




    }

    // guestCartAdapter
    @Override
    public void onDeleteCartItemClick2(GuestCart guestCart, int position, int positionDish) {

        Toast.makeText(getActivity(), guestCart.getDishList().get(positionDish).getDishName(),
                Toast.LENGTH_LONG).show();

        totalPriceTextView.setText(Integer.toString(Integer.parseInt(totalPriceTextView.getText().toString())-
                (guestCart.getDishList().get(positionDish).getDishPrice()*
                        guestCart.getDishList().get(positionDish).getDishCount())));

        guestCarts.get(position).getDishList().remove(positionDish);
        guestCartAdapter.notifyDataSetChanged();


        List<DishesSelected> dishes=selectedMenuItemGuest.get(guestCart.getGuestName()-1);

        Log.d("guestCart number", guestCart.getGuestName()+"");
        Log.d("key set list dishes",selectedMenuItemGuest.keySet()+"");
        for(int i=0;i<dishes.size();i++){
            Log.d("dishes list", dishes.get(i).getDishName()+"");
        }

        dishes.remove(positionDish);
        selectedMenuItemGuest.put(guestCart.getGuestName()-1,dishes);

        onItemClick(guestCart.getGuestName()-1);

        if(dishes.isEmpty()){
            selectedMenuItemGuest.remove(guestCart.getGuestName()-1);
        }
        if(guestCarts.get(position).getDishList().isEmpty()){
            guestCarts.remove(position);
            guestCartAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onNewCartItemCount2(GuestCart guestCart, int position, int positionDish) {

        //Log.d("dish count", guestCart.getDishList().get(positionDish).getDishCount()+"");
////
        //guestCarts.get(position).getDishList().get(positionDish).setDishPrice(guestCart.getDishList().get(positionDish).getDishPrice()+
        //        (guestCart.getDishList().get(positionDish).getDishPrice()/
        //                guestCart.getDishList().get(positionDish).getDishCount()));
        //guestCarts.get(position).getDishList().get(positionDish).setDishCount(guestCart.getDishList().get(positionDish).getDishCount()+1);
////
        //guestCartAdapter.notifyDataSetChanged();
////
        //List<DishesSelected> dishes=selectedMenuItemGuest.get(guestCart.getGuestName()-1);
        //dishes.get(positionDish).setDishCount(guestCarts.get(position).getDishList().get(positionDish).getDishCount());
        //selectedMenuItemGuest.put(guestCart.getGuestName()-1,dishes);
////
        //onItemClick(guestCart.getGuestName()-1);

    }

    @Override
    public void onRemoveCartItemCount2(GuestCart guestCart, int position, int positionDish) {

        //guestCarts.get(position).getDishList().get(positionDish).setDishPrice(guestCart.getDishList().get(positionDish).getDishPrice()-
        //        (guestCart.getDishList().get(positionDish).getDishPrice()/
        //                guestCart.getDishList().get(positionDish).getDishCount()));
        //guestCarts.get(position).getDishList().get(positionDish).setDishCount(guestCart.getDishList().get(positionDish).getDishCount()-1);
////
        //guestCartAdapter.notifyDataSetChanged();
////
        //List<DishesSelected> dishes=selectedMenuItemGuest.get(guestCart.getGuestName()-1);
        //dishes.get(positionDish).setDishCount(guestCarts.get(position).getDishList().get(positionDish).getDishCount());
        //selectedMenuItemGuest.put(guestCart.getGuestName()-1,dishes);
////
        //onItemClick(guestCart.getGuestName()-1);
    }


    Integer guestCount;
    Integer tableNumber;
    String waiter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null) {
            guestCount = getArguments().getInt(ARG_PARAM1);
            tableNumber= getArguments().getInt(ARG_PARAM2);
            waiter= getArguments().getString(ARG_PARAM3);
        }
    }



    //  поиск по категориям
    public void filterList(String text) {

        ArrayList<FoodCategory> newList = new ArrayList<>();

        for (int i = 0; i < parentModelClassList.size(); i++) {
            ArrayList<Dish> filteredlist = new ArrayList<>();
            for (int j = 0; j < parentModelClassList.get(i).getDishList().size(); j++) {
                if (parentModelClassList.get(i).getDishList().get(j).getDishName().toLowerCase().contains(text.toLowerCase())) {
                    filteredlist.add(parentModelClassList.get(i).getDishList().get(j));
                }
            }
            if (!filteredlist.isEmpty()) {
                newList.add(new FoodCategory(parentModelClassList.get(i).getCategoryName(), filteredlist));
            }
        }

        if (newList.isEmpty()) {
            ArrayList<FoodCategory> emptyList = new ArrayList<>();

            adapter.setFilterList(emptyList);
        } else {
            adapter.setFilterList(newList);
        }
    }

    TextView totalPriceTextView;

    // создание Bottom Sheets
    private void createDialog(){
        View view =getLayoutInflater().inflate(R.layout.cart_bottom_sheets,null,false);

        createOrderButton=view.findViewById(R.id.createOrderButton);

//
       createOrderButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               HashMap<String,List<String>> list=new HashMap<>();
               Integer price=0;


               for(int i=0;i<guestCount;i++){
                   for(int j=0;j<guestCarts.size();j++){
                       Log.d("guest key 4", guestCarts.get(j).getGuestName()+"");
                       if(guestCarts.get(j).getGuestName().equals(i+1)){

                           Log.d("guest key 1", guestCarts.get(j).getGuestName()+"");
                           List<String> dish=new ArrayList<>();
                           for(int k=0;k<guestCarts.get(j).getDishList().size();k++){
                               for(int l=0;l<guestCarts.get(j).getDishList().get(k).getDishCount();l++){
                                   dish.add(guestCarts.get(j).getDishList().get(k).getDishName());
                                   price+=guestCarts.get(j).getDishList().get(k).getDishPrice();

                                   Log.d("dish 2", dish.get(l)+"");
                               }
                           }
                           list.put(Integer.toString(i),dish);
                       }
                   }
               }


               Map<String, Object> order = new HashMap<>();
               order.put("id", UUID.randomUUID().toString());
               order.put("table", tableNumber);
               order.put("price", price);
               order.put("status", "open");
               order.put("idWaiter", waiter);
               order.put("datetime", new Date());
               order.put("orders", list);



               // Add a new document with a generated ID
               db.collection("Orders")
                        .add(order)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

               setTableChecked(tableNumber);
               Toast.makeText(getActivity(), "Заказ создан",
                         Toast.LENGTH_LONG).show();
            }
        });

        cartRecyclerView=view.findViewById(R.id.rv_cart);
        cartBottomSheetDialog=new BottomSheetDialog(getContext());
        cartBottomSheetDialog.setContentView(view);
        guestCart_rv=(RecyclerView) view.findViewById(R.id.rv_cart);
        totalPriceTextView=(TextView) view.findViewById(R.id.totalPriceCart);
    }

    private DatabaseReference mDatabase;

    private void setTableChecked(Integer selectedTable){

        Map<String, Object> table = new HashMap<>();
        table.put("isBusy", true);

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


    public void readMenu(){

        db.collection("Dishes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                FoodCategory foodCategory=document.toObject(FoodCategory.class);

                                parentModelClassList.add(foodCategory);

                                // Создание адаптера
                                adapter = new FoodCategoryAdapter(getContext(), parentModelClassList,MenuFragment.this);
                                // размещение элементов
                                parent_rv.setLayoutManager(new LinearLayoutManager(getContext()));
                                // Прикрепрепляем адаптер к recyclerView
                                parent_rv.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                    }
                });

    }


    public void setInitialData(){
        breakfastList.add(new Dish(349, "Английский завтрак",R.drawable.img,0));
        breakfastList.add(new Dish(319,"Morning plate #1",R.drawable.img_1,0));
        breakfastList.add(new Dish(319,"Омлет с фермерским окороком и сыром",R.drawable.img_2,0));
        breakfastList.add(new Dish(439,"Омлет с форелью и сливочным сыром",R.drawable.img_3,0));
        breakfastList.add(new Dish(279,"Гречка с беконом и яйцом пашот",R.drawable.img_4,0));
        breakfastList.add(new Dish(299,"Сырники со сметаной",R.drawable.img_5,0));
        breakfastList.add(new Dish(299,"Сырника с соусом крем брюле",R.drawable.img_6,0));
        breakfastList.add(new Dish(149,"Блинчики со сливочным маслом",R.drawable.img_7,0));
        breakfastList.add(new Dish(149,"Глазунья",R.drawable.img_8,0));
        breakfastList.add(new Dish(149,"Каша рисовая",R.drawable.img_9,0));
        breakfastList.add(new Dish(149,"Каша овсяная",R.drawable.img_10,0));
        breakfastList.add(new Dish(179,"Пшенная каша с тыквой",R.drawable.img_11,0));
        breakfastList.add(new Dish(439,"Жареный творог",R.drawable.img_12,0));
        breakfastList.add(new Dish(449,"Боул",R.drawable.img_13,0));
        breakfastList.add(new Dish(149,"Каша пшенная",R.drawable.img_14,0));
        breakfastList.add(new Dish(379,"Гриль сэндвич с курицей",R.drawable.img_15,0));


        parentModelClassList.add(new FoodCategory("Завтраки", breakfastList));

        saladList.add(new Dish(349,"Салат цезарь с куриной грудкой",R.drawable.img_16,0));
        saladList.add(new Dish(429,"Салат цезарь с креветками",R.drawable.img_17,0));
        saladList.add(new Dish(249,"Салат с паштетом из куриной печени",R.drawable.img_18,0));
        saladList.add(new Dish(419,"Салат с пастрами",R.drawable.img_19,0));
        saladList.add(new Dish(349,"Салат с печеной свеклой",R.drawable.img_20,0));


        parentModelClassList.add(new FoodCategory("Салаты", saladList));


        mainDishList.add(new Dish(349,"Строганов из индейки с грибами и гречей",R.drawable.img_21,0));
        mainDishList.add(new Dish(349,"Оладьи из курицы с картофельным пюре",R.drawable.img_22,0));
        mainDishList.add(new Dish(519,"Паста с милиями и креветками",R.drawable.img_23,0));
        mainDishList.add(new Dish(349,"Паста Карбонара",R.drawable.img_24,0));
        mainDishList.add(new Dish(449,"Киноа с тыквой",R.drawable.img_25,0));
        mainDishList.add(new Dish(379,"Жареные сосиски",R.drawable.img_26,0));
        mainDishList.add(new Dish(379,"Паста с индейкой, тыквой и печеным баклажаном",R.drawable.img_27,0));


        parentModelClassList.add(new FoodCategory("Горячие блюда", mainDishList));

        dessertList.add(new Dish(179,"Пирожное Наполеон",R.drawable.img_28,0));
        dessertList.add(new Dish(329,"Тарталетка с клубникой",R.drawable.img_29,0));
        dessertList.add(new Dish(379,"Тарталетка с лесными ягодами",R.drawable.img_30,0));
        dessertList.add(new Dish(169,"Пирожное Сметанник",R.drawable.img_31,0));
        dessertList.add(new Dish(249,"Пирожное Трюфельное",R.drawable.img_32,0));
        dessertList.add(new Dish(179,"Пирожное Красный бархат",R.drawable.img_33,0));
        dessertList.add(new Dish(69,"Макарон фисташка",R.drawable.img_34,0));
        dessertList.add(new Dish(69,"Макарон соленая карамель",R.drawable.img_35,0));

        parentModelClassList.add(new FoodCategory("Десерты", dessertList));

        drinkList.add(new Dish(119,"Вода Байкал газированная",R.drawable.img_36,0));
        drinkList.add(new Dish(119,"Вода Жемчужина негазированная",R.drawable.img_37,0));
        drinkList.add(new Dish(149,"Морс Бодрый Клубничный",R.drawable.img_38,0));
        drinkList.add(new Dish(149,"Морс Бодрый Клюквенный",R.drawable.img_39,0));
        drinkList.add(new Dish(149,"Морс Бодрый Клубника-банан",R.drawable.img_40,0));
        drinkList.add(new Dish(149,"Морс Бодрый Манго-маракуйя",R.drawable.img_41,0));
        drinkList.add(new Dish(149,"Морс Бодрый Ягодный",R.drawable.img_42,0));

        parentModelClassList.add(new FoodCategory("Напитки", drinkList));

    }

}
