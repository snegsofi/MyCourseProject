package com.example.courseproject;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuFragment extends Fragment implements CartAdapter.ItemClickListener,
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
    List<Dish> cartList;
    CartAdapter cartAdapter;
    GuestCartAdapter guestCartAdapter;
    ChipAdapter chipAdapter;

    RecyclerView chipRecyclerView;
    ChipGroup chipGroup;

    List<ChipModel> guestList;

    HashMap<Integer,List<DishesSelected>> dishesSelectedHashMap;

    List<GuestCart> guestCarts;

    private static final String ARG_PARAM1="param1";

    public MenuFragment(){

    }

    public static MenuFragment newInstance(int guestCount)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1,guestCount);
        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.menu_fragment,container,false);


        backOrderButton=view.findViewById(R.id.backOrderButton);
        toCartButton=view.findViewById(R.id.toCart_Button);
        searchView=view.findViewById(R.id.searchView);
        chipGroup=view.findViewById(R.id.chipGroup);
        chipRecyclerView=view.findViewById(R.id.rv_chip);

        guestCarts=new ArrayList<>();

        // получаем идентификатор по имени файла
        int ids = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(ids);
        textView.setTextColor(Color.BLACK);

        searchView.clearFocus();

        RecyclerView parent_rv=(RecyclerView) view.findViewById(R.id.rv_parent);

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

        setInitialData();


        toCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createDialog();
                //cartList.add(new Dish(349, "Английский завтрак",R.drawable.img));

                Log.d("4",Integer.toString(cartList.get(0).getDishCount()));
                //guestCartAdapter=new GuestCartAdapter(getContext(),guestCarts,MenuFragment.this);
                //cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                //cartRecyclerView.setAdapter(cartAdapter);
                //guestCartAdapter.notifyDataSetChanged();
                //cartBottomSheetDialog.show();

                cartAdapter = new CartAdapter(getContext(), cartList,MenuFragment.this);
                // размещение элементов
                cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                // Прикрепрепляем адаптер к recyclerView
                cartRecyclerView.setAdapter(cartAdapter);
                cartAdapter.notifyDataSetChanged();
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

        // Создание адаптера
        adapter = new FoodCategoryAdapter(getContext(), parentModelClassList,MenuFragment.this);
        // размещение элементов
        parent_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        // Прикрепрепляем адаптер к recyclerView
        parent_rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
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


    RecyclerView cartRecyclerView;
    BottomSheetDialog cartBottomSheetDialog;
    // создание Bottom Sheets
    private void createDialog(){
        View view =getLayoutInflater().inflate(R.layout.cart_bottom_sheets,null,false);

        cartRecyclerView=view.findViewById(R.id.rv_cart);
        cartBottomSheetDialog=new BottomSheetDialog(getContext());
        cartBottomSheetDialog.setContentView(view);
    }



    public void setInitialData(){
        breakfastList.add(new Dish(349, "Английский завтрак",R.drawable.img));
        breakfastList.add(new Dish(319,"Morning plate #1",R.drawable.img_1));
        breakfastList.add(new Dish(319,"Омлет с фермерским окороком и сыром",R.drawable.img_2));
        breakfastList.add(new Dish(439,"Омлет с форелью и сливочным сыром",R.drawable.img_3));
        breakfastList.add(new Dish(279,"Гречка с беконом и яйцом пашот",R.drawable.img_4));
        breakfastList.add(new Dish(299,"Сырники со сметаной",R.drawable.img_5));
        breakfastList.add(new Dish(299,"Сырника с соусом крем брюле",R.drawable.img_6));
        breakfastList.add(new Dish(149,"Блинчики со сливочным маслом",R.drawable.img_7));
        breakfastList.add(new Dish(149,"Глазунья",R.drawable.img_8));
        breakfastList.add(new Dish(149,"Каша рисовая",R.drawable.img_9));
        breakfastList.add(new Dish(149,"Каша овсяная",R.drawable.img_10));
        breakfastList.add(new Dish(179,"Пшенная каша с тыквой",R.drawable.img_11));
        breakfastList.add(new Dish(439,"Жареный творог",R.drawable.img_12));
        breakfastList.add(new Dish(449,"Боул",R.drawable.img_13));
        breakfastList.add(new Dish(149,"Каша пшенная",R.drawable.img_14));
        breakfastList.add(new Dish(379,"Гриль сэндвич с курицей",R.drawable.img_15));


        parentModelClassList.add(new FoodCategory("Завтраки", breakfastList));

        saladList.add(new Dish(349,"Салат цезарь с куриной грудкой",R.drawable.img_16));
        saladList.add(new Dish(429,"Салат цезарь с креветками",R.drawable.img_17));
        saladList.add(new Dish(249,"Салат с паштетом из куриной печени",R.drawable.img_18));
        saladList.add(new Dish(419,"Салат с пастрами",R.drawable.img_19));
        saladList.add(new Dish(349,"Салат с печеной свеклой",R.drawable.img_20));


        parentModelClassList.add(new FoodCategory("Салаты", saladList));


        mainDishList.add(new Dish(349,"Строганов из индейки с грибами и гречей",R.drawable.img_21));
        mainDishList.add(new Dish(349,"Оладьи из курицы с картофельным пюре",R.drawable.img_22));
        mainDishList.add(new Dish(519,"Паста с милиями и креветками",R.drawable.img_23));
        mainDishList.add(new Dish(349,"Паста Карбонара",R.drawable.img_24));
        mainDishList.add(new Dish(449,"Киноа с тыквой",R.drawable.img_25));
        mainDishList.add(new Dish(379,"Жареные сосиски",R.drawable.img_26));
        mainDishList.add(new Dish(379,"Паста с индейкой, тыквой и печеным баклажаном",R.drawable.img_27));


        parentModelClassList.add(new FoodCategory("Горячие блюда", mainDishList));

        dessertList.add(new Dish(179,"Пирожное Наполеон",R.drawable.img_28));
        dessertList.add(new Dish(329,"Тарталетка с клубникой",R.drawable.img_29));
        dessertList.add(new Dish(379,"Тарталетка с лесными ягодами",R.drawable.img_30));
        dessertList.add(new Dish(169,"Пирожное Сметанник",R.drawable.img_31));
        dessertList.add(new Dish(249,"Пирожное Трюфельное",R.drawable.img_32));
        dessertList.add(new Dish(179,"Пирожное Красный бархат",R.drawable.img_33));
        dessertList.add(new Dish(69,"Макарон фисташка",R.drawable.img_34));
        dessertList.add(new Dish(69,"Макарон соленая карамель",R.drawable.img_35));

        parentModelClassList.add(new FoodCategory("Десерты", dessertList));

        drinkList.add(new Dish(119,"Вода Байкал газированная",R.drawable.img_36));
        drinkList.add(new Dish(119,"Вода Жемчужина негазированная",R.drawable.img_37));
        drinkList.add(new Dish(149,"Морс Бодрый Клубничный",R.drawable.img_38));
        drinkList.add(new Dish(149,"Морс Бодрый Клюквенный",R.drawable.img_39));
        drinkList.add(new Dish(149,"Морс Бодрый Клубника-банан",R.drawable.img_40));
        drinkList.add(new Dish(149,"Морс Бодрый Манго-маракуйя",R.drawable.img_41));
        drinkList.add(new Dish(149,"Морс Бодрый Ягодный",R.drawable.img_42));

        parentModelClassList.add(new FoodCategory("Напитки", drinkList));

    }

    @Override
    public void onItemClick(Dish dish, int position) {

        Log.d("-----del",Integer.toString(position));
        cartList.remove(position);
        cartAdapter.notifyDataSetChanged();

    }

    @Override
    public void onNewItemCount(Dish dish, int position) {

        for(int i=0;i<parentModelClassList.size();i++){
            for(int j=0;j<parentModelClassList.get(i).getDishList().size();j++){
                if(parentModelClassList.get(i).getDishList().get(j).getDishName().contains(dish.getDishName())){
                    parentModelClassList.get(i).getDishList().get(j).setDishCount(dish.getDishCount());
                    Log.d("--count",Integer.toString(parentModelClassList.get(i).getDishList().get(j).getDishCount()));
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onAddItemClick(Dish dish, int categoryPosition, int dishPosition) {

        List<DishesSelected> list=new ArrayList<>();
        for (HashMap.Entry<Integer, List<DishesSelected>> entry : dishesSelectedHashMap.entrySet()) {
            List<DishesSelected> l=entry.getValue();
            Log.d("lost size", ""+l.size());

            for(int i=0;i<l.size();i++){
                list.add(new DishesSelected(l.get(i).getPositionParent(),l.get(i).getPositionChild(),l.get(i).getDishCount()));
            }

            //for(int i=0;i<l.size();i++){
            //    Log.d("=====", l.get(i).getPositionParent()+"="+categoryPosition+"  "+l.get(i).getPositionChild()+"="+dishPosition);
            //    if(l.get(i).getPositionParent()!=categoryPosition && l.get(i).getPositionChild()!=dishPosition){
            //        list.add(new DishesSelected(l.get(i).getPositionParent(),l.get(i).getPositionChild(),l.get(i).getDishCount()));
            //    }
            //}
        }

        for(int i=0;i<parentModelClassList.size();i++){
            for(int j=0;j<parentModelClassList.get(i).getDishList().size();j++){
                if(parentModelClassList.get(i).getDishList().get(j).getDishName().contains(dish.getDishName())){
                    list.add(new DishesSelected(i,j,parentModelClassList.get(i).getDishList().get(j).getDishCount()));
                    dishesSelectedHashMap.put(guestNumber,list);
                    Log.d("-----poi",i+" "+j+" "+parentModelClassList.get(i).getDishList().get(j).getDishCount());
                }
            }
        }

        for (HashMap.Entry<Integer, List<DishesSelected>> entry : dishesSelectedHashMap.entrySet()) {

            List<DishesSelected> l=entry.getValue();
            for(int i=0;i<l.size();i++){
                Log.d("----qwer", entry.getKey()+" "+ l.get(i).getPositionParent()+" "+l.get(i).getPositionChild()+" "+l.get(i).getDishCount());
            }
        }


        boolean isContain=false;
        for(int i=0;i<cartList.size();i++){
            if(dish.getDishName().contains(cartList.get(i).getDishName())){
                Integer count=cartList.get(i).getDishCount();
                cartList.get(i).setDishCount(count);
                isContain=true;

                guestCarts.add(new GuestCart("Гость 1", cartList));
            }
        }
        if(!isContain){
            cartList.add(dish);
        }


    }

    Integer guestCount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null) {
            guestCount = getArguments().getInt(ARG_PARAM1);
        }
    }


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

        for (HashMap.Entry<Integer, List<DishesSelected>> entry : dishesSelectedHashMap.entrySet()) {
            Log.d("entry key and position",entry.getKey()+" "+position);
            if(entry.getKey()==position){
                List<DishesSelected> list=entry.getValue();
                Log.d("list size",list.size()+"");
                for(int i=0;i<list.size();i++){
                    parentModelClassList.get(list.get(i).getPositionParent()).getDishList()
                            .get(list.get(i).getPositionChild())
                            .setDishCount(list.get(i).getDishCount());
                    Log.d("set count",parentModelClassList.get(list.get(i).getPositionParent()).getDishList()
                            .get(list.get(i).getPositionChild()).getDishCount()+"");
                    adapter.notifyDataSetChanged();
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
    }

    @Override
    public void onAddItemClick(GuestCart guestCart, int position) {

       //for(int i=0;i<parentModelClassList.size();i++){
       //    for(int j=0;j<parentModelClassList.get(i).getDishList().size();j++){
       //        if(parentModelClassList.get(i).getDishList().get(j).getDishName().contains(guestCart.getDishList().get(i).getDishName())){
       //            parentModelClassList.get(i).getDishList().get(j).setDishCount(guestCart.getDishList().get(i).getDishCount());
       //            Log.d("--count",Integer.toString(parentModelClassList.get(i).getDishList().get(j).getDishCount()));
       //            adapter.notifyDataSetChanged();
       //        }
       //    }
       //}
    }
}
